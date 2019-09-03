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
import com.github.aushacker.g2client.ui.UIPreferences;

import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author Stephen Davies
 * @since August 2019
 */
public class ControlPane extends G2Pane<GridPane> {

	private ControlPane(Stage top, IController controller, UIPreferences preferences) {
		super(top, controller, preferences);
	}

	@Override
	protected void createWidgets() {
		
	}

	@Override
	protected void initializePane() {
		setPane(new GridPane());
		getPane().setHgap(10);
		getPane().setVgap(10);
	}

	@Override
	protected void layoutWidgets() {
		
	}
}
