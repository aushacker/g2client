/*
 * Copyright 2018 Stephen Davies
 *
 * This file is part of Eagle2nc.
 *
 * Eagle2nc is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Eagle2nc is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Eagle2nc. If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.aushacker.g2client.state;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Stephen Davies
 * @since October 2018
 */
public class DigitalOutputSettings {

	private SimpleObjectProperty<Mode> mode;

	public DigitalOutputSettings() {
		this.mode = new SimpleObjectProperty<>();
	}

	public Mode getMode() {
		return mode.get();
	}

	public ObjectProperty<Mode> modeProperty() {
		return mode;
	}


	public void setMode(Mode m) {
		mode.set(m);
	}
}
