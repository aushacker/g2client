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

import com.github.aushacker.g2client.conn.IController;
import com.github.aushacker.g2client.state.SystemState;
import com.github.aushacker.g2client.ui.UIPreferences;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Displays a bunch of read only values that describe the firmware and hardware
 * settings used when the G2 code was compiled.
 *
 * @author Stephen Davies
 * @since August 2019
 */
public class SystemStatePane extends G2Pane<GridPane> {

	/**
	 * Underlying model.
	 */
	private SystemState state;

	/**
	 * Mutable widgets.
	 */
	private TextField tfFirmwareBuild;
	private TextField tfFirmwareBuildString;
	private TextField tfFirmwareConfig;
	private TextField tfFirmwareVersion;
	private TextField tfHardwareVersion;
	
	/**
	 */
	public SystemStatePane(Stage top, IController controller, UIPreferences preferences) {
		super(top, controller, preferences);
		
		this.state = getMachineState().getSystemState();
		initialize();
		populateText();
	}
	
	/**
	 * Utility method.
	 */
	private TextField createTextField(String text) {
		TextField result = new TextField(text);
		result.setEditable(false);
		
		return result;
	}

	/**
	 * Create the mutable widgets.
	 */
	@Override
	protected void createWidgets() {
		tfFirmwareBuild = createTextField("");
		tfFirmwareBuildString = createTextField("");
		tfFirmwareConfig = createTextField("");
		tfFirmwareVersion = createTextField("");
		tfHardwareVersion = createTextField("");
	}

	/**
	 * Observe the model for property changes.
	 */
	@Override
	protected void hookEvents() {
		state.firmwareBuildProperty().addListener(
				(obj, oldValue, newValue) -> tfFirmwareBuild.setText(newValue.toString())); 

		state.firmwareBuildStringProperty().addListener(
				(obj, oldValue, newValue) -> tfFirmwareBuildString.setText(newValue)); 

		state.firmwareConfigProperty().addListener(
				(obj, oldValue, newValue) -> tfFirmwareConfig.setText(newValue)); 

		state.firmwareVersionProperty().addListener(
				(obj, oldValue, newValue) -> tfFirmwareVersion.setText(newValue.toString())); 

		state.hardwareVersionProperty().addListener(
				(obj, oldValue, newValue) -> tfHardwareVersion.setText(newValue.toString())); 
	}

	@Override
	protected void initializePane() {
		setPane(new GridPane());
		getPane().setHgap(10);
		getPane().setVgap(5);
	}

	/**
	 * Organize the widgets within the grid.
	 */
	@Override
	protected void layoutWidgets() {
		getPane().add(new Label("Firmware Build:"), 0, 0);
		getPane().add(new Label("Firmware Build String:"), 0, 1);
		getPane().add(new Label("Firmware Config:"), 0, 2);
		getPane().add(new Label("Firmware Version:"), 0, 3);
		getPane().add(new Label("Hardware Version:"), 0, 4);
	
		getPane().add(tfFirmwareBuild,  1,  0);
		getPane().add(tfFirmwareBuildString,  1,  1);
		getPane().add(tfFirmwareConfig,  1,  2);
		getPane().add(tfFirmwareVersion,  1,  3);
		getPane().add(tfHardwareVersion,  1,  4);
	}

	/**
	 * Due to a timing issue with the superclass constructor was need to delay
	 * the field initialisation.
	 */
	private void populateText() {
		tfFirmwareBuild.setText(Double.toString(state.getFirmwareBuild()));
		tfFirmwareBuildString.setText(state.getFirmwareBuildString());
		tfFirmwareConfig.setText(state.getFirmwareConfig());
		tfFirmwareVersion.setText(Double.toString(state.getFirmwareVersion()));
		tfHardwareVersion.setText(Double.toString(state.getHardwareVersion()));
	}
}
