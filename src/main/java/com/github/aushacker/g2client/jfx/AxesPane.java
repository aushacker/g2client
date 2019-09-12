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

import java.text.DecimalFormat;

import com.github.aushacker.g2client.conn.IController;
import com.github.aushacker.g2client.state.Axis;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * @author Stephen Davies
 * @since September 2019
 */
public class AxesPane extends G2Pane<GridPane> {

	private CoordinatePane coordinatePane;
	private Button btHome;
	private TextField xPos;
	private TextField yPos;
	private TextField zPos;
	
	public AxesPane(Stage top, IController controller, UIPreferences preferences) {
		super(top, controller, preferences);

		initialize();
	}

	private TextField createField() {
		TextField f = new TextField(format(0.0));
		f.setEditable(false);
		f.setAlignment(Pos.CENTER_RIGHT);
		f.setFont(Font.font("Sans Serif", FontWeight.BOLD, 28));
		f.setPrefColumnCount(8);
		return f;
	}

	@Override
	protected void createWidgets() {
		coordinatePane = new CoordinatePane(getTop(), getController(), getPreferences());
		btHome = new Button("Home\nMachine");
		btHome.setMaxHeight(Double.MAX_VALUE);
		xPos = createField();
		yPos = createField();
		zPos = createField();
	}

	/**
	 * Pretty print DRO fields.
	 */
	private String format(double value) {
		DecimalFormat df = new DecimalFormat("+#,##0.000;-#,##0.000");
		return df.format(value);
	}

	private void homeMachine() {
		getController().homeMachine(Axis.X);
		getController().homeMachine(Axis.Y);
	}

	@Override
	protected void hookEvents() {
		btHome.setOnAction(e -> homeMachine());
		getMachineState().getXProperty().addListener(
				(obj, oldValue, newValue) -> xPos.setText(format(newValue.doubleValue())));
		getMachineState().getYProperty().addListener(
				(obj, oldValue, newValue) -> yPos.setText(format(newValue.doubleValue())));
		getMachineState().getZProperty().addListener(
				(obj, oldValue, newValue) -> zPos.setText(format(newValue.doubleValue())));
	}

	@Override
	protected void initializePane() {
		setPane(new GridPane());
		
		getPane().setHgap(5);
		getPane().setVgap(5);
	}

	@Override
	protected void layoutWidgets() {
		getPane().add(coordinatePane.getPane(), 0, 0, 2, 1);
		getPane().add(btHome, 0, 1, 1, 3);	// home button spanning 3 axes values on LHS
		getPane().add(xPos, 1, 1);			// x axis pos
		getPane().add(yPos, 1, 2);			// y axis pos
		getPane().add(zPos, 1, 3);			// z axis pos
	}
}
