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

import com.github.aushacker.g2client.state.SystemState;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Displays a bunch of read only values that describe the firmware and hardware
 * settings used when the G2 code was compiled.
 *
 * @author Stephen Davies
 * @since August 2019
 */
public class SystemStatePane {

	/**
	 * Underlying model.
	 */
	private SystemState state;

	/**
	 * Top level view object.
	 */
	private GridPane pane;

	/**
	 * Mutable widgets.
	 */
	private TextField tfFirmwareBuild;
	private TextField tfFirmwareBuildString;
	private TextField tfFirmwareConfig;
	private TextField tfFirmwareVersion;
	private TextField tfHardwareVersion;
	
	/**
	 * Use {@link #create(SystemState)}.
	 * 
	 * @param state
	 */
	private SystemStatePane(SystemState state) {
		this.state = state;
	}
	
	/**
	 * Create required widgets and hook their events.
	 * 
	 * @param state
	 */
	public static Node create(SystemState state) {
		return new SystemStatePane(state).create();
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
	private void createWidgets() {
		tfFirmwareBuild = createTextField(Double.toString(state.getFirmwareBuild()));
		tfFirmwareBuildString = createTextField(state.getFirmwareBuildString());
		tfFirmwareConfig = createTextField(state.getFirmwareConfig());
		tfFirmwareVersion = createTextField(Double.toString(state.getFirmwareVersion()));
		tfHardwareVersion = createTextField(Double.toString(state.getHardwareVersion()));
	}

	/**
	 * Observe the model for property changes.
	 */
	private void hookEvents() {
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

	/**
	 * Organize the widgets within the grid.
	 */
	private void layoutWidgets() {
		pane.add(new Label("Firmware Build"), 0, 0);
		pane.add(new Label("Firmware Build String"), 0, 1);
		pane.add(new Label("Firmware Config"), 0, 2);
		pane.add(new Label("Firmware Version"), 0, 3);
		pane.add(new Label("Hardware Version"), 0, 4);
	
		pane.add(tfFirmwareBuild,  1,  0);
		pane.add(tfFirmwareBuildString,  1,  1);
		pane.add(tfFirmwareConfig,  1,  2);
		pane.add(tfFirmwareVersion,  1,  3);
		pane.add(tfHardwareVersion,  1,  4);
	}
}
