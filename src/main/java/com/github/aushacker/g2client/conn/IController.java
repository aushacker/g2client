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

	public void enqueue(String command);

	public void feedhold();

	public void flush();

	public MachineState getMachineState();

	public void goToMachineZero(Axis axis);

	public void goToZero(List<Axis> axes);

	public void homeMachine(Axis axis);

	public void homeMachine(List<Axis> axes);

	public void killJob();

	public void resetLineCounter();

	public void resume();

	/**
	 * Set the current position to zero in the active fixture offset.
	 * Overwrites any data that was entered previously, i.e. absolute, 
	 * not relative.
	 *
	 * @param axis
	 */
	public void zero(Axis axis);

	public void zeroMachine(Axis axis);
}
