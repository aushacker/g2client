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
import com.github.aushacker.g2client.state.MachineState;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Common attributes shared by all panes in the application.
 *
 * @author Stephen Davies
 * @since August 2019
 */
public abstract class G2Pane <T extends Pane> {

	/**
	 * Top level view object for the subclass.
	 */
	private T pane;

	/**
	 * Allow access to the top-level so that controls like Dialogs
	 * can operate modally.
	 */
	private Stage top;
	
	private IController controller;
	
	private UIPreferences preferences;
	
	public G2Pane(Stage top, IController controller, UIPreferences preferences) {
		this.top = top;
		this.controller = controller;
		this.preferences = preferences;
	}
	
	protected void initialize() {
		initializePane();
		createWidgets();
		layoutWidgets();
		hookEvents();
	}

	/**
	 * Subclassses must create their widgets.
	 */
	protected abstract void createWidgets();	

	protected IController getController() {
		return controller;
	}

	/**
	 * Convenience method.
	 */
	protected MachineState getMachineState() {
		return controller.getMachineState();
	}

	public T getPane() {
		return pane;
	}

	protected UIPreferences getPreferences() {
		return preferences;
	}

	protected Stage getTop() {
		return top;
	}

	/**
	 * Subclasses may not have any UI event they wish to respond to.
	 * Thus they MAY override.
	 */
	protected void hookEvents() {}

	/**
	 * Subclassses must initialize their pane.
	 */
	protected abstract void initializePane();
	
	/**
	 * Subclassses must layout their widgets.
	 */
	protected abstract void layoutWidgets();	

	protected void setPane(T pane) {
		this.pane = pane;
	}
}
