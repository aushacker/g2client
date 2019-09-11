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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * @author Stephen Davies
 * @since March 2019
 */
public class Motor {

	/**
	 * Motor (groups) are numbered 1 to 6. The number of motors actually available
	 * depends on the hardware. 
	 */
	private SimpleIntegerProperty id;

	private SimpleIntegerProperty axis;

	private SimpleIntegerProperty microsteps;

	private SimpleIntegerProperty polarity;

	private SimpleDoubleProperty powerLevel;

	private SimpleIntegerProperty powerMode;

	private SimpleDoubleProperty stepAngle;

	private SimpleDoubleProperty stepsPerUnit;

	private SimpleDoubleProperty travelPerRev;

	public Motor(int id) {
		this.id = new SimpleIntegerProperty(id);
		this.axis = new SimpleIntegerProperty();
		this.microsteps = new SimpleIntegerProperty();
		this.polarity = new SimpleIntegerProperty();
		this.powerMode = new SimpleIntegerProperty();
		this.powerLevel = new SimpleDoubleProperty();
		this.stepAngle = new SimpleDoubleProperty();
		this.stepsPerUnit = new SimpleDoubleProperty();
		this.travelPerRev = new SimpleDoubleProperty();
	}

	public int getAxis() {
		return axis.get();
	}

	public IntegerProperty getAxisProperty() {
		return axis;
	}

	public int getId() {
		return id.get();
	}

	public IntegerProperty getIdProperty() {
		return id;
	}

	public int getMicrosteps() {
		return microsteps.get();
	}

	public IntegerProperty getMicrostepsProperty() {
		return microsteps;
	}

	public int getPolarity() {
		return polarity.get();
	}

	public IntegerProperty getPolarityProperty() {
		return polarity;
	}

	public double getPowerLevel() {
		return powerLevel.get();
	}

	public DoubleProperty getPowerLevelProperty() {
		return powerLevel;
	}

	public int getPowerMode() {
		return powerMode.get();
	}

	public IntegerProperty getPowerModeProperty() {
		return powerMode;
	}

	public double getStepAngle() {
		return stepAngle.get();
	}

	public DoubleProperty getStepAngleProperty() {
		return stepAngle;
	}

	public double getStepsPerUnit() {
		return stepsPerUnit.get();
	}

	public DoubleProperty getStepsPerUnitProperty() {
		return stepsPerUnit;
	}

	public double getTravelPerRev() {
		return travelPerRev.get();
	}

	public DoubleProperty getTravelPerRevProperty() {
		return travelPerRev;
	}

	public void setAxis(int axis) {
		this.axis.set(axis);
	}

	public void setMicrosteps(int microsteps) {
		this.microsteps.set(microsteps);
	}

	public void setPolarity(int polarity) {
		this.polarity.set(polarity);
	}

	public void setPowerLevel(double powerLevel) {
		this.powerLevel.set(powerLevel);
	}

	public void setPowerMode(int powerMode) {
		this.powerMode.set(powerMode);
	}

	public void setStepAngle(double stepAngle) {
		this.stepAngle.set(stepAngle);
	}

	public void setStepsPerUnit(double stepsPerUnit) {
		this.stepsPerUnit.set(stepsPerUnit);
	}

	public void setTravelPerRev(double travelPerRev) {
		this.travelPerRev.set(travelPerRev);
	}
}
