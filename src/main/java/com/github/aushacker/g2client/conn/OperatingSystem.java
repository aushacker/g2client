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
				if (port.getSystemPortName().startsWith("cu.u")) {
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
