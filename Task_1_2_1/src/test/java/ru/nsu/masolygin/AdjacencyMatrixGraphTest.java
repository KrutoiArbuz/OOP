package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;


class AdjacencyMatrixGraphTest {
    private AdjacencyMatrixGraph graph;

    @BeforeEach
    void setUp() {
        graph = new AdjacencyMatrixGraph();
    }

    @Test
    void testAddSingleVertex() {
        graph.addVertex(5);
        assertEquals(1, graph.getVertexCount());
        assertTrue(graph.getVertices().contains(5));
    }

    @Test
    void testAddMultipleVertices() {
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        assertEquals(3, graph.getVertexCount());
    }

    @Test
    void testAddDuplicateVertex() {
        graph.addVertex(1);
        graph.addVertex(1);
        assertEquals(1, graph.getVertexCount());
    }

    @Test
    void testAddNegativeVertex() {
        assertThrows(IllegalArgumentException.class, () -> graph.addVertex(-1));
    }

    @Test
    void testRemoveVertex() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.removeVertex(1);
        assertEquals(1, graph.getVertexCount());
        assertFalse(graph.getVertices().contains(1));
    }

    @Test
    void testRemoveNonExistentVertex() {
        graph.addVertex(1);
        graph.removeVertex(99);
        assertEquals(1, graph.getVertexCount());
    }

    @Test
    void testAddEdge() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);
        assertTrue(graph.hasEdge(1, 2));
        assertFalse(graph.hasEdge(2, 1));
    }

    @Test
    void testAddEdgeWithNonExistentVertex() {
        graph.addVertex(1);
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(1, 99));
    }

    @Test
    void testRemoveEdge() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);
        graph.removeEdge(1, 2);
        assertFalse(graph.hasEdge(1, 2));
    }

    @Test
    void testRemoveVertexRemovesEdges() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.removeVertex(2);
        assertFalse(graph.hasEdge(1, 2));
        assertFalse(graph.hasEdge(2, 3));
    }

    @Test
    void testGetNeighbors() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);

        List<Integer> neighbors = graph.getNeighbors(1);
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(2));
        assertTrue(neighbors.contains(3));
    }

    @Test
    void testGetNeighborsEmpty() {
        graph.addVertex(1);
        List<Integer> neighbors = graph.getNeighbors(1);
        assertTrue(neighbors.isEmpty());
    }

    @Test
    void testGetNeighborsNonExistent() {
        assertThrows(IllegalArgumentException.class, () -> graph.getNeighbors(99));
    }

    @Test
    void testTopologicalSortSimple() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);

        List<Integer> sorted = graph.topologicalSort();
        assertTrue(sorted.indexOf(1) < sorted.indexOf(2));
        assertTrue(sorted.indexOf(2) < sorted.indexOf(3));
    }

    @Test
    void testTopologicalSortWithCycle() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);

        assertThrows(IllegalStateException.class, () -> graph.topologicalSort());
    }

    @Test
    void testReadFromFile(@TempDir Path tempDir) throws IOException {
        File testFile = tempDir.resolve("graph.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("0 1 2 3\n");
            writer.write("0 1\n");
            writer.write("1 2\n");
            writer.write("2 3\n");
        }

        graph.readFromFile(testFile.getAbsolutePath());
        assertEquals(4, graph.getVertexCount());
        assertTrue(graph.hasEdge(0, 1));
        assertTrue(graph.hasEdge(1, 2));
        assertTrue(graph.hasEdge(2, 3));
    }

    @Test
    void testToString() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);
        String str = graph.toString();
        assertTrue(str.contains("AdjacencyMatrixGraph"));
        assertTrue(str.contains("1"));
        assertTrue(str.contains("2"));
    }

    @Test
    void testLargeVertexNumbers() {
        graph.addVertex(0);
        graph.addVertex(100);
        graph.addVertex(1000);
        graph.addEdge(0, 100);
        graph.addEdge(100, 1000);

        assertEquals(3, graph.getVertexCount());
        assertTrue(graph.hasEdge(0, 100));
        assertTrue(graph.hasEdge(100, 1000));
    }

    @Test
    void testEquality() {
        AdjacencyMatrixGraph graph2 = new AdjacencyMatrixGraph();

        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);

        graph2.addVertex(1);
        graph2.addVertex(2);
        graph2.addEdge(1, 2);

        assertTrue(graph.equals(graph2));
    }
}
