package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;


class TopologicalSorterTest {

    @Test
    void testDfsTopologicalSortSimple() {
        Graph graph = new AdjacencyMatrixGraph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);

        List<Integer> sorted = TopologicalSorter.dfsTopologicalSort(graph);

        assertEquals(3, sorted.size());
        assertTrue(sorted.indexOf(1) < sorted.indexOf(2));
        assertTrue(sorted.indexOf(2) < sorted.indexOf(3));
    }

    @Test
    void testDfsTopologicalSortComplex() {
        Graph graph = new AdjacencyListGraph();

        for (int i = 0; i <= 5; i++) {
            graph.addVertex(i);
        }

        graph.addEdge(5, 2);
        graph.addEdge(5, 0);
        graph.addEdge(4, 0);
        graph.addEdge(4, 1);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);

        List<Integer> sorted = TopologicalSorter.dfsTopologicalSort(graph);

        assertTrue(sorted.indexOf(5) < sorted.indexOf(2));
        assertTrue(sorted.indexOf(5) < sorted.indexOf(0));
        assertTrue(sorted.indexOf(4) < sorted.indexOf(0));
        assertTrue(sorted.indexOf(4) < sorted.indexOf(1));
        assertTrue(sorted.indexOf(2) < sorted.indexOf(3));
        assertTrue(sorted.indexOf(3) < sorted.indexOf(1));
    }

    @Test
    void testDfsTopologicalSortWithCycle() {
        Graph graph = new AdjacencyMatrixGraph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);

        assertThrows(IllegalStateException.class,
            () -> TopologicalSorter.dfsTopologicalSort(graph));
    }

    @Test
    void testDfsTopologicalSortEmpty() {
        Graph graph = new AdjacencyMatrixGraph();
        List<Integer> sorted = TopologicalSorter.dfsTopologicalSort(graph);
        assertTrue(sorted.isEmpty());
    }

    @Test
    void testDfsTopologicalSortSingleVertex() {
        Graph graph = new AdjacencyMatrixGraph();
        graph.addVertex(42);

        List<Integer> sorted = TopologicalSorter.dfsTopologicalSort(graph);
        assertEquals(1, sorted.size());
        assertEquals(42, sorted.get(0));
    }

    @Test
    void testKhanTopologicalSortSimple() {
        Graph graph = new IncidenceMatrixGraph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);

        List<Integer> sorted = TopologicalSorter.khanTopologicalSort(graph);

        assertEquals(3, sorted.size());
        assertTrue(sorted.indexOf(1) < sorted.indexOf(2));
        assertTrue(sorted.indexOf(2) < sorted.indexOf(3));
    }

    @Test
    void testKhanTopologicalSortComplex() {
        Graph graph = new IncidenceMatrixGraph();

        for (int i = 0; i <= 5; i++) {
            graph.addVertex(i);
        }

        graph.addEdge(5, 2);
        graph.addEdge(5, 0);
        graph.addEdge(4, 0);
        graph.addEdge(4, 1);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);

        List<Integer> sorted = TopologicalSorter.khanTopologicalSort(graph);

        assertTrue(sorted.indexOf(5) < sorted.indexOf(2));
        assertTrue(sorted.indexOf(5) < sorted.indexOf(0));
        assertTrue(sorted.indexOf(4) < sorted.indexOf(0));
        assertTrue(sorted.indexOf(4) < sorted.indexOf(1));
        assertTrue(sorted.indexOf(2) < sorted.indexOf(3));
        assertTrue(sorted.indexOf(3) < sorted.indexOf(1));
    }

    @Test
    void testKhanTopologicalSortWithCycle() {
        Graph graph = new IncidenceMatrixGraph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);

        assertThrows(IllegalStateException.class,
            () -> TopologicalSorter.khanTopologicalSort(graph));
    }

    @Test
    void testKhanTopologicalSortEmpty() {
        Graph graph = new IncidenceMatrixGraph();
        List<Integer> sorted = TopologicalSorter.khanTopologicalSort(graph);
        assertTrue(sorted.isEmpty());
    }

    @Test
    void testKhanTopologicalSortSingleVertex() {
        Graph graph = new IncidenceMatrixGraph();
        graph.addVertex(42);

        List<Integer> sorted = TopologicalSorter.khanTopologicalSort(graph);
        assertEquals(1, sorted.size());
        assertEquals(42, sorted.get(0));
    }

    @Test
    void testBothAlgorithmsGiveSameResult() {
        Graph graph1 = new AdjacencyMatrixGraph();
        Graph graph2 = new IncidenceMatrixGraph();

        for (int i = 0; i < 5; i++) {
            graph1.addVertex(i);
            graph2.addVertex(i);
        }

        graph1.addEdge(0, 1);
        graph1.addEdge(0, 2);
        graph1.addEdge(1, 3);
        graph1.addEdge(2, 3);
        graph1.addEdge(3, 4);

        graph2.addEdge(0, 1);
        graph2.addEdge(0, 2);
        graph2.addEdge(1, 3);
        graph2.addEdge(2, 3);
        graph2.addEdge(3, 4);

        List<Integer> sorted1 = TopologicalSorter.dfsTopologicalSort(graph1);
        List<Integer> sorted2 = TopologicalSorter.khanTopologicalSort(graph2);

        assertTrue(sorted1.indexOf(0) < sorted1.indexOf(1));
        assertTrue(sorted1.indexOf(0) < sorted1.indexOf(2));
        assertTrue(sorted1.indexOf(1) < sorted1.indexOf(3));
        assertTrue(sorted1.indexOf(2) < sorted1.indexOf(3));
        assertTrue(sorted1.indexOf(3) < sorted1.indexOf(4));

        assertTrue(sorted2.indexOf(0) < sorted2.indexOf(1));
        assertTrue(sorted2.indexOf(0) < sorted2.indexOf(2));
        assertTrue(sorted2.indexOf(1) < sorted2.indexOf(3));
        assertTrue(sorted2.indexOf(2) < sorted2.indexOf(3));
        assertTrue(sorted2.indexOf(3) < sorted2.indexOf(4));
    }
}
