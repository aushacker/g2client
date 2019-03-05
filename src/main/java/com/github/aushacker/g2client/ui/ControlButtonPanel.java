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

import com.github.aushacker.g2client.conn.MachineController;

/**
 * Provides UI access to the basic job control functions such as feedhold
 * and cycle start.
 *
 * @author Stephen Davies
 * @since March 2019
 */
public class ControlButtonPanel extends G2Panel {

	private static final long serialVersionUID = -8758366619253512200L;

	public ControlButtonPanel(MachineController controller, UIPreferences prefs) {
		super(new FlowLayout(), controller, prefs);
		
		JButton btFeedhold = new JButton("Feedhold");
		btFeedhold.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				controller.feedhold();
			}
		});

		JButton btResume = new JButton("Resume");
		btResume.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				controller.resume();
			}
		});

		JButton btFlush = new JButton("Flush Q");
		btFlush.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				controller.flush();
			}
		});

		JButton btKill = new JButton("Kill");
		btKill.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				controller.killJob();
			}
		});

		add(btFeedhold);
		add(btResume);
		add(btFlush);
		add(btKill);
	}
}
