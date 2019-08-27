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
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.github.aushacker.g2client.conn.IController;

/**
 * Top-level diagnostics panel.
 *
 * @author Stephen Davies
 * @since August 2019
 */
public class DiagnosticsPanel extends G2Panel implements PropertyChangeListener {

	private static final long serialVersionUID = 4185064464983069076L;

	private JTextField firmwareVersionField;

	private JTextField firmwareBuildField;

	private JTextField firmwareBuildStringField;

	private JTextField firmwareConfigField;

	private JTextField hardwareVersionField;

	public DiagnosticsPanel(IController controller, UIPreferences prefs) {
		super(new GridBagLayout(), controller, prefs);

		firmwareVersionField = createTextField();
		firmwareBuildField = createTextField();
		firmwareBuildStringField = createTextField();
		firmwareConfigField = createTextField();
		hardwareVersionField = createTextField();

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(new JLabel("Firmware Version"), gbc);

		add(Box.createHorizontalStrut(10));
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(firmwareVersionField, gbc);

		appendField(gbc, "Firmware Build", firmwareBuildField);
		appendField(gbc, "Firmware Build String", firmwareBuildStringField);
		appendField(gbc, "Firmware Config", firmwareConfigField);
		appendField(gbc, "Hardware Version", hardwareVersionField);
		
		getMachineState().addPropertyChangeListener(this);
	}

	private void appendField(GridBagConstraints gbc, String label, JTextField tf) {
		gbc.gridwidth = 1;
		add(new JLabel(label), gbc);

		add(Box.createHorizontalStrut(10));
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(tf, gbc);
	}

	private JTextField createTextField() {
		JTextField tf = new JTextField("", 40);
		tf.setEditable(false);
		
		return tf;
	}

	public void propertyChange(PropertyChangeEvent e) {
		switch (e.getPropertyName()) {
		case "firmwareVersion":
			firmwareVersionField.setText(e.getNewValue().toString());
			break;
		case "firmwareBuild":
			firmwareBuildField.setText(e.getNewValue().toString());
			break;
		case "firmwareBuildString":
			firmwareBuildStringField.setText((String)e.getNewValue());
			break;
		case "firmwareConfig":
			firmwareConfigField.setText((String)e.getNewValue());
			break;
		case "hardwareVersion":
			hardwareVersionField.setText(e.getNewValue().toString());
			break;
		}
	}
}
