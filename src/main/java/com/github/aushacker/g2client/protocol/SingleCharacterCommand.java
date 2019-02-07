/*
 * Copyright 2018 Stephen Davies
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
 * @since October 2018
 */
public class SingleCharacterCommand extends Command {

	private SingleCharacterType type;
	
	public SingleCharacterCommand(SingleCharacterType type) {
		this.type = type;
	}

	@Override
	public int compareTo(Command other) {
		if (other instanceof DataCommand) {
			return -1;
		} else {
			return getId() - other.getId();
		}
	}

	public int getPriority() {
		return type.getPriority();
	}
}
