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

import org.controlsfx.tools.Borders;

import com.github.aushacker.g2client.conn.IController;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Stephen Davies
 * @since August 2019
 */
public class ControlPane extends G2Pane<VBox> {

	private AxesPane axesPane;

	private StatusPane statusPane;

	public ControlPane(Stage top, IController controller, UIPreferences preferences) {
		super(top, controller, preferences);
		
		initialize();
	}

	@Override
	protected void createWidgets() {
		axesPane = new AxesPane(getTop(), getController(), getPreferences());
		statusPane = new StatusPane(getTop(), getController(), getPreferences());
	}

	@Override
	protected void initializePane() {
		setPane(new VBox());
	}

	@Override
	protected void layoutWidgets() {
		Node axes = Borders.wrap(axesPane.getPane()).lineBorder().title("Axis Control").color(Color.BLACK).buildAll();
		Node status = Borders.wrap(statusPane.getPane()).lineBorder().title("Machine Status").color(Color.BLACK).buildAll();

		getPane().getChildren().addAll(axes, status);
	}
}
