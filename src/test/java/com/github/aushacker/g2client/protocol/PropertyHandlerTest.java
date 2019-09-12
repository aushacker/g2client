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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonValue;

import org.junit.Before;
import org.junit.Test;

import com.github.aushacker.g2client.state.MachineState;
import com.github.aushacker.g2client.state.Motor;
import com.github.aushacker.g2client.state.SystemState;

/**
 * @author Stephen Davies
 * @since March 2019
 */
public class PropertyHandlerTest {

	/**
	 * Object under test.
	 */
	private PropertyHandler handler;

	private Motor motor;

	@Before
	public void setUp() {
		motor = new Motor(1);
	}

	@Test
	public void testStringOperation() {
		SystemState s = new SystemState();
		handler = new PropertyHandler(s, "firmwareBuildString");

		assertNull(s.getFirmwareBuildString());
		handler.handle(create("1.2"));
		assertEquals("1.2", s.getFirmwareBuildString());
	}

	@Test
	public void testIntOperation() {
		handler = new PropertyHandler(motor, "axis");

		assertEquals(0, motor.getAxis());
		handler.handle(create(2));
		assertEquals(2, motor.getAxis());
	}

	@Test
	public void testBigDecimalOperation() {
		MachineState ms = new MachineState();
		handler = new PropertyHandler(ms, "x");

		assertEquals(new BigDecimal(0), ms.getX());
		handler.handle(create(new BigDecimal(2)));
		assertEquals(new BigDecimal(2), ms.getX());
	}

	/**
	 * None of this can ever work when the target is null.
	 */
	@Test(expected=NullPointerException.class)
	public void testNullTarget() {
		new PropertyHandler(null, "ignored");
	}

	/**
	 * Motor.id is a read-only property. Indicate a hard failure.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testMissingSetter() {
		new PropertyHandler(motor, "id");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMissingProperty() {
		new PropertyHandler(motor, "undefined");
	}

	/**
	 * Utility method to create a JsonBigDecimal.
	 */
	private JsonValue create(BigDecimal d) {
		JsonArray a = Json.createArrayBuilder()
			     .add(d)
			     .build();
		return a.get(0);
	}

	/**
	 * Utility method to create a JsonInt.
	 */
	private JsonValue create(int i) {
		JsonArray a = Json.createArrayBuilder()
			     .add(i)
			     .build();
		return a.get(0);
	}

	/**
	 * Utility method to create a JsonString.
	 */
	private JsonValue create(String s) {
		JsonArray a = Json.createArrayBuilder()
			     .add(s)
			     .build();
		return a.get(0);
	}
}
