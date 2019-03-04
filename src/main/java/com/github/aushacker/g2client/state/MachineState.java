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
package com.github.aushacker.g2client.state;

/**
 * @author Stephen Davies
 * @since March 2019
 */
public class MachineState {

	public static final int MOTOR_COUNT = 6;

	private String firmwareBuild;

	private String firmwareVersion;

	/**
	 * g2 allows for 6 motors but the number present depends on the actual 
	 * controller build.
	 * <p>
	 * g2 uses 1 based indexing i.e. motors 1 to 6 but code used 0 based.
	 */
	private Motor[] motors;

	public MachineState() {
		motors = new Motor[MOTOR_COUNT];
		for (int i = 0; i < MOTOR_COUNT; i++) {
			motors[i] = new Motor(i + 1);
		}
	}

	public String getFirmwareBuild() {
		return firmwareBuild;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public Motor getMotors(int index) {
		return motors[index];
	}

	public void setFirmwareBuild(String fb) {
		firmwareBuild = fb;
	}

	public void setFirmwareVersion(String fv) {
		firmwareVersion = fv;
	}
}
