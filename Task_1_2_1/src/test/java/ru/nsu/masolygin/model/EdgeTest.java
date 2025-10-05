package ru.nsu.masolygin.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EdgeTest {

    @Test
    void testConstructor() {
        Edge edge = new Edge(1, 2);
        assertEquals(1, edge.getFrom());
        assertEquals(2, edge.getTo());
    }

    @Test
    void testConstructorWithZeroVertices() {
        Edge edge = new Edge(0, 0);
        assertEquals(0, edge.getFrom());
        assertEquals(0, edge.getTo());
    }

    @Test
    void testConstructorWithLargeVertices() {
        Edge edge = new Edge(999999, 888888);
        assertEquals(999999, edge.getFrom());
        assertEquals(888888, edge.getTo());
    }

    @Test
    void testEquals() {
        Edge edge1 = new Edge(1, 2);
        Edge edge2 = new Edge(1, 2);
        Edge edge3 = new Edge(2, 1);
        Edge edge4 = new Edge(1, 3);

        assertTrue(edge1.equals(edge2));
        assertTrue(edge2.equals(edge1));
        assertFalse(edge1.equals(edge3));
        assertFalse(edge1.equals(edge4));
    }

    @Test
    void testEqualsWithSameObject() {
        Edge edge = new Edge(1, 2);
        assertTrue(edge.equals(edge));
    }

    @Test
    void testEqualsWithNull() {
        Edge edge = new Edge(1, 2);
        assertFalse(edge.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        Edge edge = new Edge(1, 2);
        assertFalse(edge.equals("not an edge"));
    }

    @Test
    void testToString() {
        Edge edge = new Edge(1, 2);
        String str = edge.toString();
        assertTrue(str.contains("1"));
        assertTrue(str.contains("2"));
        assertTrue(str.contains("->"));
    }

    @Test
    void testSelfLoop() {
        Edge selfLoop = new Edge(5, 5);
        assertEquals(5, selfLoop.getFrom());
        assertEquals(5, selfLoop.getTo());

        Edge anotherSelfLoop = new Edge(5, 5);
        assertTrue(selfLoop.equals(anotherSelfLoop));
    }
}
