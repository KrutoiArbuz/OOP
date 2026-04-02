package ru.nsu.masolygin.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class PointTest {

    @Test
    void testPointCreation() {
        Point point = new Point(5, 10);
        assertNotNull(point);
    }

    @Test
    void testGetX() {
        Point point = new Point(5, 10);
        assertEquals(5, point.getX());
    }

    @Test
    void testGetY() {
        Point point = new Point(5, 10);
        assertEquals(10, point.getY());
    }

    @Test
    void testTranslate() {
        Point point = new Point(5, 10);
        Point translated = point.translate(3, 2);
        assertEquals(8, translated.getX());
        assertEquals(12, translated.getY());
    }

    @Test
    void testTranslateNegative() {
        Point point = new Point(5, 10);
        Point translated = point.translate(-2, -3);
        assertEquals(3, translated.getX());
        assertEquals(7, translated.getY());
    }

    @Test
    void testEquals() {
        Point point1 = new Point(5, 10);
        Point point2 = new Point(5, 10);
        assertEquals(point1, point2);
    }

    @Test
    void testNotEquals() {
        Point point1 = new Point(5, 10);
        Point point2 = new Point(5, 11);
        assertNotNull(point1);
        assertNotNull(point2);
    }
}

