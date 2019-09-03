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

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author Stephen Davies
 * @since September 2019
 */
public class AxesPane extends G2Pane<GridPane> {

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
		return f;
	}

	@Override
	protected void createWidgets() {
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

	@Override
	protected void hookEvents() {
		
	}

	@Override
	protected void initializePane() {
		setPane(new GridPane());
	}

	@Override
	protected void layoutWidgets() {
		getPane().add(xPos, 0, 0);
		getPane().add(yPos, 0, 1);
		getPane().add(zPos, 0, 2);
	}
}
