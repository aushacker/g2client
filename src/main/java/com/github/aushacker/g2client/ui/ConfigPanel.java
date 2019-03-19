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

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import com.fazecast.jSerialComm.SerialPort;
import com.github.aushacker.g2client.conn.IController;

import com.github.aushacker.g2client.conn.OperatingSystem;

/**
 * Top-level configuration panel.
 *
 * @author Stephen Davies
 * @since March 2019
 */
public class ConfigPanel extends G2Panel {

	private static final long serialVersionUID = 1108114237450732892L;

	private JTextField initialScriptField;

	private JTextField scriptHomeField;

	private JComboBox<SerialPort> portChoice = new JComboBox<>();

	public ConfigPanel(IController controller, UIPreferences prefs) {
		super(new GridBagLayout(), controller, prefs);

		initialScriptField = new JTextField(prefs.getInitialScript(), 40);
		scriptHomeField = new JTextField(prefs.getScriptHome(), 40);
		populatePortChoice();

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(new JLabel("Script Home"), gbc);

		add(Box.createHorizontalStrut(10));
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(scriptHomeField, gbc);

		gbc.gridwidth = 1;
		add(new JLabel("Serial Port"), gbc);

		add(Box.createHorizontalStrut(10));
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.NONE;
		add(portChoice, gbc);

		gbc.gridwidth = 1;
		add(new JLabel("Initial Script"), gbc);

		add(Box.createHorizontalStrut(10));
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(initialScriptField, gbc);

		initializeHandlers();
	}
	
	private void initializeHandlers() {
		initialScriptField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				getPrefs().setInitialScript(initialScriptField.getText());
			}
		});

		scriptHomeField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				getPrefs().setScriptHome(scriptHomeField.getText());
			}
		});

		portChoice.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					SerialPort port = (SerialPort) e.getItem();
					getPrefs().setPortName(port.getSystemPortName());
				} else {
					getPrefs().setPortName("");
				}
			}
		});
	}

	/**
	 * Populate the serial port combo box with the available serial ports.
	 */
	private void populatePortChoice() {
		for (SerialPort port : OperatingSystem.current().getFilteredPorts()) {
			portChoice.addItem(port);
		}

		// TODO - need to select currently preferred port if it is attached.

		portChoice.setRenderer(new SerialPortRenderer());
	}

	/**
	 * Make SerialPort instances more text friendly in a combo box.
	 */
	static class SerialPortRenderer extends JLabel implements ListCellRenderer<SerialPort> {

		private static final long serialVersionUID = -5834794067115003178L;

		private Border redBorder = BorderFactory.createLineBorder(Color.RED, 2);
		private Border emptyBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);

		@Override
		public Component getListCellRendererComponent(JList<? extends SerialPort> list,
				SerialPort port,
				int index,
				boolean isSelected,
				boolean cellHasFocus) {

			setText(port.getSystemPortName() + " (" + port.getDescriptivePortName() + ")");

			if (isSelected) {
				setBorder(redBorder);
			} else {
				setBorder(emptyBorder);
			}

			return this;
		}
	}
}
