package com.github.aushacker.g2client.conn;

import java.io.IOException;
import java.io.InputStream;

import com.fazecast.jSerialComm.SerialPort;

/**
 * @since October 2018
 * @author Stephen Davies
 */
public class PortMonitor {

	private static final int G2_BAUD = 115200;
	private static final byte ENQ = 5;

	private SerialPort port;

	private boolean shutdown;

	public PortMonitor(SerialPort port) {
		this.port = port;
	}

	private void configurePort() {
		port.setComPortParameters(G2_BAUD, 8, 1, SerialPort.NO_PARITY);
	}

	public static void main(String[] args) {
		PortMonitor monitor = new PortMonitor(SerialPort.getCommPort("/dev/cu.usbmodem1411"));

		monitor.start();
	}

	public void start() {
		configurePort();
		port.openPort();

		new Thread(new ReceiverProcess()).start();

		for (int i = 0; i < 2; i++) {
			try {
				port.writeBytes(new byte[] { ENQ }, 1L);
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		shutdown = true;

		port.closePort();
	}
	
	private class ReceiverProcess implements Runnable {

		@Override
		public void run() {
			InputStream in = port.getInputStream();
			int count = 0;
			
			while (!shutdown) {
				try {
					int c = in.read();
					if (c > 0) {
						count++;
						System.out.println(c);
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println(count);
		}
		
	}
}
