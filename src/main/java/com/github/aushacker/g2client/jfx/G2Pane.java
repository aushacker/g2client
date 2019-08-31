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

package com.github.aushacker.g2client.jfx;

import com.github.aushacker.g2client.conn.IController;
import com.github.aushacker.g2client.state.MachineState;
import com.github.aushacker.g2client.ui.UIPreferences;

/**
 * Common attributes shared by all panes in the application.
 *
 * @author Stephen Davies
 * @since August 2019
 */
public abstract class G2Pane {

	private G2Client top;
	
	public G2Pane(G2Client top) {
		this.top = top;
	}

	/**
	 * Convenience method.
	 */
	protected IController getController() {
		return top.getController();
	}

	/**
	 * Convenience method.
	 */
	protected MachineState getMachineState() {
		return top.getController().getMachineState();
	}

	/**
	 * Convenience method.
	 */
	protected UIPreferences getPrefs() {
		return top.getUIPreferences();
	}
}
