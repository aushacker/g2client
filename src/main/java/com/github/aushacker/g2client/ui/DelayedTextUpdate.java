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

import javax.swing.text.JTextComponent;

/**
 * Update a JTextComponent 'the right way', using the Swing UI thread.
 *
 * @author Stephen Davies
 * @since March 2019
 */
public class DelayedTextUpdate implements Runnable {

	private JTextComponent c;
	private String value;

	public DelayedTextUpdate(JTextComponent c, String value) {
		this.c = c;
		this.value = value;
	}

	@Override
	public void run() {
		c.setText(value);
	}
}
