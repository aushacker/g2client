/*
 * */

package com.github.aushacker.g2client.conn;

import com.github.aushacker.g2client.jfx.UIPreferences;

/**
 * @author Stephen Davies
 * @since March 2019
 */
public class ResetDefaultSerialPort {

	public static void main(String[] args) {
		UIPreferences prefs = new UIPreferences();
		
		prefs.setPortName(OperatingSystem.current().getFilteredPorts().get(0).getSystemPortName());
	}

}
