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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.github.aushacker.g2client.conn.MachineController;
import com.github.aushacker.g2client.protocol.StatValue;

/**
 * Displays status information from the g2 controller.
 *
 * @author Stephen Davies
 * @since March 2019
 */
public class StatusPanel extends G2Panel implements PropertyChangeListener {

	private static final int WIDTH = 10;

	private JTextField tfFeedRate;

	private JTextField tfLine;

	private JTextField tfStatus;

	private JTextField tfVelocity;

	public StatusPanel(MachineController controller, UIPreferences prefs) {
		super(new GridBagLayout(), controller, prefs);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(new JLabel("Velocity"), c);

		tfVelocity = new JTextField(format(getMachineState().getVelocity()), WIDTH);
		tfVelocity.setEditable(false);
		tfVelocity.setHorizontalAlignment(JTextField.RIGHT);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.5;
		add(tfVelocity, c);
		
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 0;
		add(new JLabel("Feed Rate"), c);
		
		tfFeedRate = new JTextField(Integer.toString(getMachineState().getFeedRate()), WIDTH);
		tfFeedRate.setEditable(false);
		tfFeedRate.setHorizontalAlignment(JTextField.RIGHT);
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 0;
		c.weightx = 0.5;
		add(tfFeedRate, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		add(new JLabel("Line"), c);
		
		tfLine = new JTextField(Integer.toString(getMachineState().getLine()), WIDTH);
		tfLine.setEditable(false);
		tfLine.setHorizontalAlignment(JTextField.RIGHT);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0.5;
		add(tfLine, c);
		
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		add(new JLabel("Status"), c);
		
		tfStatus = new JTextField(StatValue.lookupId(getMachineState().getStatus()).getDescription(), WIDTH);
		tfStatus.setEditable(false);
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 1;
		c.weightx = 0.5;
		add(tfStatus, c);
		
		getMachineState().addPropertyChangeListener(this);
	}

	private String format(BigDecimal value) {
		DecimalFormat df = new DecimalFormat("#,##0.00");
		return df.format(value);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String value = null;
		JTextField field;
		switch (evt.getPropertyName()) {
		case "velocity":
			field = tfVelocity;
			value = format((BigDecimal) evt.getNewValue());
			break;
		case "feedRate":
			field = tfFeedRate;
			value = evt.getNewValue().toString();
			break;
		case "line":
			field = tfLine;
			value = evt.getNewValue().toString();
			break;
		case "status":
			field = tfStatus;
			Integer id = (Integer) evt.getNewValue();
			value = StatValue.lookupId(id).getDescription();
			break;
		default:
			// Nothing we're interested in, bail early
			return;
		}
		
		SwingUtilities.invokeLater(new DelayedUpdate(field, value));
	}

	private class DelayedUpdate implements Runnable {
		JTextField field;
		String value;
		
		DelayedUpdate(JTextField field, String value) {
			this.field = field;
			this.value = value;
		}
		
		public void run() {
			field.setText(value);
		}
	}
}
