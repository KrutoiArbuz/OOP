package ru.nsu.masolygin.strategy;

import ru.nsu.masolygin.Graph;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация топологической сортировки с использованием алгоритма Кана.
 */
public class KhanTopologicalSort implements TopologicalSortStrategy {

    /**
     * Создает новый экземпляр стратегии топологической сортировки Кана.
     */
    public KhanTopologicalSort() {
    }

    /**
     * Выполнить топологическую сортировку графа с использованием алгоритма Кана.
     *
     * @param graph граф для сортировки
     * @return список вершин в топологическом порядке
     * @throws IllegalStateException если граф содержит циклы
     */
    public List<Integer> sort(Graph graph) {
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
