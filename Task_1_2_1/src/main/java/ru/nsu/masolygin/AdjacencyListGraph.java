package ru.nsu.masolygin;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация графа с помощью списка смежности.
 */
public class AdjacencyListGraph implements Graph {

    /**
     * Вершина графа.
     */
    private static class Vertex {
        int id;
        List<Integer> neighbors;

        /**
         * Конструктор вершины.
         *
         * @param id идентификатор вершины
         */
        Vertex(int id) {
            this.id = id;
            this.neighbors = new ArrayList<>();
        }
    }

    private List<Vertex> vertices;

    /**
     * Конструктор графа.
     */
    public AdjacencyListGraph() {
        vertices = new ArrayList<>();
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
        if (findVertex(vertex) == null) {
            vertices.add(new Vertex(vertex));
        }
    }

    /**
     * Удалить вершину.
     *
     * @param vertex номер вершины
     */
    public void removeVertex(int vertex) {
        Vertex v = findVertex(vertex);
        if (v == null) {
            return;
        }

        for (Vertex otherVertex : vertices) {
            otherVertex.neighbors.remove(Integer.valueOf(vertex));
        }

        vertices.remove(v);
    }

    /**
     * Добавить ребро.
     *
     * @param from откуда
     * @param to куда
     */
    public void addEdge(int from, int to) {
        Vertex fromVertex = findVertex(from);
        Vertex toVertex = findVertex(to);

        if (fromVertex == null || toVertex == null) {
            throw new IllegalArgumentException("Both vertices must exist in the graph");
        }

        if (!fromVertex.neighbors.contains(to)) {
            fromVertex.neighbors.add(to);
        }
    }

    /**
     * Удалить ребро.
     *
     * @param from откуда
     * @param to куда
     */
    public void removeEdge(int from, int to) {
        Vertex fromVertex = findVertex(from);
        if (fromVertex != null) {
            fromVertex.neighbors.remove(Integer.valueOf(to));
        }
    }

    /**
     * Получить список соседей вершины.
     *
     * @param vertex номер вершины
     * @return список соседей
     */
    public List<Integer> getNeighbors(int vertex) {
        Vertex v = findVertex(vertex);
        if (v == null) {
            throw new IllegalArgumentException("Vertex does not exist in the graph");
        }
        return new ArrayList<>(v.neighbors);
    }

    /**
     * Получить список всех вершин.
     *
     * @return список вершин
     */
    public List<Integer> getVertices() {
        List<Integer> result = new ArrayList<>();
        for (Vertex v : vertices) {
            result.add(v.id);
        }
        return result;
    }

    /**
     * Топологическая сортировка (DFS).
     *
     * @return список вершин в топологическом порядке
     */
    public List<Integer> topologicalSort() {
        return TopologicalSorter.dfsTopologicalSort(this);
    }

    /**
     * Проверить наличие ребра.
     *
     * @param from откуда
     * @param to куда
     * @return true если есть ребро
     */
    public boolean hasEdge(int from, int to) {
        Vertex fromVertex = findVertex(from);
        if (fromVertex == null) {
            return false;
        }
        return fromVertex.neighbors.contains(to);
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
        sb.append("AdjacencyListGraph:\n");
        sb.append("Vertices: ").append(getVertices()).append("\n");
        sb.append("Edges:\n");

        for (Vertex v : vertices) {
            for (Integer neighbor : v.neighbors) {
                sb.append("  ").append(v.id).append(" -> ").append(neighbor).append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * Найти вершину по идентификатору.
     *
     * @param id номер вершины
     * @return вершина или null если не найдена
     */
    private Vertex findVertex(int id) {
        for (Vertex v : vertices) {
            if (v.id == id) {
                return v;
            }
        }
        return null;
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

        if (this.getVertexCount() != other.getVertexCount()) {
            return false;
        }

        List<Integer> thisVertices = this.getVertices();
        List<Integer> otherVertices = other.getVertices();

        for (Integer v : thisVertices) {
            if (!otherVertices.contains(v)) {
                return false;
            }
        }

        for (Integer from : thisVertices) {
            for (Integer to : thisVertices) {
                if (this.hasEdge(from, to) != other.hasEdge(from, to)) {
                    return false;
                }
            }
        }

        return true;
    }
}
