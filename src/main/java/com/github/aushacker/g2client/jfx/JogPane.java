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
 * @since September 2019
 */
public class JogPane extends G2Pane<GridPane> {

	private Button btXpos;
	
	private Button btXneg;
	
	private Button btYpos;
	
	private Button btYneg;
	
	public JogPane(Stage top, IController controller, UIPreferences preferences) {
		super(top, controller, preferences);

		initialize();
	}

	@Override
	protected void createWidgets() {
		btXpos = new Button("X+");
		btXneg = new Button("X-");
		btYpos = new Button("Y+");
		btYneg = new Button("Y-");
	}

	@Override
	protected void initializePane() {
		setPane(new GridPane());

		getPane().setHgap(5);
		getPane().setVgap(5);
	}

	@Override
	protected void layoutWidgets() {
		getPane().add(btYpos, 1, 0);
		getPane().add(btXneg, 0, 1);
		getPane().add(btXpos, 2, 1);
		getPane().add(btYneg, 1, 2);
	}
}
