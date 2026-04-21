package ru.nsu.masolygin.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class PlaceableTest {

    @Test
    void testPlaceableImplementation() {
        Point position = new Point(5, 10);
        Placeable placeable = new TestPlaceable(position);
        assertNotNull(placeable);
    }

    @Test
    void testGetPosition() {
        Point position = new Point(5, 10);
        Placeable placeable = new TestPlaceable(position);
        assertEquals(position, placeable.getPosition());
    }

    @Test
    void testPositionCoordinates() {
        Point position = new Point(3, 7);
        Placeable placeable = new TestPlaceable(position);
        assertEquals(3, placeable.getPosition().getX());
        assertEquals(7, placeable.getPosition().getY());
    }

    private static class TestPlaceable implements Placeable {

        private final Point position;

        TestPlaceable(Point position) {
            this.position = position;
        }

        @Override
        public Point getPosition() {
            return position;
        }
    }
}

