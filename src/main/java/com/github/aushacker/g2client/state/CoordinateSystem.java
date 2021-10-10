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

package com.github.aushacker.g2client.state;

/**
 * @author Stephen Davies
 * @since September 2019
 */
public enum CoordinateSystem {
    G53(""),
    G54("1"),
    G55("2"),
    G56("3"),
    G57("4"),
    G58("5"),
    G59("6"),
    G92(""),
    G28(""),
    G30("");

    private CoordinateSystem(String p) {
        this.p = p;
    }

    /**
     * Codes like G10 encode a coordinate system using a numeric value, e.g. P1 = G54.
     */
    private String p;

    public String getP() {
        return p;
    }
}
