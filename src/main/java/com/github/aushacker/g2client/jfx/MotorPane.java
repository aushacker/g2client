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
import com.github.aushacker.g2client.state.Motor;

import javafx.collections.FXCollections;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @author Stephen Davies
 * @since September 2019
 */
public class MotorPane extends G2Pane<StackPane> {

    private ScrollPane scrollPane;

    private TableView<Motor> table;

    public MotorPane(Stage top, IController controller, UIPreferences preferences) {
        super(top, controller, preferences);
        
        initialize();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void createWidgets() {
        scrollPane = new ScrollPane();

        table = new TableView<>(FXCollections.observableArrayList(getMachineState().getMotors()));
        
        TableColumn<Motor,Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<Motor,Integer>("id"));
        
        TableColumn<Motor,Double> stepAngleCol = new TableColumn<>("Step Angle");
        stepAngleCol.setCellValueFactory(new PropertyValueFactory<Motor,Double>("stepAngle"));
         
        TableColumn<Motor,Double> perRevCol = new TableColumn<>("Travel/Rev");
        perRevCol.setCellValueFactory(new PropertyValueFactory<Motor,Double>("travelPerRev"));
         
        TableColumn<Motor,Integer> microstepCol = new TableColumn<>("Microsteps");
        microstepCol.setCellValueFactory(new PropertyValueFactory<Motor,Integer>("microsteps"));
         
        TableColumn<Motor,Double> stepsUnitCol = new TableColumn<>("Steps/Unit");
        stepsUnitCol.setCellValueFactory(new PropertyValueFactory<Motor,Double>("stepsPerUnit"));
         
        TableColumn<Motor,Integer> polarityCol = new TableColumn<>("Polarity");
        polarityCol.setCellValueFactory(new PropertyValueFactory<Motor,Integer>("polarity"));
         
        table.getColumns().addAll(idCol, stepAngleCol, perRevCol, microstepCol, stepsUnitCol, polarityCol);
    }

    @Override
    protected void initializePane() {
        setPane(new StackPane());
    }

    @Override
    protected void layoutWidgets() {
        getPane().getChildren().add(scrollPane);
        
        scrollPane.setContent(table);
    }
}
