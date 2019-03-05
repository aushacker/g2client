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
public class AxesPanel extends G2Panel implements PropertyChangeListener {

	private static final long serialVersionUID = 2012787993585660363L;

	private JTextField x;

	private JLabel xLabel;

	private JTextField y;

	private JLabel yLabel;

	public AxesPanel(MachineController controller, UIPreferences prefs) {
		super(new GridBagLayout(), controller, prefs);

		getMachineState().addPropertyChangeListener(this);

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

		xLabel.setComponentPopupMenu(createMenu(Axis.X));
		yLabel.setComponentPopupMenu(createMenu(Axis.Y));
	}

	private JTextField createField() {
		JTextField f = new JTextField(format(new BigDecimal(0)), 8);
		f.setEditable(false);
		f.setFont(getPrefs().getDroFont());
		f.setForeground(getPrefs().getDroForeground());
		f.setHorizontalAlignment(JTextField.RIGHT);
		return f;
	}

	private JLabel createLabel(String text) {
		JLabel l = new JLabel(text);
		l.setFont(getPrefs().getDroFont());
		l.setForeground(getPrefs().getDroForeground());
		return l;
	}

	private JPopupMenu createMenu(Axis axis) {
		JPopupMenu menu = new JPopupMenu();
		
		JMenuItem item = new JMenuItem("Go to zero on " + axis + " axis (G53 G0 " + axis + "0)");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getController().goToMachineZero(axis);
			}
		});
		menu.add(item);
		
		item = new JMenuItem("Zero " + axis + " axis (G28.3 " + axis + "0)");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getController().zeroMachine(axis);
			}
		});
		menu.add(item);
		
		item = new JMenuItem("Home " + axis + " axis (G28.2 " + axis + "0)");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getController().homeMachine(axis);
			}
		});
		menu.add(item);
		
		return menu;
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
