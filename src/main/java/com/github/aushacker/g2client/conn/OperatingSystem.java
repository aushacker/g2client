package com.github.aushacker.g2client.conn;

import java.util.ArrayList;
import java.util.List;

import com.fazecast.jSerialComm.SerialPort;

/**
 * @since October 2018
 * @author Stephen Davies
 */
public enum OperatingSystem {
	MACOS() {
		protected List<SerialPort> filter(SerialPort[] ports) {
			ArrayList<SerialPort> result = new ArrayList<SerialPort>();
			
			for (SerialPort port : ports) {
				if (port.getSystemPortName().startsWith("cu")) {
					result.add(port);
				}
			}
			return result;
		}
	};

	protected abstract List<SerialPort> filter(SerialPort[] ports);
	
	public List<SerialPort> getFilteredPorts() {
		return filter(SerialPort.getCommPorts());
	}

	public static OperatingSystem current() {
		switch (System.getProperty("os.name")) {
		case "Mac OS X":
			return MACOS;
		default:
			throw new IllegalArgumentException();
		}
	}
}
