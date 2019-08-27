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

import java.math.BigDecimal;

/**
 * @author Stephen Davies
 * @since March 2019
 */
public class Motor {

	/**
	 * Motor (groups) are numbered 1 to 6. The number of motors actually available
	 * depends on the hardware. 
	 */
	private int id;

	private int axis;

	private int microsteps;

	private int polarity;

	private double powerLevel;

	private int powerMode;

	private BigDecimal stepAngle;

	private double stepsPerUnit;

	private BigDecimal travelPerRev;

	public Motor(int id) {
		this.id = id;
	}

	public int getAxis() {
		return axis;
	}

	public int getId() {
		return id;
	}

	public int getMicrosteps() {
		return microsteps;
	}

	public int getPolarity() {
		return polarity;
	}

	public double getPowerLevel() {
		return powerLevel;
	}

	public int getPowerMode() {
		return powerMode;
	}

	public BigDecimal getStepAngle() {
		return stepAngle;
	}

	public double getStepsPerUnit() {
		return stepsPerUnit;
	}

	public BigDecimal getTravelPerRev() {
		return travelPerRev;
	}

	public void setAxis(int axis) {
		this.axis = axis;
	}

	public void setMicrosteps(int microsteps) {
		this.microsteps = microsteps;
	}

	public void setPolarity(int polarity) {
		this.polarity = polarity;
	}

	public void setPowerLevel(double powerLevel) {
		this.powerLevel = powerLevel;
	}

	public void setPowerMode(int powerMode) {
		this.powerMode = powerMode;
	}

	public void setStepAngle(BigDecimal stepAngle) {
		this.stepAngle = stepAngle;
	}

	public void setStepsPerUnit(double stepsPerUnit) {
		this.stepsPerUnit = stepsPerUnit;
	}

	public void setTravelPerRev(BigDecimal travelPerRev) {
		this.travelPerRev = travelPerRev;
	}
}
