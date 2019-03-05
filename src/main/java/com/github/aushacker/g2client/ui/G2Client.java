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
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.fazecast.jSerialComm.SerialPort;
import com.github.aushacker.g2client.conn.MachineController;
import com.github.aushacker.g2client.conn.OperatingSystem;

/**
 * Top level UI component i.e. the application.
 *
 * @author Stephen Davies
 * @since March 2019
 */
public class G2Client extends JFrame {

	private static final long serialVersionUID = 7164706210700770341L;

	private MachineController controller;

	private UIPreferences preferences;

	public G2Client() {
		super("G2Client - Gcode Runner");

		controller = new MachineController();
		preferences = new UIPreferences();

		Container contentPane = getContentPane();

		JTabbedPane tp = new JTabbedPane();

		tp.addTab("Run", new RunPanel(controller, preferences));
		tp.addTab("Config", new ConfigPanel(controller, preferences));
		tp.addTab("Diagnostics", new JPanel());

		contentPane.add(tp, BorderLayout.CENTER);

		SwingUtilities.invokeLater(new InitializationProcess(tp));
	}

	private void connect() {
		controller.connect(OperatingSystem.current().getFilteredPorts().get(0));
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		controller.enqueue("{\"x\":{\"am\":1}}");
		controller.enqueue("{\"y\":{\"am\":1}}");
		controller.enqueue("{\"sr\":n}");
		controller.enqueue("{\"line\":0}");
//				controller.enqueue("N1 G21 G90");
//				controller.enqueue("N2 G1 X10 Y10 F800");
//				controller.enqueue("N3 G1 X10 Y10");
//				controller.enqueue("N4 G1 X-10 Y10");
//				controller.enqueue("N5 G1 X-10 Y-10");
//				controller.enqueue("N6 G1 X10 Y-10");
//				controller.enqueue("N7 G1 X10 Y10");
//				controller.enqueue("N8 G1 X0 Y0");
//				controller.enqueue("{\"line\":n}");
//				controller.enqueue("{\"di1\":{\"mo\":0,\"ac\":2,\"fn\":1}}");
//				controller.enqueue("{\"di2\":{\"mo\":0,\"ac\":2,\"fn\":1}}");
//				controller.enqueue("{\"di3\":{\"mo\":0,\"ac\":2,\"fn\":1}}");
//				controller.enqueue("{\"di4\":{\"mo\":0,\"ac\":2,\"fn\":1}}");
	}

	private void shutdown() {
		controller.shutdown();
	}

	public static void main(String[] args) {
		G2Client f = new G2Client();
		
		f.setBounds(100, 100, 800, 400);
		f.setVisible(true);
		f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		f.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				f.shutdown();
				System.exit(0);
			}
		});
	}

	private class InitializationProcess implements Runnable {
		JComponent parent;

		InitializationProcess(JComponent parent) {
			this.parent = parent;
		}

		public void run() {
			// Attempt to find preferred SerialPort
			String portName = preferences.getPortName();
			SerialPort port = null;
			
			for (SerialPort p : OperatingSystem.current().getFilteredPorts()) {
				if (p.getSystemPortName().equals(portName)) {
					port = p;
					break;
				}
			}

			if (port != null) {
				if (!controller.connect(port)) {
					JOptionPane.showMessageDialog(parent,
							"Unable to connect with g2 controller, \n" +
							"check configuration.",
							"Connection Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(parent,
					"Configured serial port not found.",
					"Configuration Error",
					JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
