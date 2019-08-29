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
 * @since 2019
 */
public class SystemState {

	private BigDecimal firmwareBuild;

	private String firmwareBuildString;

	private String firmwareConfig;

	private BigDecimal firmwareVersion;

	private BigDecimal hardwareVersion;

	private PropertyChangeSupport pcs;

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
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

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
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
}
