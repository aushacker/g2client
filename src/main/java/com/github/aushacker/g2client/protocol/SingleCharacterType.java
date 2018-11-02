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

/**
 * Encapsulates the various single character commands supported by the G2 protocol.
 *
 * @author Stephen Davies
 * @since October 2018
 */
public enum SingleCharacterType {
	FEEDHOLD('!'),
	RESUME('~'),
	QUEUE_FLUSH('%'),
	KILL_JOB((char) 4),
	RESET((char) 24),
	ENQ((char) 5);
	
	private char command;

	private SingleCharacterType(char command) {
		this.command = command;
	}

	public Command asCommand() {
		return new SingleCharacterCommand(this);
	}

	public char getCommand() {
		return command;
	}

	public int getPriority() {
		return 0;
	}
}
