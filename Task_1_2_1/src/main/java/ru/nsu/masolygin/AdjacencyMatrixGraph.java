package ru.nsu.masolygin;

import ru.nsu.masolygin.strategy.TopologicalSortStrategy;
import ru.nsu.masolygin.strategy.DfsTopologicalSort;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация графа с помощью матрицы смежности.
 */
public class AdjacencyMatrixGraph implements Graph {
    private List<List<Boolean>> matrix;
    private List<Integer> vertices;
    private TopologicalSortStrategy topologicalSortStrategy;

    /**
     * Конструктор графа.
     */
    public AdjacencyMatrixGraph() {
        matrix = new ArrayList<>();
        vertices = new ArrayList<>();
        topologicalSortStrategy = new DfsTopologicalSort();
    }

    /**
     * Конструктор графа со стратегией топологической сортировки.
     *
     * @param strategy стратегия топологической сортировки
     */
    public AdjacencyMatrixGraph(TopologicalSortStrategy strategy) {
        matrix = new ArrayList<>();
        vertices = new ArrayList<>();
        topologicalSortStrategy = strategy;
    }

    /**
     * Добавить вершину.
     *
     * @param vertex номер вершины
     */
    public void addVertex(int vertex) {
        if (vertex < 0) {
            throw new IllegalArgumentException("Vertex must be non-negative");
        }
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
            expandMatrixIfNeeded(vertex);
        }
    }

    /**
     * Удалить вершину.
     *
     * @param vertex номер вершины
     */
    public void removeVertex(int vertex) {
        if (!vertices.contains(vertex)) {
            return;
        }
        vertices.remove(Integer.valueOf(vertex));

        if (vertex < matrix.size()) {
            for (int i = 0; i < matrix.get(vertex).size(); i++) {
                matrix.get(vertex).set(i, false);
            }
            for (int i = 0; i < matrix.size(); i++) {
                if (i < matrix.size() && vertex < matrix.get(i).size()) {
                    matrix.get(i).set(vertex, false);
                }
            }
        }
    }

    /**
     * Добавить ребро.
     *
     * @param from откуда
     * @param to куда
     */
    public void addEdge(int from, int to) {
        if (!vertices.contains(from) || !vertices.contains(to)) {
            throw new IllegalArgumentException("Both vertices must exist in the graph");
        }
        expandMatrixIfNeeded(Math.max(from, to));
        matrix.get(from).set(to, true);
    }

    /**
     * Удалить ребро.
     *
     * @param from откуда
     * @param to куда
     */
    public void removeEdge(int from, int to) {
        if (from >= 0 && from < matrix.size() && to >= 0 && to < matrix.get(from).size()) {
            matrix.get(from).set(to, false);
        }
    }

    /**
     * Получить список соседей вершины.
     *
     * @param vertex номер вершины
     * @return список соседей
     */
    public List<Integer> getNeighbors(int vertex) {
        if (!vertices.contains(vertex)) {
            throw new IllegalArgumentException("Vertex does not exist in the graph");
        }
        List<Integer> neighbors = new ArrayList<>();
        if (vertex < matrix.size()) {
            for (int i = 0; i < matrix.get(vertex).size(); i++) {
                if (matrix.get(vertex).get(i) && vertices.contains(i)) {
                    neighbors.add(i);
                }
            }
        }
        return neighbors;
    }

    /**
     * Получить список всех вершин.
     *
     * @return список вершин
     */
    public List<Integer> getVertices() {
        return new ArrayList<>(vertices);
    }

    /**
     * Проверить наличие ребра.
     *
     * @param from откуда
     * @param to куда
     * @return true если есть ребро
     */
    public boolean hasEdge(int from, int to) {
        if (from < 0 || from >= matrix.size() || to < 0 || to >= matrix.get(from).size()) {
            return false;
        }
        return matrix.get(from).get(to);
    }

    /**
     * Получить количество вершин.
     *
     * @return число вершин
     */
    public int getVertexCount() {
        return vertices.size();
    }

    /**
     * Строковое представление графа.
     *
     * @return строка
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AdjacencyMatrixGraph:\n");
        sb.append("Vertices: ").append(vertices).append("\n");
        sb.append("Edges:\n");

        for (Integer from : vertices) {
            for (Integer to : vertices) {
                if (hasEdge(from, to)) {
                    sb.append("  ").append(from).append(" -> ").append(to).append("\n");
                }
            }
        }

        return sb.toString();
    }

    /**
     * Расширить матрицу при необходимости.
     *
     * @param maxIndex максимальный индекс
     */
    private void expandMatrixIfNeeded(int maxIndex) {
        while (matrix.size() <= maxIndex) {
            matrix.add(new ArrayList<>());
        }

        for (List<Boolean> row : matrix) {
            while (row.size() <= maxIndex) {
                row.add(false);
            }
        }
    }

    /**
     * Проверка на равенство графов.
     *
     * @param o объект
     * @return true если графы равны
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Graph)) {
            return false;
        }

        Graph other = (Graph) o;

        List<Integer> thisVertices = this.getVertices();
        List<Integer> otherVertices = other.getVertices();
        thisVertices.sort(Integer::compareTo);
        otherVertices.sort(Integer::compareTo);

        if (!thisVertices.equals(otherVertices)) {
            return false;
        }

        for (Integer vertex : thisVertices) {
            List<Integer> thisNeighbors = this.getNeighbors(vertex);
            List<Integer> otherNeighbors = other.getNeighbors(vertex);
            thisNeighbors.sort(Integer::compareTo);
            otherNeighbors.sort(Integer::compareTo);
            if (!thisNeighbors.equals(otherNeighbors)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Топологическая сортировка (DFS).
     *
     * @return список вершин в топологическом порядке
     */
    public List<Integer> topologicalSort() {
        return topologicalSortStrategy.sort(this);
    }

    /**
     * Установить стратегию топологической сортировки.
     *
     * @param strategy стратегия топологической сортировки
     */
    public void setTopologicalSortStrategy(TopologicalSortStrategy strategy) {
        this.topologicalSortStrategy = strategy;
    }

}
