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
public class DigitalInput {

	private SimpleObjectProperty<Action> action;

	private SimpleObjectProperty<Function> function;

	private SimpleObjectProperty<Mode> mode;

	public Action getAction() {
		return action.get();
	}

	public ObjectProperty<Action> getActionProperty() {
		return action;
	}

	public Function getFunction() {
		return function.get();
	}

	public ObjectProperty<Function> getFunctionProperty() {
		return function;
	}

	public Mode getMode() {
		return mode.get();
	}

	public ObjectProperty<Mode> getModeProperty() {
		return mode;
	}

	public void setAction(Action a) {
		action.setValue(a);
	}

	public void setFunction(Function f) {
		function.setValue(f);
	}

	public void setMode(Mode m) {
		mode.setValue(m);
	}

	public enum Mode { 
		ACTIVE_LOW,
		ACTIVE_HIGH,
		DISABLED;
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
