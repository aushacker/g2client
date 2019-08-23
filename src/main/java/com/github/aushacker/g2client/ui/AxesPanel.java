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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.github.aushacker.g2client.conn.IController;
import com.github.aushacker.g2client.state.Axis;

/**
 * Displays X, Y & Z axis information.
 *
 * @author Stephen Davies
 * @since March 2019
 */
public class AxesPanel extends G2Panel implements PropertyChangeListener {

	private static final long serialVersionUID = 2012787993585660363L;

	private JButton home;
	private JButton x;
	private JButton y;
	private JButton z;
	private JButton zero;

	private JTextField xPos;
	private JTextField yPos;
	private JTextField zPos;

	public AxesPanel(IController controller, UIPreferences prefs) {
		super(new BorderLayout(), controller, prefs);

		createWidgets();
		layoutWidgets();
		initialiseEvents();
	}

	private JButton createButton(String text, String toolTip) {
		JButton bt = new JButton("<html>" + text + "</html>");
		bt.setToolTipText(toolTip);
		return bt;
	}

	private JTextField createField() {
		JTextField f = new JTextField(format(new BigDecimal(0)), 8);
		f.setEditable(false);
		f.setFont(getPrefs().getDroFont());
		f.setForeground(getPrefs().getDroForeground());
		f.setHorizontalAlignment(JTextField.RIGHT);
		return f;
	}

	private void createWidgets() {
		xPos = createField();
		yPos = createField();
		zPos = createField();

		home = createButton("Home<br>Machine", "G28.2 X0 Y0");
		x = createButton("Zero<br>X", "G0 X0");
		y = createButton("Zero<br>Y", "G0 Y0");
		z = createButton("Zero<br>Z", "G0 Z0");
		zero = createButton("Goto XY Zero", "");
	}

	/**
	 * Pretty print DRO fields.
	 */
	private String format(BigDecimal value) {
		DecimalFormat df = new DecimalFormat("+#,##0.000;-#,##0.000");
		return df.format(value);
	}

	private void initialiseEvents() {
		getMachineState().addPropertyChangeListener(this);
		
		home.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getController().homeMachine(getPrefs().getHoming());
			}
		});
		
		x.addActionListener(new ZeroAxisHandler(Axis.X));
		y.addActionListener(new ZeroAxisHandler(Axis.Y));
		z.addActionListener(new ZeroAxisHandler(Axis.Z));

		zero.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getController().goToZero(Arrays.asList(Axis.X, Axis.Y));
			}
		});
	}

	/**
	 * Using BorderLayout at top level to make sizing of home button easy.
	 * Other widgets are dumped into a nested panel for easier layout management.
	 */
	private void layoutWidgets() {
		setBorder(BorderFactory.createTitledBorder("Axis Control"));
	
		JPanel dro = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
	
		// X
		dro.add(x, c);
	
		c.gridwidth = GridBagConstraints.REMAINDER;
		dro.add(xPos, c);
	
		// Y
		c.gridwidth = 1;
		dro.add(y, c);
	
		c.gridwidth = GridBagConstraints.REMAINDER;
		dro.add(yPos, c);
	
		// Z
		c.anchor = GridBagConstraints.NORTH;
		c.gridwidth = 1;
		c.weighty = 1;
		dro.add(z, c);
	
		c.gridwidth = GridBagConstraints.REMAINDER;
		dro.add(zPos, c);

		JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bottomButtons.add(zero);

		add(home, BorderLayout.WEST);
		add(dro, BorderLayout.CENTER);
		add(bottomButtons, BorderLayout.SOUTH);
	}

	/**
	 * Filter and queue an update for relevant model changes.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object newValue = evt.getNewValue();
		JTextField field;
		switch (evt.getPropertyName()) {
		case "x":
			field = xPos;
			break;
		case "y":
			field = yPos;
			break;
		case "z":
			field = zPos;
			break;
		default:
			// Nothing we're interested in, bail early
			return;
		}
		
		SwingUtilities.invokeLater(new DelayedTextUpdate(field, format((BigDecimal) newValue)));
	}

	private class ZeroAxisHandler implements ActionListener {
		Axis axis;
		
		ZeroAxisHandler(Axis axis) {
			this.axis = axis;
		}
		
		public void actionPerformed(ActionEvent e) {
			getController().zero(axis);
		}
	}
}
