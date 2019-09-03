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

import com.github.aushacker.g2client.conn.IController;
import com.github.aushacker.g2client.ui.UIPreferences;

import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Stephen Davies
 * @since August 2019
 */
public class RunPane extends G2Pane<BorderPane> {

	/**
	 * GCode content
	 */
	//private CodePane codePane;
	
	/**
	 * DRO etc.
	 */
	//private ControlPane controlPane;

	public RunPane(Stage top, IController controller, UIPreferences preferences) {
		super(top, controller, preferences);
	}

	@Override
	protected void createWidgets() {
//		codePanel = new GCodePanel(getController(), getPrefs());
//		controlPanel = new ControlPanel(getController(), getPrefs());
	}

	@Override
	protected void initializePane() {
		setPane(new BorderPane());
	}

	@Override
	protected void layoutWidgets() {
		getPane().setTop(new VBox(new TitledPane("T1", new Button("B1"))));
//		add(codePanel, BorderLayout.CENTER);
//		add(controlPanel, BorderLayout.EAST);
	}

	public void openFile(File file) {
		//codePane.openFile(file);
	}
}
