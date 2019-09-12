/*
 * Copyright 2018 Stephen Davies
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

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;
import com.github.aushacker.g2client.protocol.Command;
import com.github.aushacker.g2client.protocol.DataCommand;

/**
 * A multi-threaded handler for communicating with a G2 controller board
 * over a serial port. Manages the G2 linemode protocol (g2core 100 builds).
 *
 * @since October 2018
 * @author Stephen Davies
 */
public class PortMonitor {

	private static final int G2_BAUD = 115200;

	private final Logger logger = LoggerFactory.getLogger(PortMonitor.class);

	private SerialPort port;

	private volatile State state;
	
	private volatile boolean shutdown;

	private volatile boolean ack;

	private volatile int lineCount;

	private Object semaphore = new Object();

	private PriorityBlockingQueue<Command> in;
	private BlockingQueue<JsonValue> out;

	public PortMonitor(SerialPort port, BlockingQueue<JsonValue> out) {
		this.port = port;
		this.state = State.RESET;
		this.lineCount = 3;
		this.in = new PriorityBlockingQueue<>();
		this.out = out;
	}

	public void enqueue(String data) {
		enqueueCommand(new DataCommand(data));
	}

	public void enqueueCommand(Command cmd) {
		in.add(cmd);

		synchronized(semaphore) {
			semaphore.notify();
		}
	}

	public BlockingQueue<JsonValue> getOut() {
		return out;
	}

	private void initialisePort() {
		port.setComPortParameters(G2_BAUD, 8, 1, SerialPort.NO_PARITY);
		port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
	}

	private void setState(State state) {
		this.state = state;
		
		logger.info("State changed to {}", state);
	}

	public synchronized void shutdown() {
		shutdown = true;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		synchronized (semaphore) {
			semaphore.notify();
		}

		port.closePort();
	}

	public synchronized boolean start() {
		try {
			if (state == State.RESET) {
				logger.info("Setting up serial port {}", port);
				
				initialisePort();
				if (!port.openPort()) {
					logger.error("Cannot open serial port {}", port);
					return false;
				}

				startProcess(new ReceiveProcess(), "rx");
				startProcess(new TransmitProcess(), "tx");
				
				Thread.sleep(2 * BOARD_RESET_TIME);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return state == State.READY;
	}

	private void startProcess(Runnable process, String name) {
		Thread t = new Thread(process, name);
		t.setPriority(Thread.MAX_PRIORITY - 1);
		t.start();
	}

	private enum State {
		RESET, SYNCHRONISING, READY, FAILED;
	}

	private class TransmitProcess implements Runnable {

		private PrintStream out;

		@Override
		public void run() {
			logger.info("Transmit process running");

			out = new PrintStream(port.getOutputStream());

			while (!shutdown) {
				logger.debug("Input queue length: {} lineCount: {}", in.size(), lineCount);
				
				switch (state) {
				case RESET:
					try {
						Thread.sleep(BOARD_RESET_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					setState(State.SYNCHRONISING);
					break;
				case SYNCHRONISING:
					synchronise();
					break;
				default:
					try {
						Command cmd = in.peek();
						if (cmd == null) {
							// Wait for more data to arrive
							synchronized (semaphore) {
								logger.debug("Waiting, no data");
								semaphore.wait();
							}
						} else {
							// Always process control commands
							if (cmd.isControl()) {
								cmd = in.poll();
								cmd.printOn(out);
								logger.debug("sending: {}", cmd.toString());
							} else {
								if (lineCount > 0) {
									cmd = in.poll();	// in case queue has updated since peek
									cmd.printOn(out);
									logger.debug("sending: {}", cmd.toString());
									if (!cmd.isControl()) {
										lineCount--;
									}
								} else {
									// too many lines
									synchronized (semaphore) {
										logger.debug("Waiting, buffer full");
										semaphore.wait();
									}
								}
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			logger.info("Transmit process terminated");
		}

		private void synchronise() {
			ack = false;

			for (int i = 0; i < 10; i++) {
				out.write(ENQ);
				out.flush();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if (ack) {
					setState(State.READY);
					return;
				}
			}

			setState(State.FAILED);
			logger.error("Unable to synchronise with g2 board");
			port.closePort();
			shutdown = true;
		}
	}

	/**
	 * Handle the data coming back from the g2 controller. In general this is a
	 * stream of characters (LF delimited) forming JSON response packets.
	 */
	private class ReceiveProcess implements Runnable {

		@Override
		public void run() {
			logger.info("Receive process running");

			StringBuilder sb = new StringBuilder();
			InputStream in = port.getInputStream();
			
			try {
				while (!shutdown) {
					int c = in.read();

					if (c < NUL) {
						// TODO - should this force a state change to RESET so we begin again?
						break;
					}
					if (c == LF) {
						// 
						if (sb.length() > 0) {
							processResponse(sb.toString());
							sb = new StringBuilder();
						}
					} else {
						sb.append((char)c);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			logger.info("Receive process terminated");
		}

		/**
		 * G2 board has output a line (response of some kind). Perform basic
		 * parsing to determine if response is only for the PortMonitor or if
		 * it should be handled by a higher layer i.e. queued.
		 * 
		 * @param response Response from g2 board
		 */
		private void processResponse(String s) {
			logger.debug("g2 says: {}", s);

			if (s.startsWith("{")) {

				JsonReader rdr = Json.createReader(new StringReader(s));
				JsonValue jsonResponse = rdr.readValue();
				rdr.close();
				
				if (jsonResponse.getValueType() == ValueType.OBJECT) {
					JsonObject jo = (JsonObject) jsonResponse;
					if (jo.containsKey(ACKNOWLEDGEMENT)) {
						ack = true;
					} else if (jo.containsKey(RESPONSE)) {
						// More buffer space available, adjust linemode count and notify tx thread
						lineCount++;
						synchronized(semaphore) {
							semaphore.notify();
						}
						// Pass to higher layer
						out.add(jsonResponse);
					} else if (jo.containsKey(STATUS)) {
						// No effect on linemode, pass to higher layer.
						out.add(jsonResponse);
					}
				}
 			}
		}
	}
}
