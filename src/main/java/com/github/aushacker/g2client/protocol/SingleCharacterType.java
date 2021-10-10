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
    RESET((char) 24);
    
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
