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

import com.github.aushacker.g2client.protocol.StatValue;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Stephen Davies
 * @since March 2019
 */
public class MachineState {

	public static final int DINPUT_COUNT = 10;

	public static final int DOUTPUT_COUNT = 10;

	public static final int MOTOR_COUNT = 6;

	private SimpleIntegerProperty feedRate;

	private int jogIndex;

	private SimpleIntegerProperty line;

	/**
	 * g2 uses 1 based indexing i.e. inputs 1 to 10 but code using 0 based.
	 */
	private DigitalInput[] digitalInputs;

	/**
	 * g2 allows for 6 motors but the number present depends on the actual 
	 * controller build.
	 * <p>
	 * g2 uses 1 based indexing i.e. motors 1 to 6 but code using 0 based.
	 */
	private Motor[] motors;

	private SimpleObjectProperty<StatValue> status;

	private SystemState systemState;

	private Unit units;

	private SimpleObjectProperty<BigDecimal> velocity;

	private SimpleObjectProperty<BigDecimal> x;

	private SimpleObjectProperty<BigDecimal> y;

	private SimpleObjectProperty<BigDecimal> z;

	public MachineState() {
		digitalInputs = new DigitalInput[DINPUT_COUNT];
		for (int i = 0; i < DINPUT_COUNT; i++) {
			digitalInputs[i] = new DigitalInput();
		}

		feedRate = new SimpleIntegerProperty();
		line = new SimpleIntegerProperty();

		motors = new Motor[MOTOR_COUNT];
		for (int i = 0; i < MOTOR_COUNT; i++) {
			motors[i] = new Motor(i + 1);
		}

		units = Unit.MM;
		jogIndex = units.getDefaultIndex();
		status = new SimpleObjectProperty<>(StatValue.INITIALIZING);
		systemState = new SystemState();
		velocity = createBigDecimalWrapper(0);
		
		x = createBigDecimalWrapper(0);
		y = createBigDecimalWrapper(0);
		z = createBigDecimalWrapper(0);
	}

	/**
	 * Helper for JavaFX bound properties.
	 */
	private SimpleObjectProperty<BigDecimal> createBigDecimalWrapper(int val) {
		SimpleObjectProperty<BigDecimal> wrapper = new SimpleObjectProperty<>();
		wrapper.set(new BigDecimal(val));
		return wrapper;
	}

	public void cycleJogIncrement() {
		int next = jogIndex + 1;
		if (next > units.getMaxIndex())
			next = 0;
		
		setJogIndex(next);
	}

	public IntegerProperty feedRateProperty() {
		return feedRate;
	}

	public DigitalInput getDigitalInput(int index) {
		return digitalInputs[index];
	}

	public int getFeedRate() {
		return feedRate.get();
	}

	public double getJogIncrement() {
		return units.getIncrement(jogIndex);
	}

	public int getLine() {
		return line.get();
	}

	public Motor[] getMotors() {
		return motors;
	}

	public Motor getMotors(int index) {
		return motors[index];
	}

	public StatValue getStatus() {
		return status.get();
	}

	public SystemState getSystemState() {
		return systemState;
	}

	public Unit getUnits() {
		return units;
	}

	public BigDecimal getVelocity() {
		return velocity.get();
	}

	public BigDecimal getX() {
		return x.get();
	}

	public ObjectProperty<BigDecimal> getXProperty() {
		return x;
	}

	public BigDecimal getY() {
		return y.get();
	}

	public ObjectProperty<BigDecimal> getYProperty() {
		return y;
	}

	public BigDecimal getZ() {
		return z.get();
	}

	public ObjectProperty<BigDecimal> getZProperty() {
		return z;
	}

	public IntegerProperty lineProperty() {
		return line;
	}

	public void setFeedRate(int feedRate) {
		this.feedRate.set(feedRate);
	}

	public void setJogIndex(int jogIndex) {
		int oldIndex = this.jogIndex;
		this.jogIndex = jogIndex;

		//TODO pcs.firePropertyChange("jogIndex", oldIndex, jogIndex);
	}

	public void setLine(int line) {
		this.line.set(line);
	}

	public void setStatus(StatValue status) {
		this.status.set(status);
	}

	public void setUnits(Unit units) {
		Unit old = this.units;
		this.units = units;
		//TODO pcs.firePropertyChange("units", old, units);

		setJogIndex(units.getDefaultIndex());
	}

	public void setVelocity(BigDecimal velocity) {
		this.velocity.set(velocity);
	}

	public void setX(BigDecimal x) {
		this.x.set(x);
	}

	public void setY(BigDecimal y) {
		this.y.set(y);
	}

	public void setZ(BigDecimal z) {
		this.z.set(z);
	}

	public ObjectProperty<StatValue> statusProperty() {
		return status;
	}

	public ObjectProperty<BigDecimal> velocityProperty() {
		return velocity;
	}
}
