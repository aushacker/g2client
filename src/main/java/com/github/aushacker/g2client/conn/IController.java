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

import java.util.List;

import com.fazecast.jSerialComm.SerialPort;
import com.github.aushacker.g2client.state.Axis;
import com.github.aushacker.g2client.state.MachineState;

/**
 * Abstract machine operations. Intent is to allow stubbing
 * during unit testing.
 * 
 * @author Stephen Davies
 * @since March 2019
 */
public interface IController {

	boolean connect(SerialPort port);

	void enqueue(String command);

	void feedhold();

	void flush();

	MachineState getMachineState();

	void goToMachineZero(Axis axis);

	void goToZero(List<Axis> axes);

	void homeMachine(Axis axis);

	void homeMachine(List<Axis> axes);

	/**
	 * Instructs the controller to commence jogging. Distances and
	 * mode are to be taken from the MachineState.
	 * 
	 * @param axis Axis to jog
	 * @param positive when true jog in the positive direction
	 */
	void jog(Axis axis, boolean positive);

	void killJob();

	void resetLineCounter();

	void resume();

	void shutdown();

	/**
	 * Set the current position to zero in the active fixture offset.
	 * Overwrites any data that was entered previously, i.e. absolute, 
	 * not relative.
	 *
	 * @param axis
	 */
	void zero(Axis axis);

	void zeroMachine(Axis axis);
}
