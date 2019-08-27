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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import com.github.aushacker.g2client.conn.IController;

/**
 * Controls visual component spacing in the UI. Controller operations are delegated
 * to the nested panels.
 *
 * @author Stephen Davies
 * @since March 2019
 */
public class ControlPanel extends G2Panel {

	private static final long serialVersionUID = 4195185461115800021L;

	public ControlPanel(IController controller, UIPreferences prefs) {
		super(new GridBagLayout(), controller, prefs);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(new AxesPanel(controller, prefs), c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		add(new ControlButtonPanel(controller, prefs), c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new StatusPanel(controller, prefs), c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.weighty = 1.0;
		add(new JPanel(), c);
	}
}
