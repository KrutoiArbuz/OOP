package ru.nsu.masolygin;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для выполнения топологической сортировки графов.
 */
public class TopologicalSorter {

    /**
     * Приватный конструктор для предотвращения создания экземпляров утилитного класса.
     */
    private TopologicalSorter() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Топологическая сортировка с использованием DFS.
     *
     * @param graph граф для сортировки
     * @return список вершин в топологическом порядке
     */
    public static List<Integer> dfsTopologicalSort(Graph graph) {
        List<Integer> result = new ArrayList<>();
        List<Integer> vertices = graph.getVertices();

        boolean[] visited = new boolean[getMaxVertex(vertices) + 1];
        boolean[] inStack = new boolean[getMaxVertex(vertices) + 1];

        for (Integer v : vertices) {
            if (!visited[v]) {
                dfsVisit(graph, v, visited, inStack, result);
            }
        }

        List<Integer> reversed = new ArrayList<>();
        for (int i = result.size() - 1; i >= 0; i--) {
            reversed.add(result.get(i));
        }

        return reversed;
    }

    /**
     * Рекурсивный обход в глубину для DFS топологической сортировки.
     *
     * @param graph граф
     * @param vertex текущая вершина
     * @param visited массив посещенных вершин
     * @param inStack массив вершин в стеке вызовов
     * @param result список результата
     */
    private static void dfsVisit(Graph graph, int vertex, boolean[] visited,
                                boolean[] inStack, List<Integer> result) {
        visited[vertex] = true;
        inStack[vertex] = true;

        List<Integer> neighbors = graph.getNeighbors(vertex);
        for (Integer neighbor : neighbors) {
            if (inStack[neighbor]) {
                throw new IllegalStateException("Graph contains cycle");
            }
            if (!visited[neighbor]) {
                dfsVisit(graph, neighbor, visited, inStack, result);
            }
        }

        inStack[vertex] = false;
        result.add(vertex);
    }

    /**
     * Топологическая сортировка с использованием алгоритма Кана.
     *
     * @param graph граф для сортировки
     * @return список вершин в топологическом порядке
     */
    public static List<Integer> khanTopologicalSort(Graph graph) {
        List<Integer> result = new ArrayList<>();
        List<Integer> vertices = graph.getVertices();

        int maxVertex = getMaxVertex(vertices);
        int[] inDegree = new int[maxVertex + 1];

        for (Integer v : vertices) {
            List<Integer> neighbors = graph.getNeighbors(v);
            for (Integer neighbor : neighbors) {
                inDegree[neighbor]++;
            }
        }

        List<Integer> queue = new ArrayList<>();
        for (Integer v : vertices) {
            if (inDegree[v] == 0) {
                queue.add(v);
            }
        }

        while (!queue.isEmpty()) {
            Integer current = queue.remove(0);
            result.add(current);

            List<Integer> neighbors = graph.getNeighbors(current);
            for (Integer neighbor : neighbors) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (result.size() != vertices.size()) {
            throw new IllegalStateException("Graph contains cycle");
        }

        return result;
    }

    /**
     * Найти максимальный номер вершины в списке.
     *
     * @param vertices список вершин
     * @return максимальный номер вершины
     */
    private static int getMaxVertex(List<Integer> vertices) {
        int max = -1;
        for (Integer v : vertices) {
            if (v > max) {
                max = v;
            }
        }
        return max;
    }
}
