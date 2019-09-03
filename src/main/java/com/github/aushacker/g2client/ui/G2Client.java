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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.fazecast.jSerialComm.SerialPort;
import com.github.aushacker.g2client.conn.MachineController;
import com.github.aushacker.g2client.conn.OperatingSystem;

/**
 * Top level UI component i.e. the application. Manages global objects like the
 * MachineController and top-level tasks like menus and application lifecycle.
 * <p>
 * Primary UI component is a JTabbedPane that allows the user to switch between
 * the following panels:
 * <ul>
 * <li>{@link RunPanel}</li>
 * <li>{@link ConfigPanel}</li>
 * <li>{@link DiagnosticsPanel}</li>
 * </ul>
 *
 * @author Stephen Davies
 * @since March 2019
 */
public class G2Client extends JFrame {

	private static final long serialVersionUID = 7164706210700770341L;

	private MachineController controller;

	private UIPreferences preferences;

	private JTabbedPane tp = new JTabbedPane();
	
	private JMenuBar mb = new JMenuBar();
	private JMenu fileMenu = new JMenu("File");
	private JMenuItem fileOpenItem = new JMenuItem("Open...");
	private JMenuItem fileExitItem = new JMenuItem("Exit");

	private RunPanel runPanel;

	public G2Client(UIPreferences preferences) {
		super("G2Client - Gcode Runner");

		this.preferences = preferences;
		this.controller = new MachineController();

		Container contentPane = getContentPane();

		initializeMenus();

		tp.addTab("Run", runPanel = new RunPanel(controller, preferences));
		tp.addTab("Config", new ConfigPanel(controller, preferences));
		tp.addTab("Diagnostics", new DiagnosticsPanel(controller, preferences));

		contentPane.add(tp, BorderLayout.CENTER);

		// Window resizes are persistent
		contentPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				preferences.setHeight(getHeight());
				preferences.setWidth(getWidth());
			}
		});

		SwingUtilities.invokeLater(new InitializationProcess(tp));
	}

	private void fileOpen() {
		String home = preferences.getScriptHome();
		JFileChooser chooser;

		if (home != null && home.trim().length() > 0) {
			chooser = new JFileChooser(home);
		} else {
			chooser = new JFileChooser();
		}

		int result = chooser.showOpenDialog(tp);
		File script = chooser.getSelectedFile();
		if (result == JFileChooser.APPROVE_OPTION && script.exists()) {
			runPanel.openFile(script);
		}
	}

	private void initializeMenus() {
		mb.add(fileMenu);
		fileMenu.add(fileOpenItem);
		fileMenu.add(fileExitItem);
		
		initMenuEvents();
		
		setJMenuBar(mb);
	}
	
	private void initMenuEvents() {
		fileOpenItem.addActionListener(e -> fileOpen());
		fileExitItem.addActionListener(e -> shutdown());
	}

	private void shutdown() {
		controller.shutdown();
		System.exit(0);
	}

	public static void main(String[] args) {
		UIPreferences preferences = new UIPreferences();
		G2Client f = new G2Client(preferences);

		f.setBounds(100, 100, preferences.getWidth(), preferences.getHeight());
		f.setVisible(true);
		f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				f.shutdown();
			}
		});
	}

	/**
	 * Attempts to connect to the SerialPort and initialize the g2 controller.
	 */
	private class InitializationProcess implements Runnable {
		JComponent parent;

		InitializationProcess(JComponent parent) {
			this.parent = parent;
		}

		@Override
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
				// Found
				if (controller.connect(port)) {
					// Connected ok, try to play initial script
					try (BufferedReader in = new BufferedReader(new FileReader(preferences.getInitialScript()))) {
						String line = in.readLine();
						while (line != null) {
							controller.enqueue(line);
							line = in.readLine();
						}
					}
					catch (Exception e) {
						JOptionPane.showMessageDialog(parent,
								"Unable to open initial script.",
								"File Not Found",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(parent,
							"Unable to connect with g2 controller, \n" +
							"check configuration.",
							"Connection Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				// SerialPort not found
				JOptionPane.showMessageDialog(parent,
					"Configured serial port not found.",
					"Configuration Error",
					JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
