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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;

/**
 * @author Stephen Davies
 * @since March 2019
 */
public class MachineState {

	public static final int DINPUT_COUNT = 9;

	public static final int DOUTPUT_COUNT = 9;

	public static final int MOTOR_COUNT = 6;

	private int feedRate;

	private String firmwareBuild;

	private String firmwareVersion;

	private int line;

	/**
	 * g2 allows for 6 motors but the number present depends on the actual 
	 * controller build.
	 * <p>
	 * g2 uses 1 based indexing i.e. motors 1 to 6 but code used 0 based.
	 */
	private Motor[] motors;

	private PropertyChangeSupport pcs;

	private int status;

	private BigDecimal velocity;

	private BigDecimal x;

	private BigDecimal y;

	private BigDecimal z;

	public MachineState() {
		motors = new Motor[MOTOR_COUNT];
		for (int i = 0; i < MOTOR_COUNT; i++) {
			motors[i] = new Motor(i + 1);
		}

		pcs = new PropertyChangeSupport(this);

		velocity = new BigDecimal(0);
		x = new BigDecimal(0);
		y = new BigDecimal(0);
		z = new BigDecimal(0);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public int getFeedRate() {
		return feedRate;
	}

	public String getFirmwareBuild() {
		return firmwareBuild;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public int getLine() {
		return line;
	}

	public Motor getMotors(int index) {
		return motors[index];
	}

	public int getStatus() {
		return status;
	}

	public BigDecimal getVelocity() {
		return velocity;
	}

	public BigDecimal getX() {
		return x;
	}

	public BigDecimal getY() {
		return y;
	}

	public BigDecimal getZ() {
		return z;
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	public void setFeedRate(int feedRate) {
		int old = this.feedRate;
		this.feedRate = feedRate;

		pcs.firePropertyChange("feedRate", old, feedRate);
	}

	public void setFirmwareBuild(String fb) {
		firmwareBuild = fb;
	}

	public void setFirmwareVersion(String fv) {
		firmwareVersion = fv;
	}

	public void setLine(int line) {
		int old = this.line;
		this.line = line;

		pcs.firePropertyChange("line", old, line);
	}

	public void setStatus(int status) {
		int old = this.status;
		this.status = status;
		
		pcs.firePropertyChange("status", old, status);
	}

	public void setVelocity(BigDecimal velocity) {
		BigDecimal old = this.velocity;
		this.velocity = velocity;
		
		pcs.firePropertyChange("velocity", old, velocity);
	}

	public void setX(BigDecimal x) {
		BigDecimal old = this.x;
		this.x = x;
		
		pcs.firePropertyChange("x", old, x);
	}

	public void setY(BigDecimal y) {
		BigDecimal old = this.y;
		this.y = y;
		
		pcs.firePropertyChange("y", old, y);
	}

	public void setZ(BigDecimal z) {
		BigDecimal old = this.z;
		this.z = z;
		
		pcs.firePropertyChange("z", old, z);
	}
}
