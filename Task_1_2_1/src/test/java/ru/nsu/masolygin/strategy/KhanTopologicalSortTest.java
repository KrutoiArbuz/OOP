package ru.nsu.masolygin.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.nsu.masolygin.AdjacencyListGraph;
import ru.nsu.masolygin.Graph;

class KhanTopologicalSortTest {
    private KhanTopologicalSort strategy;
    private Graph graph;

    @BeforeEach
    void setUp() {
        strategy = new KhanTopologicalSort();
        graph = new AdjacencyListGraph();
    }

    @Test
    void testSimpleLinearGraph() {
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        List<Integer> result = strategy.sort(graph);

        assertEquals(3, result.size());
        assertTrue(result.indexOf(0) < result.indexOf(1));
        assertTrue(result.indexOf(1) < result.indexOf(2));
    }

    @Test
    void testComplexGraph() {
        for (int i = 0; i <= 5; i++) {
            graph.addVertex(i);
        }

        graph.addEdge(5, 2);
        graph.addEdge(5, 0);
        graph.addEdge(4, 0);
        graph.addEdge(4, 1);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);

        List<Integer> result = strategy.sort(graph);

        assertEquals(6, result.size());
        assertTrue(result.indexOf(5) < result.indexOf(2));
        assertTrue(result.indexOf(5) < result.indexOf(0));
        assertTrue(result.indexOf(4) < result.indexOf(0));
        assertTrue(result.indexOf(4) < result.indexOf(1));
        assertTrue(result.indexOf(2) < result.indexOf(3));
        assertTrue(result.indexOf(3) < result.indexOf(1));
    }

    @Test
    void testGraphWithCycle() {
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 0);

        assertThrows(IllegalStateException.class, () -> strategy.sort(graph));
    }

    @Test
    void testSingleVertex() {
        graph.addVertex(42);

        List<Integer> result = strategy.sort(graph);

        assertEquals(1, result.size());
        assertEquals(42, result.get(0));
    }

    @Test
    void testEmptyGraph() {
        List<Integer> result = strategy.sort(graph);
        assertTrue(result.isEmpty());
    }

    @Test
    void testMultipleStartVertices() {
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);

        List<Integer> result = strategy.sort(graph);

        assertEquals(4, result.size());
        assertTrue(result.indexOf(0) < result.indexOf(2));
        assertTrue(result.indexOf(1) < result.indexOf(3));
    }

    @Test
    void testDisconnectedGraph() {
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(0, 1);
        graph.addEdge(2, 3);

        List<Integer> result = strategy.sort(graph);

        assertEquals(4, result.size());
        assertTrue(result.indexOf(0) < result.indexOf(1));
        assertTrue(result.indexOf(2) < result.indexOf(3));
    }
}
