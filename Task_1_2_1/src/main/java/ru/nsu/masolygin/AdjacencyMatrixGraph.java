package ru.nsu.masolygin;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация графа с помощью матрицы смежности.
 */
public class AdjacencyMatrixGraph implements Graph {
    private List<List<Boolean>> matrix;
    private List<Integer> vertices;

    /**
     * Конструктор графа.
     */
    public AdjacencyMatrixGraph() {
        matrix = new ArrayList<>();
        vertices = new ArrayList<>();
    }

    public void addVertex(int vertex) {
        if (vertex < 0) {
            throw new IllegalArgumentException("Vertex must be non-negative");
        }
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
            expandMatrixIfNeeded(vertex);
        }
    }

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

    public void addEdge(int from, int to) {
        if (!vertices.contains(from) || !vertices.contains(to)) {
            throw new IllegalArgumentException("Both vertices must exist in the graph");
        }
        expandMatrixIfNeeded(Math.max(from, to));
        matrix.get(from).set(to, true);
    }

    public void removeEdge(int from, int to) {
        if (from >= 0 && from < matrix.size() && to >= 0 && to < matrix.get(from).size()) {
            matrix.get(from).set(to, false);
        }
    }

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

    public List<Integer> getVertices() {
        return new ArrayList<>(vertices);
    }

    public List<Integer> topologicalSort() {
        return TopologicalSorter.dfsTopologicalSort(this);
    }

    public boolean hasEdge(int from, int to) {
        if (from < 0 || from >= matrix.size() || to < 0 || to >= matrix.get(from).size()) {
            return false;
        }
        return matrix.get(from).get(to);
    }

    public int getVertexCount() {
        return vertices.size();
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Graph)) return false;

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
