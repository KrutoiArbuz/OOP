package ru.nsu.masolygin.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
    void testTranslateZero() {
        Point point = new Point(5, 10);
        Point translated = point.translate(0, 0);
        assertEquals(5, translated.getX());
        assertEquals(10, translated.getY());
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
        assertNotEquals(point1, point2);
    }

    @Test
    void testNotEqualsDifferentX() {
        Point point1 = new Point(5, 10);
        Point point2 = new Point(6, 10);
        assertNotEquals(point1, point2);
    }

    @Test
    void testHashCode() {
        Point point1 = new Point(5, 10);
        Point point2 = new Point(5, 10);
        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    void testHashCodeDifferent() {
        Point point1 = new Point(5, 10);
        Point point2 = new Point(5, 11);
        assertNotEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    void testToString() {
        Point point = new Point(5, 10);
        assertNotNull(point.toString());
    }

    @Test
    void testTranslateMultiple() {
        Point point = new Point(0, 0);
        Point translated = point.translate(1, 1).translate(2, 3).translate(-1, 0);
        assertEquals(2, translated.getX());
        assertEquals(4, translated.getY());
    }

    @Test
    void testPointWithNegativeCoordinates() {
        Point point = new Point(-5, -10);
        assertEquals(-5, point.getX());
        assertEquals(-10, point.getY());
    }

    @Test
    void testPointWithZeroCoordinates() {
        Point point = new Point(0, 0);
        assertEquals(0, point.getX());
        assertEquals(0, point.getY());
    }

    @Test
    void testPointLargeCoordinates() {
        Point point = new Point(1000, 2000);
        assertEquals(1000, point.getX());
        assertEquals(2000, point.getY());
    }
}

