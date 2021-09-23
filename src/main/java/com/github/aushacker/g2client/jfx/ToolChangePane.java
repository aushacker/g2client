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

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author Stephen Davies
 * @since September2019
 */
public class ToolChangePane extends G2Pane<GridPane> {

	private Button btChange;
	private Button btZeroDrill;
	private Button btZeroEngraver;
	private Button btComplete;
	
	public ToolChangePane(Stage top, IController controller, UIPreferences preferences) {
		super(top, controller, preferences);

		initialize();
	}

	@Override
	protected void createWidgets() {
		btChange = new Button("Goto Change");
		btZeroDrill = new Button("Goto Zero Drill");
		btZeroEngraver = new Button("Goto Zero Engraver");
		btComplete = new Button("Complete");
	}

	private void handleChange() {
		getController().enqueue("G28.1");
		getController().enqueue("G53 G0 X0");
		getController().enqueue("G53 G0 Y0");
	}

	private void handleComplete() {
		getController().enqueue("G53 G0 Y36");
		getController().enqueue("G28");
	}

	private void handleZeroDrill() {
		getController().enqueue("G53 G0 X0 Y31");
	}

	private void handleZeroEngraver() {
		getController().enqueue("G53 G0 X10 Y31");
	}

	@Override
	protected void hookEvents() {
		btChange.setOnAction(e -> handleChange());
		btZeroDrill.setOnAction(e -> handleZeroDrill());
		btZeroEngraver.setOnAction(e -> handleZeroEngraver());
		btComplete.setOnAction(e -> handleComplete());
	}

	@Override
	protected void initializePane() {
		setPane(new GridPane());
		
		getPane().setVgap(5);
	}

	@Override
	protected void layoutWidgets() {
		getPane().add(btChange, 0, 0);
		getPane().add(btZeroDrill, 0, 1);
		getPane().add(btZeroEngraver, 0, 2);
		getPane().add(btComplete, 0, 3);
	}
}
