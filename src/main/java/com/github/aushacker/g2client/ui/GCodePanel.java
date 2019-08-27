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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.github.aushacker.g2client.conn.IController;

/**
 * @author Stephen Davies
 * @since March 2019
 */
public class GCodePanel extends G2Panel {

	private static final long serialVersionUID = -1699674077772697176L;

	private JTextPane textPane;
	private JScrollPane scrollPane;

	private JButton playButton = new JButton("Play");

	public GCodePanel(IController controller, UIPreferences preferences) {
		super(new BorderLayout(), controller, preferences);

		// Create and link the Gcode text objects
		textPane = new JTextPane();
		scrollPane = new JScrollPane(textPane);
		TextLineNumber tln = new TextLineNumber(textPane);
		scrollPane.setRowHeaderView(tln);	
		textPane.setText("");
		textPane.setEditable(false);

		playButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				play();
			}
		});

		ButtonPanel bp = new ButtonPanel();
		bp.addButton(playButton);

		add(bp, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
	}

	private void goToLineZero() {
		textPane.setCaretPosition(0);
	}

	/**
	 * Dumps the contents of a file into the code pane. Hopefully this is
	 * actually a gcode file.
	 */
	public void openFile(File gcode) {
		try (BufferedReader in = new BufferedReader(new FileReader(gcode))) {
			StringBuilder sb = new StringBuilder();
			String line = in.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = in.readLine();
			}

			textPane.setText(sb.toString());
			goToLineZero();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void play() {
		goToLineZero();
		getController().resetLineCounter();
		
		String[] lines = textPane.getText().split("\\n");
		for (int i = 0; i < lines.length; i++) {
			String c = "N" + (i + 1) + " " + lines[i];
			getController().enqueue(c);
		}
	}

	class ButtonPanel extends JPanel {

		private static final long serialVersionUID = -3107027042637386805L;

		ButtonPanel() {
			setLayout(new FlowLayout(FlowLayout.LEFT));
		}

		void addButton(JButton button) {
			add(button);
		}
	}
}
