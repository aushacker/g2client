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
 * Units and their hard-coded jog increment values.
 * 
 * @author Stephen Davies
 * @since March 2019
 */
public enum Unit {

	INCH(new double[] { 0.0001, 0.001, 0.01, 0.1, 1.0 }),
	
	MM(new double[]{ 0.001, 0.01, 0.1, 1.0, 10.0 });

	private double[] jogIncrements;

	private Unit(double[] jogIncrements) {
		this.jogIncrements = jogIncrements;
	}

	public int getDefaultIndex() {
		return 3;
	}

	public double getIncrement(int index) {
		return jogIncrements[index];
	}

	public int getMaxIndex() {
		return jogIncrements.length - 1;
	}
}
