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

package com.github.aushacker.g2client.jfx;

import com.fazecast.jSerialComm.SerialPort;
import com.github.aushacker.g2client.conn.OperatingSystem;
import com.github.aushacker.g2client.ui.UIPreferences;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/**
 * Display and edit the UIPreferences.
 *
 * @author Stephen Davies
 * @since August 2019
 */
public class ConfigPane {

	/**
	 * Underlying model.
	 */
	private UIPreferences prefs;

	/**
	 * Top level view object.
	 */
	private GridPane pane;

	/**
	 * Mutable widgets.
	 */
	private TextField tfGcodeDir;
	private TextField tfSettingsFile;
	private ComboBox<SerialPort>  cbSerialPort;

	/**
	 * Use {@link #create(UIPreferences)}.
	 * 
	 * @param state
	 */
	private ConfigPane(UIPreferences prefs) {
		this.prefs = prefs;
	}
	
	/**
	 * Create required widgets and hook their events.
	 * 
	 * @param prefs
	 */
	public static Node create(UIPreferences prefs) {
		return new ConfigPane(prefs).create();
	}

	/**
	 * Create and layout the UI objects.
	 */
	private Node create() {
		pane = new GridPane();
		pane.setHgap(10);

		createWidgets();
		layoutWidgets();
		hookEvents();
		
		return pane;
	}
	
	private void createWidgets() {
		tfGcodeDir = new TextField(prefs.getScriptHome());
		tfGcodeDir.setPrefColumnCount(40);
		tfSettingsFile = new TextField(prefs.getInitialScript());
		tfSettingsFile.setPrefColumnCount(40);

		cbSerialPort = new ComboBox<>();
		cbSerialPort.setButtonCell(new SerialPortListCell());
		cbSerialPort.setCellFactory(new Callback<ListView<SerialPort>, ListCell<SerialPort>>() {
            @Override
            public ListCell<SerialPort> call(ListView<SerialPort> p) {
                return new SerialPortListCell();
            }
        });
		for (SerialPort port : OperatingSystem.current().getFilteredPorts()) {
			cbSerialPort.getItems().add(port);
			if (port.getSystemPortName().equals(prefs.getPortName())) {
				cbSerialPort.setValue(port);
			}
		}
	}
	
	private void hookEvents() {
		
	}
	
	private void layoutWidgets() {
		pane.add(new Label("GCode Directory"), 0, 0);
		pane.add(new Label("Serial Port"), 0, 1);
		pane.add(new Label("Settings File"), 0, 2);

		pane.add(tfGcodeDir,  1,  0);
		pane.add(cbSerialPort, 1, 1);
		pane.add(tfSettingsFile,  1,  2);
	}
	
	static class SerialPortListCell extends ListCell<SerialPort> {
		@Override
		protected void updateItem(SerialPort item, boolean empty) {
			super.updateItem(item, empty);
			if (!empty && item != null) {
				setText(item.getSystemPortName() + " (" + item.getDescriptivePortName() + ")");
			} else {
				setText(null);
			}
		}
	}
}
