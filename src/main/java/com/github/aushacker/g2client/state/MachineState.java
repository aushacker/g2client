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
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * A big, heavyweight object that contains all of the G2 controller state.
 *
 * @author Stephen Davies
 * @since March 2019
 */
public class MachineState {

    public static final int AXIS_COUNT = 9;

    public static final int DINPUT_COUNT = 9;

    public static final int DOUTPUT_COUNT = 9;

    public static final int MOTOR_COUNT = 6;

    private Map<Axis,AxisSettings> axisSettings;
    
    private SimpleObjectProperty<CoordinateSystem> coordinateSystem;

    /**
     * g2 uses 1 based indexing but code using 0 based.
     */
    private DigitalInputSettings[] digitalInputs;

    /**
     * g2 uses 1 based indexing but code using 0 based.
     */
    private DigitalOutputSettings[] digitalOutputs;

    private SimpleObjectProperty<DistanceMode> distanceMode;

    private SimpleObjectProperty<DynamicState> dynamicState;

    private SimpleIntegerProperty feedRate;

    private int jogIndex;

    private SimpleIntegerProperty line;

    private SimpleObjectProperty<MotionMode> motionMode;

    /**
     * g2 allows for 6 motors but the number present depends on the actual 
     * controller build.
     * <p>
     * g2 uses 1 based indexing but code using 0 based.
     */
    private Motor[] motors;

    private Map<CoordinateSystem,Offsets> offsets;

    private SystemState systemState;

    private SimpleObjectProperty<Unit> units;

    private SimpleObjectProperty<BigDecimal> velocity;

    private SimpleObjectProperty<BigDecimal> x;

    private SimpleObjectProperty<BigDecimal> y;

    private SimpleObjectProperty<BigDecimal> z;

    public MachineState() {
        axisSettings = new HashMap<>();
        for (Axis a : Axis.values()) {
            axisSettings.put(a, new AxisSettings(a));
        }

        coordinateSystem = new SimpleObjectProperty<>(CoordinateSystem.G54);

        digitalInputs = new DigitalInputSettings[DINPUT_COUNT];
        for (int i = 0; i < DINPUT_COUNT; i++) {
            digitalInputs[i] = new DigitalInputSettings();
        }

        digitalOutputs = new DigitalOutputSettings[DOUTPUT_COUNT];
        for (int i = 0; i < DOUTPUT_COUNT; i++) {
            digitalOutputs[i] = new DigitalOutputSettings();
        }

        distanceMode = new SimpleObjectProperty<>(DistanceMode.ABSOLUTE);
        dynamicState = new SimpleObjectProperty<>(DynamicState.INITIALIZING);
        feedRate = new SimpleIntegerProperty();
        line = new SimpleIntegerProperty();
        motionMode = new SimpleObjectProperty<>(MotionMode.TRAVERSE);

        motors = new Motor[MOTOR_COUNT];
        for (int i = 0; i < MOTOR_COUNT; i++) {
            motors[i] = new Motor(i + 1);
        }

        offsets = new HashMap<>();
        for (CoordinateSystem cs : CoordinateSystem.values()) {
            offsets.put(cs, new Offsets(cs));
        }

        systemState = new SystemState();
        units = new SimpleObjectProperty<>(Unit.MM);
        jogIndex = getUnits().getDefaultIndex();
        velocity = createBigDecimalWrapper(0);
        
        x = createBigDecimalWrapper(0);
        y = createBigDecimalWrapper(0);
        z = createBigDecimalWrapper(0);
    }

    public SimpleObjectProperty<CoordinateSystem> coordinateSystemProperty() {
        return coordinateSystem;
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
        if (next > getUnits().getMaxIndex())
            next = 0;
        
        setJogIndex(next);
    }

    public ObjectProperty<DistanceMode> distanceModeProperty() {
        return distanceMode;
    }

    public ObjectProperty<DynamicState> dynamicStateProperty() {
        return dynamicState;
    }

    public IntegerProperty feedRateProperty() {
        return feedRate;
    }

    public AxisSettings lookupSettings(Axis axis) {
        return axisSettings.get(axis);
    }

    public CoordinateSystem getCoordinateSystem() {
        return coordinateSystem.get();
    }

    public DistanceMode getDistanceMode() {
        return distanceMode.get();
    }

    public DigitalInputSettings getDigitalInput(int index) {
        return digitalInputs[index];
    }

    
    public DigitalOutputSettings getDigitalOutput(int index) {
        return digitalOutputs[index];
    }

    public DynamicState getDynamicState() {
        return dynamicState.get();
    }

    public int getFeedRate() {
        return feedRate.get();
    }

    public double getJogIncrement() {
        return getUnits().getIncrement(jogIndex);
    }

    public int getLine() {
        return line.get();
    }

    public MotionMode getMotionMode() {
        return motionMode.get();
    }

    public Motor[] getMotors() {
        return motors;
    }

    public Motor getMotors(int index) {
        return motors[index];
    }

    public Offsets getOffsets(CoordinateSystem cs) {
        return offsets.get(cs);
    }

    public SystemState getSystemState() {
        return systemState;
    }

    public Unit getUnits() {
        return units.get();
    }

    public BigDecimal getVelocity() {
        return velocity.get();
    }

    public BigDecimal getX() {
        return x.get();
    }

    public BigDecimal getY() {
        return y.get();
    }

    public BigDecimal getZ() {
        return z.get();
    }

    public IntegerProperty lineProperty() {
        return line;
    }

    public ObjectProperty<MotionMode> motionModeProperty() {
        return motionMode;
    }

    public void setCoordinateSystem(CoordinateSystem coordinateSystem) {
        this.coordinateSystem.set(coordinateSystem);
    }

    public void setDistanceMode(DistanceMode distanceMode) {
        this.distanceMode.set(distanceMode);
    }

    public void setDynamicState(DynamicState status) {
        this.dynamicState.set(status);
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

    public void setMotionMode(MotionMode motionMode) {
        this.motionMode.set(motionMode);
    }

    public void setUnits(Unit units) {
        this.units.set(units);

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

    public ObjectProperty<Unit> unitsProperty() {
        return units;
    }

    public ObjectProperty<BigDecimal> velocityProperty() {
        return velocity;
    }

    public ObjectProperty<BigDecimal> xProperty() {
        return x;
    }

    public ObjectProperty<BigDecimal> yProperty() {
        return y;
    }

    public ObjectProperty<BigDecimal> zProperty() {
        return z;
    }
}
