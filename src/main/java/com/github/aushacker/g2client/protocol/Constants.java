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
package com.github.aushacker.g2client.protocol;

/**
 * @author Stephen Davies
 * @since March 2019
 */
public class Constants {

	private Constants() {} // Prevent instantiation

	/**
	 * G2 boards with Marlin enabled take a while to get past the STK500 bootloader.
	 */
	public static final int BOARD_RESET_TIME = 2500;

	public static final int NUL = 0;	// ASCII NUL character
	public static final int ENQ = 5;	// ASCII ESC character
	public static final int LF = 10;	// ASCII LF character

	/**
	 * G2 responses have one of the following as the top-level Json value
	 * in the response object.
	 */
	public static final String ACKNOWLEDGEMENT = "ack";
	public static final String FOOTER = "f";
	public static final String RESPONSE = "r";
	public static final String STATUS = "sr";
	
	
}
