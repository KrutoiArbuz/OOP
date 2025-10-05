package ru.nsu.masolygin;

import java.io.IOException;
import java.util.List;
import ru.nsu.masolygin.parser.FileGraphReader;
import ru.nsu.masolygin.parser.GraphReader;
import ru.nsu.masolygin.strategy.DfsTopologicalSort;
import ru.nsu.masolygin.strategy.KhanTopologicalSort;

/**
 * Главный класс для демонстрации работы с графом.
 */
public class Main {

    /**
     * Приватный конструктор для предотвращения создания экземпляров утилитного класса.
     */
    private Main() {
    }

    /**
     * Главный метод программы.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        Graph graph = new AdjacencyMatrixGraph(new DfsTopologicalSort());

        try {
            GraphReader fileReader = new FileGraphReader("graph.txt");
            fileReader.readGraph(graph);
            System.out.println("Graph loaded from file");
        } catch (IOException e) {
            System.out.println("File not found, creating graph manually");
            createSampleGraph(graph);
        }

        System.out.println("Vertices: " + graph.getVertices());
        System.out.println("Vertex count: " + graph.getVertexCount());

        System.out.println(graph);

        try {
            List<Integer> sortedDfs = graph.topologicalSort();
            System.out.println("DFS Topological sort: " + sortedDfs);
        } catch (IllegalStateException e) {
            System.out.println("Graph contains cycle (DFS)");
        }

        graph.setTopologicalSortStrategy(new KhanTopologicalSort());
        try {
            List<Integer> sortedKhan = graph.topologicalSort();
            System.out.println("Khan Topological sort: " + sortedKhan);
        } catch (IllegalStateException e) {
            System.out.println("Graph contains cycle (Khan)");
        }

        demonstrateGraphImplementations();
    }

    /**
     * Создает образец графа.
     *
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

    /**
     * Демонстрирует работу разных реализаций графов.
     */
    private static void demonstrateGraphImplementations() {

        Graph matrixGraph = new AdjacencyMatrixGraph(new DfsTopologicalSort());
        setupTestGraph(matrixGraph);
        System.out.println("Matrix Graph:");
        System.out.println(matrixGraph);

        Graph listGraph = new AdjacencyListGraph(new KhanTopologicalSort());
        setupTestGraph(listGraph);
        System.out.println("List Graph:");
        System.out.println(listGraph);

        Graph incidenceGraph = new IncidenceMatrixGraph(new DfsTopologicalSort());
        setupTestGraph(incidenceGraph);
        System.out.println("Incidence Graph:");
        System.out.println(incidenceGraph);

        System.out.println("Matrix equals List: " + matrixGraph.equals(listGraph));
        System.out.println("List equals Incidence: " + listGraph.equals(incidenceGraph));
    }

    /**
     * Настраивает тестовый граф.
     *
     * @param graph граф для настройки
     */
    private static void setupTestGraph(Graph graph) {
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
    }
}
