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
import java.util.concurrent.TimeUnit;

import javax.json.JsonValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;
import com.github.aushacker.g2client.protocol.Handler;
import com.github.aushacker.g2client.protocol.NoOpHandler;
import com.github.aushacker.g2client.protocol.PropertyHandler;
import com.github.aushacker.g2client.state.MachineState;
import com.github.aushacker.g2client.state.Motor;

/**
 * @author Stephen Davies
 * @since March 2019
 */
public class Controller {

	private final Logger logger;

	private MachineState machineState;

	private PortMonitor monitor;

	private Handler handler;

	private volatile boolean shutdown;

	public Controller(SerialPort port) {
		logger = LoggerFactory.getLogger(Controller.class);
		machineState = new MachineState();
		monitor = new PortMonitor(port);
		shutdown = false;

		registerHandlers();

		Thread t = new Thread(new ReceiveProcess(monitor.getOut()), "ctl-rx");
		t.start();

		monitor.start();
	}

	public void enqueue(String data) {
		monitor.enqueue(data);
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
	}

	private void registerMotors(Handler parent) {
		for (int i = 0; i < MachineState.MOTOR_COUNT; i++) {
			registerMotor(parent, i);
		}
	}

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

	public void shutdown() {
		monitor.shutdown();
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

			logger.info("Controller receive process teminating");
		}
	}
}
