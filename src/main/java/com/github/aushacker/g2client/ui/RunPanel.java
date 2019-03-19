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

import java.awt.BorderLayout;
import java.io.File;

import com.github.aushacker.g2client.conn.IController;

/**
 * @author Stephen Davies
 * @since March 2019
 */
public class RunPanel extends G2Panel {

	private static final long serialVersionUID = 4447900800636040730L;

	private GCodePanel codePanel;
	
	private ControlPanel controlPanel;
	
	public RunPanel(IController controller, UIPreferences prefs) {
		super(new BorderLayout(), controller, prefs);

		createWidgets();
		layoutWidgets();
	}

	private void layoutWidgets() {
		add(codePanel, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.EAST);
	}

	private void createWidgets() {
		codePanel = new GCodePanel(getController(), getPrefs());
		controlPanel = new ControlPanel(getController(), getPrefs());
	}

	public void openFile(File file) {
		codePanel.openFile(file);
	}
}
