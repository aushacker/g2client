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

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.github.aushacker.g2client.conn.IController;

/**
 * Provides UI access to the basic job control functions such as feedhold
 * and cycle start.
 *
 * @author Stephen Davies
 * @since March 2019
 */
public class ControlButtonPanel extends G2Panel {

	private static final long serialVersionUID = -8758366619253512200L;

	private JButton feedhold;
	private JButton flush;
	private JButton kill;
	private JButton resume;

	public ControlButtonPanel(IController controller, UIPreferences prefs) {
		super(new FlowLayout(), controller, prefs);
		
		createWidgets();
		layoutWidgets();
		initialiseEvents();
	}

	private void createWidgets() {
		feedhold = new JButton("Feedhold");
		flush = new JButton("Flush Q");
		kill = new JButton("Kill");
		resume = new JButton("Resume");
	}

	private void initialiseEvents() {
		feedhold.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				getController().feedhold();
			}
		});

		resume.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				getController().resume();
			}
		});

		flush.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				getController().flush();
			}
		});

		kill.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				getController().killJob();
			}
		});
	}

	private void layoutWidgets() {
		add(feedhold);
		add(resume);
		add(flush);
		add(kill);
	}
}
