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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.PriorityBlockingQueue;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Stephen Davies
 * @since October 2018
 */
public class CommandTest {

    private Command c1;
    private Command c2;
    private Command c3;
    private Command c4;
    
    @Before
    public void setUp() {
        c1 = new DataCommand("G21");
        c2 = SingleCharacterType.RESET.asCommand();
        c3 = new DataCommand("G17");
        c4 = SingleCharacterType.FEEDHOLD.asCommand();
    }

    /**
     * Commands must have a unique sequence number based on their creation order.
     */
    @Test
    public void testCreationSequence() {
        assertTrue(c1.getId() < c2.getId());
    }

    /**
     * compareTo should return 0 when compared to itself.
     */
    @Test
    public void testCompareToSame() {
        assertEquals(0, c1.compareTo(c1));
    }

    /**
     * As c1 is a DataCommand it should compare greater than c2 and c4 but less than
     * c3 (created earlier).
     */
    @Test
    public void testCompareToC1() {
        assertTrue(c1.compareTo(c2) > 0);
        assertTrue(c1.compareTo(c4) > 0);
        assertTrue(c1.compareTo(c3) < 0);
    }

    /**
     * As c2 is a control Command it should compare less than all other commands.
     */
    @Test
    public void testCompareToC2() {
        assertTrue(c2.compareTo(c1) < 0);
        assertTrue(c2.compareTo(c3) < 0);
        assertTrue(c2.compareTo(c4) < 0);
    }

    /**
     * c3 is a DataCommand created after c1. It should compare higher than all other commands.
     */
    @Test
    public void testCompareToC3() {
        assertTrue(c3.compareTo(c1) > 0);
        assertTrue(c3.compareTo(c2) > 0);
        assertTrue(c3.compareTo(c4) > 0);
    }

    /**
     * c4 is the second control Command. It should compare lower than all except c2.
     */
    @Test
    public void testCompareToC4() {
        assertTrue(c4.compareTo(c1) < 0);
        assertTrue(c4.compareTo(c2) > 0);
        assertTrue(c4.compareTo(c3) < 0);
    }

    @Test
    public void testQueuePriority() {
        PriorityBlockingQueue<Command> q = new PriorityBlockingQueue<>();

        q.add(c3);
        q.add(c1);
        q.add(c4);
        q.add(c2);

        assertSame(c2, q.poll());
        assertSame(c4, q.poll());
        assertSame(c1, q.poll());
        assertSame(c3, q.poll());
    }
}
