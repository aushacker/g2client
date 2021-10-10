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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.fazecast.jSerialComm.SerialPort;
import com.github.aushacker.g2client.conn.IController;
import com.github.aushacker.g2client.conn.MachineController;
import com.github.aushacker.g2client.conn.OperatingSystem;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * @author Stephen Davies
 * @since August 2019
 */
public class G2Client extends Application {

    private Stage mainStage;

    private IController controller;

    private UIPreferences preferences;

    private ConfigPane configPane;

    private DiagnosticsPane diagnosticsPane;

    private RunPane runPane;

    public static void main(String[] args) {
        launch(args);
    }

    private void createChildPanes() {
        configPane = new ConfigPane(mainStage, controller, preferences);
        diagnosticsPane = new DiagnosticsPane(mainStage, controller, preferences);
        runPane = new RunPane(mainStage, controller, preferences);
    }

    /**
     * Create the menu bar at the top of the screen.
     */
    private Node createMenuPane() {
        Menu fileMenu = new Menu("File");
        
        MenuItem fOpen = new MenuItem("Open...");
        fOpen.setOnAction(e -> fileOpen());

        fileMenu.getItems().add(fOpen);
        
        MenuBar mb = new MenuBar();
        mb.getMenus().add(fileMenu);

        return new VBox(mb);
    }

    /**
     * Layout all of the panels in the main view.
     */
    private TabPane createTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        Tab tab = new Tab();
        tab.setText("Run");
        tab.setContent(runPane.getPane());
        tabPane.getTabs().add(tab);

        tab = new Tab();
        tab.setText("Config");
        tab.setContent(configPane.getPane());
        tabPane.getTabs().add(tab);

        tab = new Tab();
        tab.setText("Diagnostics");
        tab.setContent(diagnosticsPane.getPane());
        tabPane.getTabs().add(tab);

        return tabPane;
    }

    /**
     * Handle callback from the File-&gt;Open... menu item.
     */
    private void fileOpen() {
        FileChooser fileChooser = new FileChooser();
        
        fileChooser.setInitialDirectory(new File(preferences.getScriptHome()));
        fileChooser.setTitle("Open GCode File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("GCode Files", "*.cnc", "*.gc", "*.gcode", "*.nc"),
                new ExtensionFilter("All Files", "*.*"));

        File selectedFile = fileChooser.showOpenDialog(mainStage);

        if (selectedFile != null) {
            runPane.openFile(selectedFile);
        }
    }

    public IController getController() {
        return controller;
    }

    public UIPreferences getUIPreferences() {
        return preferences;
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        this.mainStage = mainStage;

        controller = new MachineController();
        preferences = new UIPreferences();
        BorderPane root = new BorderPane();

        createChildPanes();

        root.setTop(createMenuPane());
        root.setCenter(createTabPane());

        Scene scene = new Scene(root, preferences.getWidth(), preferences.getHeight());
        mainStage.widthProperty().addListener((o, old, nw) -> preferences.setWidth(nw.intValue()));
        mainStage.heightProperty().addListener((o, old, nw) -> preferences.setHeight(nw.intValue()));

        mainStage.setTitle("G2Client - Gcode Runner");
        mainStage.setScene(scene);
        mainStage.sizeToScene();
        mainStage.show();
        
        startMachine();
    }

    private void startMachine() {
        // Attempt to find preferred SerialPort
        String portName = preferences.getPortName();
        SerialPort port = null;
        
        for (SerialPort p : OperatingSystem.current().getFilteredPorts()) {
            if (p.getSystemPortName().equals(portName)) {
                port = p;
                break;
            }
        }

        if (port != null) {
            // Found
            if (controller.connect(port)) {
                // Connected ok, try to play initial script
                try (BufferedReader in = new BufferedReader(new FileReader(preferences.getInitialScript()))) {
                    String line = in.readLine();
                    while (line != null) {
                        controller.enqueue(line);
                        line = in.readLine();
                    }
                }
                catch (Exception e) {
                    new Alert(AlertType.ERROR,
                            "Unable to open initial script - File Not Found",
                            ButtonType.CANCEL).showAndWait();
                }
            } else {
                new Alert(AlertType.ERROR,
                        "Unable to connect with g2 controller, check configuration.",
                        ButtonType.CANCEL).showAndWait();
            }
        } else {
            // not found
            new Alert(AlertType.ERROR,
                    "Configured serial port not found.",
                    ButtonType.CANCEL).showAndWait();
        }
    }

    /**
     * Cleanup application resources.
     */
    @Override
    public void stop() {
        controller.shutdown();
    }
}
