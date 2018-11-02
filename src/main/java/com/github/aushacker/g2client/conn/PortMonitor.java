package com.github.aushacker.g2client.conn;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.json.Json;
import javax.json.JsonObject;

import com.fazecast.jSerialComm.SerialPort;
import com.github.aushacker.g2client.protocol.Command;

/**
 * @since October 2018
 * @author Stephen Davies
 */
public class PortMonitor {

	private static final int G2_BAUD = 115200;
	private static final int NUL = 0;	// ASCII NUL character
	private static final int ENQ = 5;	// ASCII ESC character
	private static final int LF = 10;	// ASCII LF character

	private static final int POLL_TIMEOUT = 100;

	private SerialPort port;

	private boolean shutdown;

	private PriorityBlockingQueue<Command> in;
	private BlockingQueue<JsonObject> out;

	public PortMonitor(SerialPort port) {
		this.port = port;
		this.in = new PriorityBlockingQueue<>();
		this.out = new LinkedBlockingQueue<>();
	}

	public Queue<Command> getIn() {
		return in;
	}

	public BlockingQueue<JsonObject> getOut() {
		return out;
	}

	public void shutdown() {
		shutdown = true;
	}

	public void start() {
		port.openPort();
		port.setComPortParameters(G2_BAUD, 8, 1, SerialPort.NO_PARITY);
		port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);

		new Thread(new ReceiveProcess()).start();

		for (int i = 0; i < 2; i++) {
			try {
				port.writeBytes(new byte[] { ENQ }, 1L);
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private class TransmitProcess implements Runnable {
		public void run() {
			while (!shutdown) {
				try {
					Command c = in.poll(POLL_TIMEOUT, TimeUnit.MILLISECONDS);
					
					if (c != null) {
						
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class ReceiveProcess implements Runnable {

		@Override
		public void run() {
			InputStream in = port.getInputStream();
			StringBuilder buff = new StringBuilder();

			while (!shutdown) {
				try {
					int c = in.read();
					if (c > NUL && c != LF) {
						buff.append((char)c);
					} else if (c == LF) {
						if (buff.length() > 0) {
							process(Json.createReader(new StringReader(buff.toString())).readObject());
							buff = new StringBuilder();
						}
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void process(JsonObject response) {
			out.add(response);
		}
	}
}
