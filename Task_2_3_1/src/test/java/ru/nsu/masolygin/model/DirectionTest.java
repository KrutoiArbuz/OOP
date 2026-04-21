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
        assertTrue(Direction.DOWN.isOpposite(Direction.UP));
        assertTrue(Direction.RIGHT.isOpposite(Direction.LEFT));
    }

    @Test
    void testIsNotOpposite() {
        assertTrue(!Direction.UP.isOpposite(Direction.LEFT));
        assertTrue(!Direction.UP.isOpposite(Direction.RIGHT));
        assertTrue(!Direction.RIGHT.isOpposite(Direction.DOWN));
        assertTrue(!Direction.RIGHT.isOpposite(Direction.UP));
        assertTrue(!Direction.LEFT.isOpposite(Direction.UP));
        assertTrue(!Direction.LEFT.isOpposite(Direction.DOWN));
    }

    @Test
    void testIsNotOppositeSelf() {
        assertTrue(!Direction.UP.isOpposite(Direction.UP));
        assertTrue(!Direction.DOWN.isOpposite(Direction.DOWN));
        assertTrue(!Direction.LEFT.isOpposite(Direction.LEFT));
        assertTrue(!Direction.RIGHT.isOpposite(Direction.RIGHT));
    }

    @Test
    void testAllDirectionsHaveDelta() {
        for (Direction dir : Direction.values()) {
            assertNotNull(dir);
            assertTrue(Math.abs(dir.getDx()) + Math.abs(dir.getDy()) == 1);
        }
    }

    @Test
    void testDirectionValues() {
        Direction[] values = Direction.values();
        assertEquals(4, values.length);
    }

    @Test
    void testDirectionValueOf() {
        assertEquals(Direction.UP, Direction.valueOf("UP"));
        assertEquals(Direction.DOWN, Direction.valueOf("DOWN"));
        assertEquals(Direction.LEFT, Direction.valueOf("LEFT"));
        assertEquals(Direction.RIGHT, Direction.valueOf("RIGHT"));
    }
}

