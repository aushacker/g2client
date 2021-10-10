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

package com.github.aushacker.g2client.pcb;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Stephen Davies
 * @since August 2019
 */
public class TestExcellon {

    /**
     * Object under test.
     */
    private Excellon excellon;
    
    @Before
    public void setUp() {
        excellon = new Excellon();
    }
    
    @Test
    public void testInchLeadingZero() {
        Excellon.Units units = Excellon.Units.INCH;
        
        assertEquals(new BigDecimal("7.0"), units.toDecimal("07"));
        assertEquals(new BigDecimal("7.5"), units.toDecimal("075"));
        assertEquals(new BigDecimal("0.75"), units.toDecimal("0075"));
        assertEquals(new BigDecimal("0.075"), units.toDecimal("00075"));
        assertEquals(new BigDecimal("0.0075"), units.toDecimal("000075"));
    }
    
    @Test
    public void testInchTrailingZero() {
        Excellon.Units units = Excellon.Units.INCH;
        
        assertEquals(new BigDecimal("0.075"), units.toDecimal("750"));
        assertEquals(new BigDecimal("0.75"), units.toDecimal("7500"));
        assertEquals(new BigDecimal("7.5"), units.toDecimal("75000"));
        assertEquals(new BigDecimal("75"), units.toDecimal("750000"));
    }
    
    @Test
    public void testInchDecimal() {
        Excellon.Units units = Excellon.Units.INCH;
        
        assertEquals(new BigDecimal("75"), units.toDecimal("75.0"));
        assertEquals(new BigDecimal("7.5"), units.toDecimal("7.5"));
        assertEquals(new BigDecimal("0.75"), units.toDecimal(".75"));
        assertEquals(new BigDecimal("0.075"), units.toDecimal(".075"));
        assertEquals(new BigDecimal("0.0075"), units.toDecimal(".0075"));
    }

    @Test
    public void testIsPoint() {
        
    }
}
