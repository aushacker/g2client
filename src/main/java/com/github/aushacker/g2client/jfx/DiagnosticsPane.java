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

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Stephen Davies
 * @since September 2019
 */
public class DiagnosticsPane extends G2Pane<GridPane> {

    private SystemStatePane statePane;

    public DiagnosticsPane(Stage top, IController controller, UIPreferences preferences) {
        super(top, controller, preferences);
        
        initialize();
    }

    @Override
    protected void createWidgets() {
        statePane = new SystemStatePane(getTop(), getController(), getPreferences());
    }

    @Override
    protected void initializePane() {
        setPane(new GridPane());
        getPane().setPadding(new Insets(10));
        getPane().setHgap(10);
        getPane().setVgap(10);
    }

    @Override
    protected void layoutWidgets() {
        Node state = Borders.wrap(statePane.getPane()).lineBorder().title("Firmware/Hardware Settings").color(Color.BLACK).buildAll();
        getPane().add(state, 0, 0);
    }
}
