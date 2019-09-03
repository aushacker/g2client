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

import java.io.File;

import com.fazecast.jSerialComm.SerialPort;
import com.github.aushacker.g2client.conn.IController;
import com.github.aushacker.g2client.conn.OperatingSystem;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Display and edit the UIPreferences.
 *
 * @author Stephen Davies
 * @since August 2019
 */
public class ConfigPane extends G2Pane<GridPane> {

	/**
	 * Mutable widgets.
	 */
	private TextField tfGcodeDir;
	private TextField tfSettingsFile;
	private ComboBox<SerialPort>  cbSerialPort;

	private Button btGcodeDir;
	private Button btSettingsFile;

	/**
	 *
	 */
	public ConfigPane(Stage top, IController controller, UIPreferences preferences) {
		super(top, controller, preferences);
		
		initialize();
	}
	
	@Override
	protected void createWidgets() {
		tfGcodeDir = new TextField(getPreferences().getScriptHome());
		tfGcodeDir.setPrefColumnCount(40);
		tfGcodeDir.setEditable(false);
		
		tfSettingsFile = new TextField(getPreferences().getInitialScript());
		tfSettingsFile.setPrefColumnCount(40);
		tfSettingsFile.setEditable(false);

		cbSerialPort = new ComboBox<>();
		// custom cell rendering
		cbSerialPort.setButtonCell(new SerialPortListCell());
		cbSerialPort.setCellFactory(new Callback<ListView<SerialPort>, ListCell<SerialPort>>() {
            @Override
            public ListCell<SerialPort> call(ListView<SerialPort> p) {
                return new SerialPortListCell();
            }
        });
		
		// Populate the combo and select the preferred port
		for (SerialPort port : OperatingSystem.current().getFilteredPorts()) {
			cbSerialPort.getItems().add(port);
			if (port.getSystemPortName().equals(getPreferences().getPortName())) {
				cbSerialPort.setValue(port);
			}
		}
		
		btGcodeDir = new Button("Edit...");
		btSettingsFile = new Button("Edit...");
	}

	private void handleChangedPort() {
		getPreferences().setPortName(cbSerialPort.getValue() == null ? "" : cbSerialPort.getValue().getSystemPortName());
	}

	/**
	 * Allows the client to change the gcode script home directory.
	 */
	private void handleEditGcodeDir() {
		DirectoryChooser dialog = new DirectoryChooser();

		if (getPreferences().getScriptHome() != null &&
				new File(getPreferences().getScriptHome()).exists()) {
			dialog.setInitialDirectory(new File(getPreferences().getScriptHome()));
		} else {
			dialog.setInitialDirectory(new File(System.getProperty("user.home")));
		}

		dialog.setTitle("GCode Script Directory");
		
		File f = dialog.showDialog(getTop());
		if (f != null) {
			getPreferences().setScriptHome(f.getAbsolutePath());
			tfGcodeDir.setText(getPreferences().getScriptHome());
		}
	}

	/**
	 * Allows the client to change the gcode script home directory.
	 */
	private void handleEditSettingsFile() {
		FileChooser dialog = new FileChooser();

		dialog.setTitle("Settings File");
		dialog.getExtensionFilters().addAll(
				new ExtensionFilter("Settings Files", "*.settings"),
				new ExtensionFilter("All Files", "*.*")
		);
		
		if (getPreferences().getInitialScript() != null) {
			File f = new File(getPreferences().getInitialScript());
			if (f.exists() && f.isFile()) {
				dialog.setInitialDirectory(new File(f.getParent()));
			}
		}
		
		File f = dialog.showOpenDialog(getTop());
		if (f != null) {
			getPreferences().setInitialScript(f.getAbsolutePath());
			tfSettingsFile.setText(getPreferences().getInitialScript());
		}
	}

	@Override
	protected void hookEvents() {
		btGcodeDir.setOnAction(e -> handleEditGcodeDir());
		btSettingsFile.setOnAction(e -> handleEditSettingsFile());
		cbSerialPort.setOnAction(e -> handleChangedPort());
	}
	
	@Override
	protected void initializePane() {
		setPane(new GridPane());
		getPane().setPadding(new Insets(10));
		getPane().setHgap(10);
		getPane().setVgap(5);
	}

	@Override
	protected void layoutWidgets() {
		getPane().add(new Label("GCode Directory:"), 0, 0);
		getPane().add(new Label("Serial Port:"), 0, 1);
		getPane().add(new Label("Settings File:"), 0, 2);

		getPane().add(tfGcodeDir,  1,  0);
		getPane().add(cbSerialPort, 1, 1);
		getPane().add(tfSettingsFile,  1,  2);
		
		getPane().add(btGcodeDir, 2, 0);
		getPane().add(btSettingsFile, 2, 2);
	}

	/**
	 * Custom cell renderer for the SerialPort
	 */
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
