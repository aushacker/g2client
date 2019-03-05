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
package com.github.aushacker.g2client.ui;

import java.awt.LayoutManager;

import javax.swing.JPanel;

import com.github.aushacker.g2client.conn.MachineController;
import com.github.aushacker.g2client.state.MachineState;

/**
 * Common attributes shared by all panels.
 *
 * @author Stephen Davies
 * @since March 2019
 */
public abstract class G2Panel extends JPanel {

	private static final long serialVersionUID = 8901180446217631821L;

	private MachineController controller;

	private UIPreferences prefs;

	public G2Panel(LayoutManager layoutManager, MachineController controller, UIPreferences prefs) {
		super(layoutManager);
		
		this.controller = controller;
		this.prefs = prefs;
	}

	protected MachineController getController() {
		return controller;
	}

	/**
	 * Convenience method.
	 */
	protected MachineState getMachineState() {
		return getController().getMachineState();
	}

	protected UIPreferences getPrefs() {
		return prefs;
	}
}
