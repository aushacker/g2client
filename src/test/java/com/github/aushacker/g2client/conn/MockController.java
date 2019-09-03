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
 * Mocked up controller for testing the UI.
 * 
 * @author Stephen Davies
 * @since March 2019
 */
public class MockController implements IController {

	private MachineState state;

	public MockController() {
		state = new MachineState();
	}

	@Override
	public boolean connect(SerialPort port) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void enqueue(String command) {}

	@Override
	public MachineState getMachineState() {
		return state;
	}

	@Override
	public void feedhold() {
		System.out.println("Feedhold");
	}

	@Override
	public void flush() {
		System.out.println("Flush");
	}

	@Override
	public void goToMachineZero(Axis axis) {
		System.out.println("Go to machine zero " + axis);
	}

	@Override
	public void goToZero(List<Axis> axes) {
		System.out.print("Go to zero ");
		axes.forEach(axis -> System.out.print("" + axis + " "));
		System.out.println();
	}

	@Override
	public void homeMachine(Axis axis) {
		System.out.println("Home machine " + axis);
	}

	@Override
	public void homeMachine(List<Axis> axes) {
		System.out.print("Home machine ");
		axes.forEach(axis -> System.out.print("" + axis + " "));
		System.out.println();
	}

	@Override
	public void jog(Axis axis, boolean positive) {
		System.out.println("Jog " + axis + (positive ? "+" : "-") + " " + getMachineState().getJogIncrement());
	}

	@Override
	public void killJob() {
		System.out.println("Kill job");
	}

	@Override
	public void resetLineCounter() {}

	@Override
	public void resume() {
		System.out.println("Resume");
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void zero(Axis axis) {
		System.out.println("Zero offset " + axis);
	}

	@Override
	public void zeroMachine(Axis axis) {
		System.out.println("Zero machine " + axis);
	}
}
