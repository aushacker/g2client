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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author Stephen Davies
 * @since September 2019
 */
public class StatusPane extends G2Pane<GridPane> {

    private Button btFeedhold;
    private Button btFlush;
    private Button btKill;
    private Button btResume;

    private TextField tfFeedrate;
    private TextField tfLine;
    private TextField tfStatus;
    private TextField tfVelocity;

    public StatusPane(Stage top, IController controller, UIPreferences preferences) {
        super(top, controller, preferences);

        initialize();
    }

    private TextField createTextField(String text) {
        TextField tf = new TextField(text);
        tf.setEditable(false);
        tf.setPrefColumnCount(8);

        return tf;
    }

    @Override
    protected void createWidgets() {
        btFeedhold = new Button("Feedhold");
        btFlush = new Button("Flush Q");
        btKill = new Button("Kill");
        btResume = new Button("Resume");

        tfFeedrate = createTextField(getFeedrate());
        tfLine = createTextField(getLine());
        tfStatus = createTextField(getStatus());
        tfVelocity = createTextField(getVelocity());
    }

    private String getFeedrate() {
        return Integer.toString(getMachineState().getFeedRate());
    }

    private String getLine() {
        return Integer.toString(getMachineState().getLine());
    }

    private String getStatus() {
        return getMachineState().getDynamicState().toString();
    }

    private String getVelocity() {
        return getMachineState().getVelocity().toPlainString();
    }

    @Override
    protected void hookEvents() {
        btFeedhold.setOnAction(e -> getController().feedhold());
        btFlush.setOnAction(e -> getController().flush());
        btKill.setOnAction(e -> getController().killJob());
        btResume.setOnAction(e -> getController().resume());
        
        getMachineState().feedRateProperty().addListener(
                e -> tfFeedrate.setText(getFeedrate()));
        getMachineState().lineProperty().addListener(
                e -> tfLine.setText(getLine()));
        getMachineState().dynamicStateProperty().addListener(
                e -> tfStatus.setText(getStatus()));
        getMachineState().velocityProperty().addListener(
                e -> tfVelocity.setText(getVelocity()));
    }

    @Override
    protected void initializePane() {
        setPane(new GridPane());
        
        getPane().setHgap(5);
        getPane().setVgap(5);
    }

    @Override
    protected void layoutWidgets() {
        FlowPane buttons = new FlowPane(5, 5);
        buttons.getChildren().addAll(btFeedhold, btResume, btFlush, btKill);
        
        getPane().add(buttons, 0, 0, 4, 1);
        getPane().add(new Label("Velocity:"), 0, 1);
        getPane().add(tfVelocity, 1, 1);
        getPane().add(new Label("Feed Rate:"), 2, 1);
        getPane().add(tfFeedrate, 3, 1);
        getPane().add(new Label("Line:"), 0, 2);
        getPane().add(tfLine, 1, 2);
        getPane().add(new Label("Status:"), 2, 2);
        getPane().add(tfStatus, 3, 2);
    }
}
