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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.github.aushacker.g2client.conn.IController;
import com.github.aushacker.g2client.state.Axis;
import com.github.aushacker.g2client.state.MachineState;

/**
 * @author Stephen Davies
 * @since March 2019
 */
public class JogPanel extends G2Panel implements PropertyChangeListener {

	private static final long serialVersionUID = -6706432287088809733L;

	private MachineState state;

	private boolean keyboardEnabled;

	private JButton xPos;
	private JButton xNeg;
	private JButton yPos;
	private JButton yNeg;
	private JButton zPos;
	private JButton zNeg;
	private JButton aPos;
	private JButton aNeg;
	private JButton cycleIncrement;
	private JButton modeToggle;
	private JButton keyboardToggle;

	private JTextField increment;

	public JogPanel(IController controller, UIPreferences prefs) {
		super(new FlowLayout(), controller, prefs);

		this.state = controller.getMachineState();
		state.addPropertyChangeListener(this);

		createWidgets();
		layoutWidgets();
		initialiseEvents();
	}

	private void createWidgets() {
		xPos = new JButton("X+");
		xNeg = new JButton("X-");
		yPos = new JButton("Y+");
		yNeg = new JButton("Y-");
		zPos = new JButton("Z+");
		zNeg = new JButton("Z-");
		aPos = new JButton("A+");
		aNeg = new JButton("A-");
		cycleIncrement = new JButton("Cycle Jog Inc");
		modeToggle = new JButton("Jog Mode");
		keyboardToggle = new JButton(getKeyboardButtonText());

		increment = new JTextField(getIncrement());
		increment.setEnabled(false);
	}

	private String getIncrement() {
		DecimalFormat df = new DecimalFormat("#,##0.0####");
		return df.format(state.getJogIncrement());
	}

	private String getKeyboardButtonText() {
		return keyboardEnabled ? "Keyboard Jog On" : "Keyboard Jog Off";
	}

	private void initialiseEvents() {
		keyboardToggle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				keyboardEnabled = !keyboardEnabled;
				keyboardToggle.setText(getKeyboardButtonText());
			}
			
		});

		cycleIncrement.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				state.cycleJogIncrement();
			}
			
		});
		
		xPos.addActionListener(new AxisButtonHandler(Axis.X, true));
		xNeg.addActionListener(new AxisButtonHandler(Axis.X, false));
		yPos.addActionListener(new AxisButtonHandler(Axis.Y, true));
		yNeg.addActionListener(new AxisButtonHandler(Axis.Y, false));
		zPos.addActionListener(new AxisButtonHandler(Axis.Z, true));
		zNeg.addActionListener(new AxisButtonHandler(Axis.Z, false));
		aPos.addActionListener(new AxisButtonHandler(Axis.A, true));
		aNeg.addActionListener(new AxisButtonHandler(Axis.A, false));
	}

	private void layoutWidgets() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JPanel line1 = new JPanel();
		line1.setLayout(new BoxLayout(line1, BoxLayout.LINE_AXIS));
		line1.add(aPos);
		line1.add(Box.createHorizontalGlue());
		line1.add(yPos);
		line1.add(Box.createHorizontalGlue());
		line1.add(zPos);

		JPanel line2 = new JPanel();
		line2.setLayout(new BoxLayout(line2, BoxLayout.LINE_AXIS));
		line2.add(Box.createHorizontalGlue());
		line2.add(xNeg);
		line2.add(xPos);
		line2.add(Box.createHorizontalGlue());

		JPanel line3 = new JPanel();
		line3.setLayout(new BoxLayout(line3, BoxLayout.LINE_AXIS));
		line3.add(aNeg);
		line3.add(Box.createHorizontalGlue());
		line3.add(yNeg);
		line3.add(Box.createHorizontalGlue());
		line3.add(zNeg);

		JPanel leftToggles = new JPanel(new GridLayout(2,1));
		leftToggles.add(cycleIncrement);
		leftToggles.add(modeToggle);

		JPanel settings = new JPanel(new GridLayout(2,2));
		settings.add(increment);

		JPanel line4 = new JPanel(new BorderLayout());
		line4.add(leftToggles, BorderLayout.LINE_START);
		line4.add(settings, BorderLayout.CENTER);
		line4.add(keyboardToggle, BorderLayout.LINE_END);

		add(line1);
		add(line2);
		add(line3);
		add(line4);
	}

	/**
	 * Listening for changes to the MachineState.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch(evt.getPropertyName()) {
		case "jogIndex":
			SwingUtilities.invokeLater(new DelayedTextUpdate(increment, getIncrement()));
			break;
		default:
		}
	}

	private class AxisButtonHandler implements ActionListener {
		private Axis axis;
		private boolean positive;
		
		AxisButtonHandler(Axis axis, boolean positive) {
			this.axis = axis;
			this.positive = positive;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			getController().jog(axis, positive);
		}
	}
}
