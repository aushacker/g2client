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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.aushacker.g2client.conn.IController;
import com.github.aushacker.g2client.state.CoordinateSystem;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * A very small panel to display the current coordinate system.
 *
 * @author Stephen Davies
 * @since September 2019
 */
public class CoordinatePane extends G2Pane<HBox> {

	private Map<CoordinateSystem,Label> labelMap;
	private List<Label> labels;

	public CoordinatePane(Stage top, IController controller, UIPreferences preferences) {
		super(top, controller, preferences);

		labelMap = new HashMap<>();
		labels = new ArrayList<>();

		initialize();
	}

	@Override
	protected void createWidgets() {
		for (CoordinateSystem cs : CoordinateSystem.values()) {
			Label l = new Label(cs.name());
			l.setVisible(false);

			labelMap.put(cs, l);
			labels.add(l);
		}
		
		labelMap.get(CoordinateSystem.G54).setVisible(true);
	}

	private void displayCoordSystem() {
		labels.forEach(l -> l.setVisible(false));
	
		labelMap.get(getMachineState().getCoordinateSystem()).setVisible(true);
	}

	@Override
	protected void hookEvents() {
		getMachineState().coordinateSystemProperty().addListener(
				e -> displayCoordSystem());
	}

	@Override
	protected void initializePane() {
		setPane(new HBox());
	}

	@Override
	protected void layoutWidgets() {
		getPane().getChildren().addAll(labels);
	}
}
