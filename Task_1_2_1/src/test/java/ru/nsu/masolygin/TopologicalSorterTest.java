package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import ru.nsu.masolygin.strategy.DfsTopologicalSort;
import ru.nsu.masolygin.strategy.KhanTopologicalSort;

class TopologicalSorterTest {

    @Test
    void testDfsStrategyWithAdjacencyMatrix() {
        Graph graph = new AdjacencyMatrixGraph(new DfsTopologicalSort());
        setupSimpleGraph(graph);

        List<Integer> sorted = graph.topologicalSort();

        assertEquals(3, sorted.size());
        assertTrue(sorted.indexOf(1) < sorted.indexOf(2));
        assertTrue(sorted.indexOf(2) < sorted.indexOf(3));
    }

    @Test
    void testKhanStrategyWithAdjacencyList() {
        Graph graph = new AdjacencyListGraph(new KhanTopologicalSort());
        setupComplexGraph(graph);

        List<Integer> sorted = graph.topologicalSort();

        assertEquals(6, sorted.size());
        assertTrue(sorted.indexOf(5) < sorted.indexOf(2));
        assertTrue(sorted.indexOf(5) < sorted.indexOf(0));
        assertTrue(sorted.indexOf(4) < sorted.indexOf(0));
        assertTrue(sorted.indexOf(4) < sorted.indexOf(1));
        assertTrue(sorted.indexOf(2) < sorted.indexOf(3));
        assertTrue(sorted.indexOf(3) < sorted.indexOf(1));
    }

    @Test
    void testDfsStrategyWithIncidenceMatrix() {
        Graph graph = new IncidenceMatrixGraph(new DfsTopologicalSort());
        setupSimpleGraph(graph);

        List<Integer> sorted = graph.topologicalSort();

        assertEquals(3, sorted.size());
        assertTrue(sorted.indexOf(1) < sorted.indexOf(2));
        assertTrue(sorted.indexOf(2) < sorted.indexOf(3));
    }

    @Test
    void testStrategySwitching() {
        Graph graph = new AdjacencyMatrixGraph();
        setupSimpleGraph(graph);

        List<Integer> dfsResult = graph.topologicalSort();
        assertEquals(3, dfsResult.size());

        graph.setTopologicalSortStrategy(new KhanTopologicalSort());
        List<Integer> khanResult = graph.topologicalSort();
        assertEquals(3, khanResult.size());

        assertTrue(dfsResult.indexOf(1) < dfsResult.indexOf(2));
        assertTrue(dfsResult.indexOf(2) < dfsResult.indexOf(3));
        assertTrue(khanResult.indexOf(1) < khanResult.indexOf(2));
        assertTrue(khanResult.indexOf(2) < khanResult.indexOf(3));
    }

    @Test
    void testCycleDetectionAcrossStrategies() {
        Graph graph = new AdjacencyListGraph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);

        graph.setTopologicalSortStrategy(new DfsTopologicalSort());
        assertThrows(IllegalStateException.class, () -> graph.topologicalSort());

        graph.setTopologicalSortStrategy(new KhanTopologicalSort());
        assertThrows(IllegalStateException.class, () -> graph.topologicalSort());
    }

    @Test
    void testEmptyGraphAcrossStrategies() {
        Graph graph = new AdjacencyMatrixGraph();

        graph.setTopologicalSortStrategy(new DfsTopologicalSort());
        assertTrue(graph.topologicalSort().isEmpty());

        graph.setTopologicalSortStrategy(new KhanTopologicalSort());
        assertTrue(graph.topologicalSort().isEmpty());
    }

    private void setupSimpleGraph(Graph graph) {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
    }

    private void setupComplexGraph(Graph graph) {
        for (int i = 0; i <= 5; i++) {
            graph.addVertex(i);
        }

        graph.addEdge(5, 2);
        graph.addEdge(5, 0);
        graph.addEdge(4, 0);
        graph.addEdge(4, 1);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);
    }
}
