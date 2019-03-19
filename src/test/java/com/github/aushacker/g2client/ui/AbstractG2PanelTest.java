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

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.github.aushacker.g2client.conn.IController;
import com.github.aushacker.g2client.conn.MockController;

/**
 * Testing tool for UI layout.
 *
 * @author Stephen Davies
 * @since March 2019
 */
public abstract class AbstractG2PanelTest {

	public AbstractG2PanelTest() {
		JFrame f = new JFrame();
		Container contentPane = f.getContentPane();

		contentPane.add(createPanel(new MockController(), new UIPreferences()), BorderLayout.CENTER);

		f.setBounds(100, 100, 400, 300);
		f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		f.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});

		f.setVisible(true);
	}

	protected abstract G2Panel createPanel(IController controller, UIPreferences prefs);
}
