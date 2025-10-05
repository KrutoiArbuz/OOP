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

import ru.nsu.masolygin.parser.FileGraphReader;
import ru.nsu.masolygin.parser.GraphReader;
import ru.nsu.masolygin.strategy.DfsTopologicalSort;
import ru.nsu.masolygin.strategy.KhanTopologicalSort;

class AdjacencyListGraphTest {
    private AdjacencyListGraph graph;

    @BeforeEach
    void setUp() {
        graph = new AdjacencyListGraph();
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
    void testAddDuplicateEdge() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);
        graph.addEdge(1, 2);
        assertEquals(1, graph.getNeighbors(1).size());
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
    void testRemoveVertexRemovesIncomingEdges() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(3, 2);
        graph.removeVertex(2);

        assertEquals(2, graph.getVertexCount());
        assertFalse(graph.hasEdge(1, 2));
        assertFalse(graph.hasEdge(3, 2));
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
    void testTopologicalSortStrategy() {
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        graph.setTopologicalSortStrategy(new DfsTopologicalSort());
        List<Integer> dfsResult = graph.topologicalSort();
        assertEquals(3, dfsResult.size());
        assertTrue(dfsResult.indexOf(0) < dfsResult.indexOf(1));
        assertTrue(dfsResult.indexOf(1) < dfsResult.indexOf(2));

        graph.setTopologicalSortStrategy(new KhanTopologicalSort());
        List<Integer> khanResult = graph.topologicalSort();
        assertEquals(3, khanResult.size());
        assertTrue(khanResult.indexOf(0) < khanResult.indexOf(1));
        assertTrue(khanResult.indexOf(1) < khanResult.indexOf(2));
    }

    @Test
    void testConstructorWithStrategy() {
        Graph strategyGraph = new AdjacencyListGraph(new KhanTopologicalSort());
        strategyGraph.addVertex(0);
        strategyGraph.addVertex(1);
        strategyGraph.addEdge(0, 1);

        List<Integer> result = strategyGraph.topologicalSort();
        assertEquals(List.of(0, 1), result);
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

        FileGraphReader reader = new FileGraphReader(testFile.getAbsolutePath());
        reader.readGraph(graph);
        assertEquals(4, graph.getVertexCount());
        assertTrue(graph.hasEdge(0, 1));
        assertTrue(graph.hasEdge(1, 2));
        assertTrue(graph.hasEdge(2, 3));
    }

    @Test
    void testReadFromFileWithFileGraphReader(@TempDir Path tempDir) throws IOException {
        File tempFile = tempDir.resolve("test_graph.txt").toFile();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("0 1 2\n");
            writer.write("0 1\n");
            writer.write("1 2\n");
        }

        AdjacencyListGraph fileGraph = new AdjacencyListGraph();
        GraphReader reader = new FileGraphReader(tempFile.getAbsolutePath());
        reader.readGraph(fileGraph);

        assertEquals(3, fileGraph.getVertexCount());
        assertTrue(fileGraph.hasEdge(0, 1));
        assertTrue(fileGraph.hasEdge(1, 2));
        assertFalse(fileGraph.hasEdge(0, 2));
    }

    @Test
    void testToString() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);
        String str = graph.toString();
        assertTrue(str.contains("AdjacencyListGraph"));
        assertTrue(str.contains("1"));
        assertTrue(str.contains("2"));
    }

    @Test
    void testEquality() {
        AdjacencyListGraph graph2 = new AdjacencyListGraph();

        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);

        graph2.addVertex(1);
        graph2.addVertex(2);
        graph2.addEdge(1, 2);

        assertTrue(graph.equals(graph2));
    }

    @Test
    void testEmptyGraph() {
        assertEquals(0, graph.getVertexCount());
        assertTrue(graph.getVertices().isEmpty());
    }
}
