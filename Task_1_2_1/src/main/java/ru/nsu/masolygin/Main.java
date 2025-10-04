package ru.nsu.masolygin;

import java.io.IOException;
import java.util.List;

/**
 * Главный класс для демонстрации работы с графом.
 */
public class Main {
    /**
     * Главный метод программы.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        Graph graph = new AdjacencyMatrixGraph();

        try {
            graph.readFromFile("graph.txt");
            System.out.println("Graph loaded from file");
        } catch (IOException e) {
            System.out.println("File not found, creating graph manually");
            createSampleGraph(graph);
        }

        System.out.println("Vertices: " + graph.getVertices());
        System.out.println("Vertex count: " + graph.getVertexCount());

        System.out.println(graph);

        try {
            List<Integer> sorted = graph.topologicalSort();
            System.out.println("Topological sort: " + sorted);
        } catch (IllegalStateException e) {
            System.out.println("Graph contains cycle");
        }
    }

    /**
     * Создает образец графа.
     * @param graph граф для заполнения
     */
    private static void createSampleGraph(Graph graph) {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 4);
    }
}
