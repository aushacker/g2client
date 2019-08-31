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

import java.io.File;

import com.github.aushacker.g2client.conn.IController;
import com.github.aushacker.g2client.state.SystemState;
import com.github.aushacker.g2client.ui.UIPreferences;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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

	public static void main(String[] args) {
		launch(args);
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

		Tab tab = new Tab();
		tab.setText("Run");
		tabPane.getTabs().add(tab);

		tab = new Tab();
		tab.setText("Config");
		tab.setContent(ConfigPane.create(preferences));
		tabPane.getTabs().add(tab);

		tab = new Tab();
		tab.setText("Diagnostics");
		tab.setContent(SystemStatePane.create(new SystemState()));
		tabPane.getTabs().add(tab);

		return tabPane;
	}

	/**
	 * Handle callback from the File-&gt;Open... menu item.
	 */
	private void fileOpen() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open GCode File");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("GCode Files", "*.cnc", "*.gc", "*.gcode", "*.nc"),
				new ExtensionFilter("All Files", "*.*"));

		File selectedFile = fileChooser.showOpenDialog(mainStage);

		if (selectedFile != null) {
			//mainStage.display(selectedFile);
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

		preferences = new UIPreferences();
		BorderPane root = new BorderPane();

		root.setTop(createMenuPane());
		root.setCenter(createTabPane());

		Scene scene = new Scene(root, preferences.getWidth(), preferences.getHeight());
		mainStage.widthProperty().addListener((o, old, nw) -> preferences.setWidth(nw.intValue()));
		mainStage.heightProperty().addListener((o, old, nw) -> preferences.setHeight(nw.intValue()));

        mainStage.setTitle("G2Client - Gcode Runner");
        mainStage.setScene(scene);
        mainStage.sizeToScene();
        mainStage.show();
	}
}
