package ru.nsu.masolygin.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VertexTest {
    private Vertex vertex;

    @BeforeEach
    void setUp() {
        vertex = new Vertex(1);
    }

    @Test
    void testConstructor() {
        assertEquals(1, vertex.getId());
        assertTrue(vertex.getNeighbors().isEmpty());
    }

    @Test
    void testAddNeighbor() {
        vertex.addNeighbor(2);
        List<Integer> neighbors = vertex.getNeighbors();
        assertEquals(1, neighbors.size());
        assertTrue(neighbors.contains(2));
    }

    @Test
    void testAddMultipleNeighbors() {
        vertex.addNeighbor(2);
        vertex.addNeighbor(3);
        vertex.addNeighbor(4);

        List<Integer> neighbors = vertex.getNeighbors();
        assertEquals(3, neighbors.size());
        assertTrue(neighbors.contains(2));
        assertTrue(neighbors.contains(3));
        assertTrue(neighbors.contains(4));
    }

    @Test
    void testAddDuplicateNeighbor() {
        vertex.addNeighbor(2);
        vertex.addNeighbor(2);

        List<Integer> neighbors = vertex.getNeighbors();
        assertEquals(1, neighbors.size());
        assertTrue(neighbors.contains(2));
    }

    @Test
    void testRemoveNeighbor() {
        vertex.addNeighbor(2);
        vertex.addNeighbor(3);
        vertex.removeNeighbor(2);

        List<Integer> neighbors = vertex.getNeighbors();
        assertEquals(1, neighbors.size());
        assertFalse(neighbors.contains(2));
        assertTrue(neighbors.contains(3));
    }

    @Test
    void testRemoveNonExistentNeighbor() {
        vertex.addNeighbor(2);
        vertex.removeNeighbor(99);

        List<Integer> neighbors = vertex.getNeighbors();
        assertEquals(1, neighbors.size());
        assertTrue(neighbors.contains(2));
    }

    @Test
    void testRemoveFromEmptyNeighbors() {
        vertex.removeNeighbor(1);
        assertTrue(vertex.getNeighbors().isEmpty());
    }

    @Test
    void testVertexWithZeroId() {
        Vertex zeroVertex = new Vertex(0);
        assertEquals(0, zeroVertex.getId());
        assertTrue(zeroVertex.getNeighbors().isEmpty());
    }

    @Test
    void testVertexWithLargeId() {
        Vertex largeVertex = new Vertex(999999);
        assertEquals(999999, largeVertex.getId());
        assertTrue(largeVertex.getNeighbors().isEmpty());
    }
}
