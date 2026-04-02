package ru.nsu.masolygin.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DirectionTest {

    @Test
    void testDirectionUp() {
        Direction dir = Direction.UP;
        assertNotNull(dir);
        assertEquals(0, dir.getDx());
        assertEquals(-1, dir.getDy());
    }

    @Test
    void testDirectionDown() {
        Direction dir = Direction.DOWN;
        assertNotNull(dir);
        assertEquals(0, dir.getDx());
        assertEquals(1, dir.getDy());
    }

    @Test
    void testDirectionLeft() {
        Direction dir = Direction.LEFT;
        assertNotNull(dir);
        assertEquals(-1, dir.getDx());
        assertEquals(0, dir.getDy());
    }

    @Test
    void testDirectionRight() {
        Direction dir = Direction.RIGHT;
        assertNotNull(dir);
        assertEquals(1, dir.getDx());
        assertEquals(0, dir.getDy());
    }

    @Test
    void testIsOpposite() {
        assertTrue(Direction.UP.isOpposite(Direction.DOWN));
        assertTrue(Direction.LEFT.isOpposite(Direction.RIGHT));
    }

    @Test
    void testIsNotOpposite() {
        assertTrue(!Direction.UP.isOpposite(Direction.LEFT));
        assertTrue(!Direction.RIGHT.isOpposite(Direction.DOWN));
    }
}

