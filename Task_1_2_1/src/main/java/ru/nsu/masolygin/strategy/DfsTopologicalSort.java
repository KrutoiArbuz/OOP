package ru.nsu.masolygin.strategy;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.masolygin.Graph;

/**
 * Реализация топологической сортировки с использованием DFS.
 */
public class DfsTopologicalSort implements TopologicalSortStrategy {

    /**
     * Создает новый экземпляр стратегии топологической сортировки DFS.
     */
    public DfsTopologicalSort() {
    }

    /**
     * Выполнить топологическую сортировку графа с использованием DFS.
     *
     * @param graph граф для сортировки
     * @return список вершин в топологическом порядке
     * @throws IllegalStateException если граф содержит циклы
     */
    public List<Integer> sort(Graph graph) {
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
    private void dfsVisit(Graph graph, int vertex, boolean[] visited,
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
     * Найти максимальный номер вершины в списке.
     *
     * @param vertices список вершин
     * @return максимальный номер вершины
     */
    private int getMaxVertex(List<Integer> vertices) {
        int max = -1;
        for (Integer v : vertices) {
            if (v > max) {
                max = v;
            }
        }
        return max;
    }
}
