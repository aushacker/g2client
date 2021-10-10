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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * A data holder that stores values for a single axis.
 *
 * @author Stephen Davies
 * @since September 2019
 */
public class AxisSettings {

    private Axis axis;

    /**
     * Sets maximum feed rate for the axis.
     */
    private SimpleDoubleProperty feedrate;

    /**
     * 0=search-towards-negative, 1=search-torwards-positive.
     */
    private SimpleObjectProperty<HomingDirection> homingDirection;

    /**
     * Switch (input) to use for homing this axis.
     */
    private SimpleIntegerProperty homingInput;

    /**
     * Jerk used during homing operations.
     */
    private SimpleDoubleProperty jerkHigh;

    /**
     * Main parameter for acceleration management.
     */
    private SimpleDoubleProperty jerkMax;

    /**
     * Maximum distance to back off switch during latch phase (drive off switch).
     */
    private SimpleDoubleProperty latchBackoff;

    /**
     * Homing speed during latch phase (drive off switch).
     */
    private SimpleDoubleProperty latchVelocity;

    /**
     * Normally {xam:1} "normal". See details for setting.
     */
    private SimpleObjectProperty<AxisMode> mode;

    /**
     * Artificial radius to convert linear values to degrees. ABC axes only.
     */
    private SimpleDoubleProperty radius;

    /**
     * Homing speed during search phase (drive to switch).
     */
    private SimpleDoubleProperty searchVelocity;

    /**
     * Minimum travel in absolute coordinates. Used by homing and soft limits.
     */
    private SimpleDoubleProperty travelMin;

    /**
     * Maximum travel in absolute coordinates. Used by homing and soft limits.
     */
    private SimpleDoubleProperty travelMax;

    /**
     * Max velocity for axis, aka "traverse rate" or "seek".
     */
    private SimpleDoubleProperty velocityMax;

    /**
     * Offset from switch for zero in absolute coordinates.
     */
    private SimpleDoubleProperty zeroBackoff;

    public AxisSettings(Axis axis) {
        this.axis = axis;

        feedrate = new SimpleDoubleProperty();
        homingDirection = new SimpleObjectProperty<>(HomingDirection.NEGATIVE);
        homingInput = new SimpleIntegerProperty();
        jerkHigh = new SimpleDoubleProperty();
        jerkMax = new SimpleDoubleProperty();
        latchBackoff= new SimpleDoubleProperty();
        latchVelocity= new SimpleDoubleProperty();
        mode = new SimpleObjectProperty<>(AxisMode.DISABLE);
        radius = new SimpleDoubleProperty();
        searchVelocity= new SimpleDoubleProperty();
        travelMin = new SimpleDoubleProperty();
        travelMax = new SimpleDoubleProperty();
        velocityMax = new SimpleDoubleProperty();
        zeroBackoff= new SimpleDoubleProperty();
    }

    public DoubleProperty feedrateProperty() {
        return feedrate;
    }

    public Axis getAxis() {
        return axis;
    }

    public double getFeedrate() {
        return feedrate.get();
    }

    public HomingDirection getHomingDirection() {
        return homingDirection.get();
    }

    public int getHomingInput() {
        return homingInput.get();
    }

    public double getJerkHigh() {
        return jerkHigh.get();
    }

    public double getJerkMax() {
        return jerkMax.get();
    }

    public double getLatchBackoff() {
        return latchBackoff.get();
    }

    public double getLatchVelocity() {
        return latchVelocity.get();
    }

    public AxisMode getMode() {
        return mode.get();
    }

    public double getRadius() {
        return radius.get();
    }

    public double getSearchVelocity() {
        return searchVelocity.get();
    }

    public double getTravelMax() {
        return travelMax.get();
    }

    public double getTravelMin() {
        return travelMin.get();
    }

    public double getVelocityMax() {
        return velocityMax.get();
    }

    public double getZeroBackoff() {
        return zeroBackoff.get();
    }

    public ObjectProperty<HomingDirection> homingDirectionProperty() {
        return homingDirection;
    }

    public IntegerProperty homingInputProperty() {
        return homingInput;
    }

    public DoubleProperty jerkHighProperty() {
        return jerkHigh;
    }

    public DoubleProperty latchBackoffProperty() {
        return latchBackoff;
    }

    public DoubleProperty latchVelocityProperty() {
        return latchVelocity;
    }

    public ObjectProperty<AxisMode> modeProperty() {
        return mode;
    }

    public DoubleProperty radiusProperty() {
        return radius;
    }

    public DoubleProperty searchVelocityProperty() {
        return searchVelocity;
    }

    public void setFeedrate(double feedrate) {
        this.feedrate.set(feedrate);
    }

    public void setHomingDirection(HomingDirection homingDirection) {
        this.homingDirection.set(homingDirection);
    }

    public void setHomingInput(int homingInput) {
        this.homingInput.set(homingInput);
    }

    public void setJerkHigh(double jerkHigh) {
        this.jerkHigh.set(jerkHigh);
    }

    public void setJerkMax(double jerkMax) {
        this.jerkMax.set(jerkMax);
    }

    public void setLatchBackoff(double latchBackoff) {
        this.latchBackoff.set(latchBackoff);
    }

    public void setLatchVelocity(double latchVelocity) {
        this.latchVelocity.set(latchVelocity);
    }

    public void setMode(AxisMode mode) {
        this.mode.set(mode);
    }

    public void setRadius(double radius) {
        this.radius.set(radius);
    }

    public void setSearchVelocity(double searchVelocity) {
        this.searchVelocity.set(searchVelocity);
    }

    public void setTravelMax(double travelMax) {
        this.travelMax.set(travelMax);
    }

    public void setTravelMin(double travelMin) {
        this.travelMin.set(travelMin);
    }

    public void setVelocityMax(double velocityMax) {
        this.velocityMax.set(velocityMax);
    }

    public void setZeroBackoff(double zeroBackoff) {
        this.zeroBackoff.set(zeroBackoff);
    }

    public DoubleProperty travelMaxProperty() {
        return travelMax;
    }

    public DoubleProperty travelMinProperty() {
        return travelMin;
    }

    public DoubleProperty velocityMaxProperty() {
        return velocityMax;
    }

    public DoubleProperty zeroBackoffProperty() {
        return zeroBackoff;
    }
}
