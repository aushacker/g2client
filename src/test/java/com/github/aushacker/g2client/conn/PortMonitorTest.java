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

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Stephen Davies
 * @since October 2018
 */
public class PortMonitorTest {

	public static void main(String[] args) {
		PortMonitor monitor = new PortMonitor(OperatingSystem.current().getFilteredPorts().get(0),
				new LinkedBlockingQueue<>());

		try {
			monitor.start();
			Thread.sleep(5000);
//			monitor.reset();
//			Thread.sleep(5000);
			monitor.enqueue("{\"1\":n}");
			monitor.enqueue("{\"2\":n}");
			monitor.enqueue("{\"3\":n}");
			monitor.enqueue("{\"4\":n}");
			monitor.enqueue("{\"5\":n}");
			monitor.enqueue("{\"xam\":1}");
			monitor.enqueue("{\"yam\":1}");
			monitor.enqueue("{\"di1\":{\"mo\":0,\"ac\":2,\"fn\":1}}");
			monitor.enqueue("{\"di2\":{\"mo\":0,\"ac\":2,\"fn\":1}}");
			monitor.enqueue("{\"di3\":{\"mo\":0,\"ac\":2,\"fn\":1}}");
			monitor.enqueue("{\"di4\":{\"mo\":0,\"ac\":2,\"fn\":1}}");
			monitor.enqueue("G0 X20 Y20");
			monitor.enqueue("G0 X0 Y0");
			//monitor.feedhold();
			//Thread.sleep(1000);
			//monitor.resume();
			Thread.sleep(10000);

			monitor.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
