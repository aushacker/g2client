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
public class MachineController {

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

	public void feedhold() {
		enqueueCommand(new SingleCharacterCommand(SingleCharacterType.FEEDHOLD));
	}

	public void flush() {
		enqueueCommand(new SingleCharacterCommand(SingleCharacterType.QUEUE_FLUSH));
	}

	public MachineState getMachineState() {
		return machineState;
	}

	public void goToMachineZero(Axis axis) {
		enqueue("G53 G0 " + axis + "0");
	}

	public void homeMachine(Axis axis) {
		enqueue("G28.2 " + axis + "0");
	}

	public void killJob() {
		enqueueCommand(new SingleCharacterCommand(SingleCharacterType.KILL_JOB));
	}

	/**
	 * Run a bunch of command to determine the initial machine state.
	 */
	private void queryMachineState() {
		enqueue("{\"sr\":n}");

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
		
		Handler hResponse = new Handler();
		hResponse.register("fv", new PropertyHandler(machineState, "firmwareVersion"));
		hResponse.register("fb", new PropertyHandler(machineState, "firmwareBuild"));
		hResponse.register("fbs", new NoOpHandler());
		hResponse.register("fbc", new NoOpHandler());
		hResponse.register("hp", new NoOpHandler());
		hResponse.register("hv", new NoOpHandler());
		hResponse.register("id", new NoOpHandler());
		hResponse.register("msg", new NoOpHandler());

		handler.register(RESPONSE, hResponse);
		handler.register(FOOTER, new NoOpHandler());

		registerMotors(hResponse);

		// Register for asynchronous status responses
		registerStatusHandler(handler);
		// Register for client requested status
		registerStatusHandler(hResponse);
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

	public void reset() {
		enqueueCommand(new SingleCharacterCommand(SingleCharacterType.RESET));
	}

	public void resetLineCounter() {
		enqueue("{\"line\":0}");
	}

	public void resume() {
		enqueueCommand(new SingleCharacterCommand(SingleCharacterType.RESUME));
	}

	public void shutdown() {
		shutdown = true;
		if (monitor != null) {
			monitor.shutdown();
		}
	}

	public void zeroMachine(Axis axis) {
		enqueue("G28.3 " + axis + "0");
	}

	private class ReceiveProcess implements Runnable {

		private BlockingQueue<JsonValue> in;
		
		private ReceiveProcess(BlockingQueue<JsonValue> in) {
			this.in = in;
		}

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
