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
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 
 * @author Stephen Davies
 * @since August 2019
 */
public class SystemState {

	private DoubleProperty firmwareBuild;

	private StringProperty firmwareBuildString;

	private StringProperty firmwareConfig;

	private DoubleProperty firmwareVersion;

	private DoubleProperty hardwareVersion;

	public SystemState() {
		firmwareBuild = new SimpleDoubleProperty();
		firmwareBuildString = new SimpleStringProperty();
		firmwareConfig = new SimpleStringProperty();
		firmwareVersion = new SimpleDoubleProperty();
		hardwareVersion = new SimpleDoubleProperty();
	}

	public DoubleProperty firmwareBuildProperty() {
		return firmwareBuild;
	}

	public StringProperty firmwareBuildStringProperty() {
		return firmwareBuildString;
	}

	public StringProperty firmwareConfigProperty() {
		return firmwareConfig;
	}

	public DoubleProperty firmwareVersionProperty() {
		return firmwareVersion;
	}

	public double getFirmwareBuild() {
		return firmwareBuild.get();
	}

	public String getFirmwareBuildString() {
		return firmwareBuildString.get();
	}

	public String getFirmwareConfig() {
		return firmwareConfig.get();
	}

	public double getFirmwareVersion() {
		return firmwareVersion.get();
	}

	public double getHardwareVersion() {
		return hardwareVersion.get();
	}

	public DoubleProperty hardwareVersionProperty() {
		return hardwareVersion;
	}

	public void setFirmwareBuild(double fb) {
		firmwareBuild.set(fb);
	}

	public void setFirmwareBuildString(String fbs) {
		firmwareBuildString.set(fbs);
	}

	public void setFirmwareConfig(String fbc) {
		firmwareConfig.set(fbc);
	}

	public void setFirmwareVersion(double fv) {
		firmwareVersion.set(fv);
	}

	public void setHardwareVersion(double hv) {
		hardwareVersion.set(hv);
	}
}
