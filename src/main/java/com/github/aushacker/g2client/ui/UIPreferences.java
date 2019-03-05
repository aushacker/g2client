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
package com.github.aushacker.g2client.ui;

import java.awt.Color;
import java.awt.Font;
import java.util.prefs.Preferences;

/**
 * User configurable UI preferences, things the user can adjust like font
 * sizes and colours.
 *
 * @author Stephen Davies
 * @since March 2019
 */
public class UIPreferences {

	private static final String FONT_NAME = "fontName";
	private static final String DEFAULT_FONT_NAME = "SansSerif";

	/**
	 * Directory containing gcode scripts. 
	 */
	private static final String SCRIPT_HOME = "scriptHome";

	private static final String PORT_NAME = "portName";

	private static Preferences prefs = Preferences.userNodeForPackage(UIPreferences.class);
	
	public Font getDroFont() {
		String fontName = prefs.get(FONT_NAME, DEFAULT_FONT_NAME);

		return new Font(fontName, Font.BOLD, 30);
	}

	public Color getDroBackground() {
		return Color.WHITE;
	}

	public Color getDroErrorBackground() {
		return Color.RED;
	}

	public Color getDroForeground() {
		return Color.BLACK;
	}

	public String getPortName() {
		return prefs.get(PORT_NAME, "");
	}

	public String getScriptHome() {
		return prefs.get(SCRIPT_HOME, "");
	}

	public void setPortName(String portName) {
		prefs.put(PORT_NAME, portName);
	}

	public void setScriptHome(String scriptHome) {
		prefs.put(SCRIPT_HOME, scriptHome);
	}
}
