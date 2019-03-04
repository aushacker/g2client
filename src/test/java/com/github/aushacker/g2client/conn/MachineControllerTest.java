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

/**
 * @author Stephen Davies
 * @since October 2019
 */
public class MachineControllerTest {

	public static void main(String[] args) {
		MachineController controller = new MachineController();
		controller.connect(OperatingSystem.current().getFilteredPorts().get(0));

		try {
			Thread.sleep(2000);
//			controller.enqueue("{\"1\":n}");
//			controller.enqueue("{\"2\":n}");
//			controller.enqueue("{\"3\":n}");
//			controller.enqueue("{\"4\":n}");
//			controller.enqueue("{\"5\":n}");
//			controller.enqueue("{\"x\":n}");
			controller.enqueue("{\"x\":{\"am\":1}}");
			controller.enqueue("{\"y\":{\"am\":1}}");
//			controller.enqueue("{\"di1\":{\"mo\":0,\"ac\":2,\"fn\":1}}");
//			controller.enqueue("{\"di2\":{\"mo\":0,\"ac\":2,\"fn\":1}}");
//			controller.enqueue("{\"di3\":{\"mo\":0,\"ac\":2,\"fn\":1}}");
//			controller.enqueue("{\"di4\":{\"mo\":0,\"ac\":2,\"fn\":1}}");
			controller.enqueue("G0 X20 Y20");
			controller.enqueue("G0 X0 Y0");
			//controller.feedhold();
			//Thread.sleep(1000);
			//controller.resume();
			Thread.sleep(10000);

			controller.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
