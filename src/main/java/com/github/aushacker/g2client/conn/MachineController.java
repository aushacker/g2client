/*
 * Copyright 2019 Stephen Davies
 *
 * This file is part of g2client.
 *
 * g2client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * g2client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2client. If not, see <https://www.gnu.org/licenses/>.
 */
package com.github.aushacker.g2client.conn;

import static com.github.aushacker.g2client.protocol.Constants.*;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.json.JsonValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;
import com.github.aushacker.g2client.protocol.Command;
import com.github.aushacker.g2client.protocol.Handler;
import com.github.aushacker.g2client.protocol.NoOpHandler;
import com.github.aushacker.g2client.protocol.PropertyHandler;
import com.github.aushacker.g2client.protocol.SingleCharacterCommand;
import com.github.aushacker.g2client.protocol.SingleCharacterType;
import com.github.aushacker.g2client.state.Axis;
import com.github.aushacker.g2client.state.MachineState;
import com.github.aushacker.g2client.state.Motor;

/**
 * @author Stephen Davies
 * @since March 2019
 */
public class MachineController implements IController {

	private final Logger logger = LoggerFactory.getLogger(MachineController.class);

	private MachineState machineState;

	private PortMonitor monitor;

	private Handler handler;

	private volatile boolean shutdown;

	private BlockingQueue<JsonValue> in;

	public MachineController() {
		machineState = new MachineState();
		shutdown = false;
		in = new LinkedBlockingQueue<>();

		registerHandlers();

		Thread t = new Thread(new ReceiveProcess(in), "ctl-rx");
		t.start();
	}

	public boolean connect(SerialPort port) {
		if (monitor != null) {
			monitor.shutdown();
			in.clear();
		}

		monitor = new PortMonitor(port, in);
		if (monitor.start()) {
			queryMachineState();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void enqueue(String data) {
		if (monitor != null) {
			monitor.enqueue(data);
		}
	}

	private void enqueueCommand(Command cmd) {
		if (monitor != null) {
			monitor.enqueueCommand(cmd);
		}
	}

	@Override
	public void feedhold() {
		enqueueCommand(new SingleCharacterCommand(SingleCharacterType.FEEDHOLD));
	}

	@Override
	public void flush() {
		enqueueCommand(new SingleCharacterCommand(SingleCharacterType.QUEUE_FLUSH));
	}

	@Override
	public MachineState getMachineState() {
		return machineState;
	}

	@Override
	public void goToMachineZero(Axis axis) {
		enqueue("G53 G0 " + axis + "0");
	}

	@Override
	public void goToZero(List<Axis> axes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void homeMachine(Axis axis) {
		enqueue("G28.2 " + axis + "0");
	}

	@Override
	public void homeMachine(List<Axis> axes) {
		StringBuilder cmd = new StringBuilder("G28.2 ");
		
		axes.forEach(axis -> cmd.append("" + axis + "0"));

		enqueue(cmd.toString());
	}

	@Override
	public void jog(Axis axis, boolean positive) {
		// TODO
	}

	@Override
	public void killJob() {
		enqueueCommand(new SingleCharacterCommand(SingleCharacterType.KILL_JOB));
	}

	/**
	 * Run a bunch of commands to determine the initial machine state.
	 */
	private void queryMachineState() {
		enqueue("$");			// Hardware/firmware values
		resetLineCounter();
		enqueue("{\"sr\":n}");	// Current status

		// Motors 1-6
//		for (int i = 1; i <= MachineState.MOTOR_COUNT; i++) {
//			enqueue("{\"" + i + "\":n}");
//		}

		// All axes
		//enqueue("{\"x\":n}");

		// Config for all digital inputs
//		for (int i = 1; i <= MachineState.DINPUT_COUNT; i++) {
//			enqueue("{\"di" + i + "\":n}");
//		}
	}

	public void registerHandlers() {
		handler = new Handler();
		
		// Handles values nested under {"r":...}
		Handler response = new Handler();
		registerSettings(response);
		registerSystem(response);
		registerStatusHandler(response);
		registerMotors(response);
		handler.register(RESPONSE, response);

		// Handles values nested under {...,"f":...}
		handler.register(FOOTER, new NoOpHandler());

		// Register for asynchronous status responses
		registerStatusHandler(handler);
	}

	/**
	 * Configure handlers for a single motor axis.
	 */
	private void registerMotor(Handler parent, int index) {
		Handler mHandler = new Handler();
		Motor motor = machineState.getMotors(index);
		mHandler.register("ma", new PropertyHandler(motor, "axis"));
		mHandler.register("sa", new PropertyHandler(motor, "stepAngle"));
		mHandler.register("tr", new PropertyHandler(motor, "travelPerRev"));
		mHandler.register("mi", new PropertyHandler(motor, "microsteps"));
		mHandler.register("su", new PropertyHandler(motor, "stepsPerUnit"));
		mHandler.register("po", new PropertyHandler(motor, "polarity"));
		mHandler.register("pm", new PropertyHandler(motor, "powerMode"));
		mHandler.register("pl", new PropertyHandler(motor, "powerLevel"));
		
		// Model indexes are 0 based, Json values are 1 based
		parent.register("" + ((char)('1' + index)), mHandler);
	}

	private void registerMotors(Handler parent) {
		for (int i = 0; i < MachineState.MOTOR_COUNT; i++) {
			registerMotor(parent, i);
		}
	}

	/**
	 * g2 reports the machines system settings at different times with different JSon
	 * nesting levels.
	 */
	private void registerSettings(Handler parent) {	
		parent.register("fv", new PropertyHandler(machineState, "firmwareVersion"));
		parent.register("fb", new PropertyHandler(machineState, "firmwareBuild"));
		parent.register("fbs", new PropertyHandler(machineState, "firmwareBuildString"));
		parent.register("fbc", new PropertyHandler(machineState, "firmwareConfig"));
		parent.register("hp", new NoOpHandler());
		parent.register("hv", new PropertyHandler(machineState, "hardwareVersion"));
		parent.register("id", new NoOpHandler());
		parent.register("jt", new NoOpHandler());
		parent.register("ct", new NoOpHandler());
		parent.register("sl", new NoOpHandler());
		parent.register("lim", new NoOpHandler());
		parent.register("saf", new NoOpHandler());
		parent.register("m48e", new NoOpHandler());
		parent.register("mfoe", new NoOpHandler());
		parent.register("mfo", new NoOpHandler());
		parent.register("mtoe", new NoOpHandler());
		parent.register("mto", new NoOpHandler());
		parent.register("mt", new NoOpHandler());
		parent.register("spep", new NoOpHandler());
		parent.register("spdp", new NoOpHandler());
		parent.register("spph", new NoOpHandler());
		parent.register("spdw", new NoOpHandler());
		parent.register("ssoe", new NoOpHandler());
		parent.register("sso", new NoOpHandler());
		parent.register("copf", new NoOpHandler());
		parent.register("comp", new NoOpHandler());
		parent.register("cofp", new NoOpHandler());
		parent.register("coph", new NoOpHandler());
		parent.register("tv", new NoOpHandler());
		parent.register("ej", new NoOpHandler());
		parent.register("jv", new NoOpHandler());
		parent.register("qv", new NoOpHandler());
		parent.register("sv", new NoOpHandler());
		parent.register("si", new NoOpHandler());
		parent.register("gpl", new NoOpHandler());
		parent.register("gun", new NoOpHandler());
		parent.register("gco", new NoOpHandler());
		parent.register("gpa", new NoOpHandler());
		parent.register("gdi", new NoOpHandler());
		parent.register("msg", new NoOpHandler());
	}

	private void registerStatusHandler(Handler parent) {
		Handler sHandler = new Handler();

		sHandler.register("feed", new PropertyHandler(machineState, "feedRate"));
		sHandler.register("line", new PropertyHandler(machineState, "line"));
		sHandler.register("posx", new PropertyHandler(machineState, "x"));
		sHandler.register("posy", new PropertyHandler(machineState, "y"));
		sHandler.register("posz", new PropertyHandler(machineState, "z"));
		sHandler.register("stat", new PropertyHandler(machineState, "status"));
		sHandler.register("vel", new PropertyHandler(machineState, "velocity"));

		parent.register(STATUS, sHandler);
	}

	/**
	 * g2 reports the machines system settings at different times with different JSon
	 * nesting levels.
	 */
	private void registerSystem(Handler parent) {
		Handler handler = new Handler();
		registerSettings(handler);
		
		parent.register(SYSTEM, handler);
	}

	public void reset() {
		enqueueCommand(new SingleCharacterCommand(SingleCharacterType.RESET));
	}

	@Override
	public void resetLineCounter() {
		enqueue("{\"line\":0}");
	}

	@Override
	public void resume() {
		enqueueCommand(new SingleCharacterCommand(SingleCharacterType.RESUME));
	}

	public void shutdown() {
		shutdown = true;
		if (monitor != null) {
			monitor.shutdown();
		}
	}

	@Override
	public void zero(Axis axis) {
		enqueue("G0 " + axis + "0");
	}

	@Override
	public void zeroMachine(Axis axis) {
		enqueue("G28.3 " + axis + "0");
	}

	private class ReceiveProcess implements Runnable {

		private BlockingQueue<JsonValue> in;
		
		private ReceiveProcess(BlockingQueue<JsonValue> in) {
			this.in = in;
		}

		@Override
		public void run() {
			logger.info("Controller receive process running");
			
			while (!shutdown) {
				try {
					JsonValue rsp = in.poll(100, TimeUnit.MILLISECONDS);

					if (rsp != null) {
						handler.handle(rsp);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			logger.info("Receive process teminated");
		}
	}
}
