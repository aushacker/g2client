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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.github.aushacker.g2client.conn.MachineController;
import com.github.aushacker.g2client.state.Axis;

/**
 * Displays the X & Y axis information.
 *
 * @author Stephen Davies
 * @since March 2019
 */
public class AxesPanel extends JPanel implements PropertyChangeListener {

	private MachineController controller;

	private JTextField x;

	private JLabel xLabel;

	private JTextField y;

	private JLabel yLabel;

	private UIPreferences prefs;

	public AxesPanel(MachineController controller, UIPreferences prefs) {
		super(new GridBagLayout());

		this.controller = controller;
		this.controller.getMachineState().addPropertyChangeListener(this);

		this.prefs = prefs;
		
		x = createField();
		y = createField();

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(xLabel = createLabel("X"), c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		add(x, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		add(yLabel = createLabel("Y"), c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		add(y, c);

		createXMenu();
		createYMenu();
	}

	private JTextField createField() {
		JTextField f = new JTextField(format(new BigDecimal(0)), 8);
		f.setEditable(false);
		f.setFont(prefs.getDroFont());
		f.setForeground(prefs.getDroForeground());
		f.setHorizontalAlignment(JTextField.RIGHT);
		return f;
	}

	private JLabel createLabel(String text) {
		JLabel l = new JLabel(text);
		l.setFont(prefs.getDroFont());
		l.setForeground(prefs.getDroForeground());
		return l;
	}

	private void createXMenu() {
		JPopupMenu xMenu = new JPopupMenu();
		
		JMenuItem item = new JMenuItem("Go to zero on X axis (G53 G0 X0)");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.goToMachineZero(Axis.X);
			}
		});
		xMenu.add(item);
		
		item = new JMenuItem("Zero X axis (G28.3 X0)");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.zeroMachine(Axis.X);
			}
		});
		xMenu.add(item);
		
		item = new JMenuItem("Home X axis (G28.2 X0)");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.homeMachine(Axis.X);
			}
		});
		xMenu.add(item);
		
		xLabel.setComponentPopupMenu(xMenu);
	}

	private void createYMenu() {
		JPopupMenu yMenu = new JPopupMenu();
		
		JMenuItem item = new JMenuItem("Go to zero on Y axis (G53 G0 Y0)");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.goToMachineZero(Axis.Y);
			}
		});
		yMenu.add(item);
		
		item = new JMenuItem("Zero Y axis (G28.3 Y0)");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.zeroMachine(Axis.Y);
			}
		});
		yMenu.add(item);
		
		item = new JMenuItem("Home Y axis (G28.2 Y0)");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.homeMachine(Axis.Y);
			}
		});
		yMenu.add(item);
		
		yLabel.setComponentPopupMenu(yMenu);
	}

	private String format(BigDecimal value) {
		DecimalFormat df = new DecimalFormat("+#,##0.000;-#,##0.000");
		return df.format(value);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object newValue = evt.getNewValue();
		JTextField field;
		switch (evt.getPropertyName()) {
		case "x":
			field = x;
			break;
		case "y":
			field = y;
			break;
		default:
			// Nothing we're interested in, bail early
			return;
		}
		
		SwingUtilities.invokeLater(new DelayedUpdate(field, (BigDecimal) newValue));
	}

	private class DelayedUpdate implements Runnable {
		JTextField field;
		BigDecimal value;
		
		DelayedUpdate(JTextField field, BigDecimal value) {
			this.field = field;
			this.value = value;
		}
		
		public void run() {
			field.setText(format(value));
		}
	}
}
