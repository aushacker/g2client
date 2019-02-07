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

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.json.JsonObject;

import com.github.aushacker.g2client.protocol.Command;
import com.github.aushacker.g2client.protocol.DataCommand;

/**
 * @author Stephen Davies
 * @since October 2018
 */
public class PortMonitorTest {

	public static void main(String[] args) {
		PortMonitor monitor = new PortMonitor(OperatingSystem.current().getFilteredPorts().get(0));

		monitor.start();

		Queue<Command> tx = monitor.getIn();
		tx.add(new DataCommand("G21 G17"));

		BlockingQueue<JsonObject> rx = monitor.getOut();

		while (true) {
			JsonObject o = null;
			try {
				o = rx.poll(10, TimeUnit.SECONDS);
				if (o == null)
					break;
				System.out.println(o);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		monitor.shutdown();
	}

}
