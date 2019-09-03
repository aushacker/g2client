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

	private BigDecimal firmwareBuild;

	private String firmwareBuildString;

	private String firmwareConfig;

	private BigDecimal firmwareVersion;

	private BigDecimal hardwareVersion;

	private int jogIndex;

	private int line;

	/**
	 * g2 allows for 6 motors but the number present depends on the actual 
	 * controller build.
	 * <p>
	 * g2 uses 1 based indexing i.e. motors 1 to 6 but code using 0 based.
	 */
	private Motor[] motors;

	private PropertyChangeSupport pcs;

	private int status;

	private SystemState systemState;

	private Unit units;

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

		units = Unit.MM;
		jogIndex = units.getDefaultIndex();

		systemState = new SystemState();
		velocity = new BigDecimal(0);
		x = new BigDecimal(0);
		y = new BigDecimal(0);
		z = new BigDecimal(0);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void cycleJogIncrement() {
		int next = jogIndex + 1;
		if (next > units.getMaxIndex())
			next = 0;
		
		setJogIndex(next);
	}

	public int getFeedRate() {
		return feedRate;
	}

	public BigDecimal getFirmwareBuild() {
		return firmwareBuild;
	}

	public String getFirmwareBuildString() {
		return firmwareBuildString;
	}

	public String getFirmwareConfig() {
		return firmwareConfig;
	}

	public BigDecimal getFirmwareVersion() {
		return firmwareVersion;
	}

	public BigDecimal getHardwareVersion() {
		return hardwareVersion;
	}

	public double getJogIncrement() {
		return units.getIncrement(jogIndex);
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

	public SystemState getSystemState() {
		return systemState;
	}

	public Unit getUnits() {
		return units;
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

	public void setFirmwareBuild(BigDecimal fb) {
		BigDecimal old = this.firmwareBuild;
		firmwareBuild = fb;

		pcs.firePropertyChange("firmwareBuild", old, firmwareBuild);
	}

	public void setFirmwareBuildString(String fbs) {
		String old = this.firmwareBuildString;
		firmwareBuildString = fbs;

		pcs.firePropertyChange("firmwareBuildString", old, firmwareBuildString);
	}

	public void setFirmwareConfig(String fbc) {
		String old = this.firmwareConfig;
		firmwareConfig = fbc;

		pcs.firePropertyChange("firmwareConfig", old, firmwareConfig);
	}

	public void setFirmwareVersion(BigDecimal fv) {
		BigDecimal old = this.firmwareVersion;
		firmwareVersion = fv;

		pcs.firePropertyChange("firmwareVersion", old, firmwareVersion);
	}

	public void setHardwareVersion(BigDecimal hv) {
		BigDecimal old = hardwareVersion;
		hardwareVersion = hv;

		pcs.firePropertyChange("hardwareVersion", old, hardwareVersion);
	}

	public void setJogIndex(int jogIndex) {
		int oldIndex = this.jogIndex;
		this.jogIndex = jogIndex;

		pcs.firePropertyChange("jogIndex", oldIndex, jogIndex);
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

	public void setUnits(Unit units) {
		Unit old = this.units;
		this.units = units;
		pcs.firePropertyChange("units", old, units);

		setJogIndex(units.getDefaultIndex());
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
