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
public class DigitalInputSettings {

	private SimpleObjectProperty<Action> action;

	private SimpleObjectProperty<Function> function;

	private SimpleObjectProperty<Mode> mode;

	public DigitalInputSettings() {
		this.action = new SimpleObjectProperty<>();
		this.function = new SimpleObjectProperty<>();
		this.mode = new SimpleObjectProperty<>();
	}

	public ObjectProperty<Action> actionProperty() {
		return action;
	}

	public ObjectProperty<Function> functionProperty() {
		return function;
	}

	public Action getAction() {
		return action.get();
	}

	public Function getFunction() {
		return function.get();
	}

	public Mode getMode() {
		return mode.get();
	}

	public ObjectProperty<Mode> modeProperty() {
		return mode;
	}

	public void setAction(Action a) {
		action.set(a);
	}

	public void setFunction(Function f) {
		function.set(f);
	}

	public void setMode(Mode m) {
		mode.set(m);
	}

	public enum Action {
		NONE,
		STOP,
		FAST_STOP,
		HALT,
		CYCLE_START,
		ALARM,
		SHUTDOWN,
		PANIC,
		RESET;
	}

	public enum Function {
		NONE,
		LIMIT,
		INTERLOCK,
		SHUTDOWN,
		PROBE;
	}
}
