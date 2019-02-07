/*
 * Copyright 2018 Stephen Davies
 *
 * This file is part of Eagle2nc.
 *
 * Eagle2nc is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Eagle2nc is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Eagle2nc. If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.aushacker.g2client.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Stephen Davies
 * @since October 2018
 */
public class DataCommandTest {

	/**
	 * Object under test.
	 */
	private DataCommand cmd;
	
	@Before
	public void setUp() {
		cmd = new DataCommand("G0 X10 Y20");
	}

	@Test
	public void testEquals() {
		assertFalse(cmd.equals(null));
		assertFalse(cmd.equals("abc"));
		assertTrue(cmd.equals(cmd));
		DataCommand other = new DataCommand("G0 X10 Y20");
		assertFalse(cmd.equals(other));
		assertFalse(other.equals(cmd));
	}

	@Test
	public void testToString() {
		assertEquals("G0 X10 Y20", cmd.toString());
	}
}
