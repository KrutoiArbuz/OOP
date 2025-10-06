package ru.nsu.masolygin;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.masolygin.model.Edge;
import ru.nsu.masolygin.strategy.KhanTopologicalSort;
import ru.nsu.masolygin.strategy.TopologicalSortStrategy;

/**
 * Реализация графа с помощью матрицы инцидентности.
 */
public class IncidenceMatrixGraph implements Graph {
    private List<Integer> vertices;
    private List<Edge> edges;
    private List<List<Integer>> matrix;
    private TopologicalSortStrategy topologicalSortStrategy;

    /**
     * Конструктор графа.
     */
    public IncidenceMatrixGraph() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        matrix = new ArrayList<>();
        topologicalSortStrategy = new KhanTopologicalSort();
    }

    /**
     * Конструктор графа со стратегией топологической сортировки.
     *
     * @param strategy стратегия топологической сортировки
     */
    public IncidenceMatrixGraph(TopologicalSortStrategy strategy) {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        matrix = new ArrayList<>();
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
            rebuildMatrix();
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

        edges.removeIf(edge -> edge.getFrom() == vertex || edge.getTo() == vertex);
        vertices.remove(Integer.valueOf(vertex));
        rebuildMatrix();
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

        Edge newEdge = new Edge(from, to);
        if (!edges.contains(newEdge)) {
            edges.add(newEdge);
            rebuildMatrix();
        }
    }

    /**
     * Удалить ребро.
     *
     * @param from откуда
     * @param to куда
     */
    public void removeEdge(int from, int to) {
        Edge edgeToRemove = new Edge(from, to);
        if (edges.remove(edgeToRemove)) {
            rebuildMatrix();
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
        for (Edge edge : edges) {
            if (edge.getFrom() == vertex && !neighbors.contains(edge.getTo())) {
                neighbors.add(edge.getTo());
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
        return edges.contains(new Edge(from, to));
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
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IncidenceMatrixGraph:\n");
        sb.append("Vertices: ").append(vertices).append("\n");
        sb.append("Edges:\n");

        for (Edge edge : edges) {
            sb.append("  ").append(edge.getFrom()).append(" -> ").append(edge.getTo()).append("\n");
        }

        return sb.toString();
    }

    /**
     * Перестроить матрицу инцидентности.
     */
    private void rebuildMatrix() {
        int n = vertices.size();
        int m = edges.size();

        matrix.clear();

        for (int i = 0; i < n; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < m; j++) {
                Edge edge = edges.get(j);
                int vertexId = vertices.get(i);
                if (edge.getFrom() == vertexId) {
                    row.add(1);
                } else if (edge.getTo() == vertexId) {
                    row.add(-1);
                } else {
                    row.add(0);
                }
            }
            matrix.add(row);
        }
    }

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
     * Топологическая сортировка.
     *
     * @return список вершин в топологическом порядке
     */
    public List<Integer> topologicalSort() {
        return topologicalSortStrategy.sort(this);
    }

    /**
     * Установить стратегию топологической сортировки.
     *
     * @param strategy стратегия сортировки
     */
    public void setTopologicalSortStrategy(TopologicalSortStrategy strategy) {
        this.topologicalSortStrategy = strategy;
    }

}
