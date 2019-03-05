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
 * Value of the machine 'stat' that is frequently returned in a
 * status report. Not to be confused with the MachineState type.
 *
 * @author Stephen Davies
 * @since March 2019
 */
public enum StatValue {
	INITIALIZING(0, "Initializing"),
	READY(1, "Ready"),
	ALARM(2, "Alarm"),
	PROGRAM_STOP(3, "Stop"),
	PROGRAM_END(4, "End"),
	RUN(5, "Run"),
	HOLD(6, "Hold"),
	PROBE(7, "Probe"),
	CYCLE(8, "CannedCycle"),
	HOMING(9, "Homing"),
	JOG(10, "Jogging"),
	INTERLOCK(11, "Interlock"),
	SHUTDOWN(12, "Shutdown"),
	PANIC(13, "Panic");
	
	/**
	 * Value code in Json data.
	 */
	private final int id;

	private final String description; 
	
	private StatValue(int id, String description) {
		this.id = id;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public int getId() {
		return id;
	}

	private static final StatValue[] byIds;
	
	static {
		byIds = new StatValue[StatValue.values().length];
		for (StatValue v : StatValue.values()) {
			byIds[v.getId()] = v;
		}
	}

	public static StatValue lookupId(int id) {
		return byIds[id];
	}
}
