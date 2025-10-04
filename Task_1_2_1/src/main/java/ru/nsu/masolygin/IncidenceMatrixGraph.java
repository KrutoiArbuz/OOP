package ru.nsu.masolygin;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация графа с помощью матрицы инцидентности.
 */
public class IncidenceMatrixGraph implements Graph {
    private List<Integer> vertices;
    private List<Edge> edges;
    private List<List<Integer>> matrix;

    /**
     * Класс ребра графа.
     */
    private static class Edge {
        int from;
        int to;

        /**
         * Конструктор ребра.
         * @param from начальная вершина
         * @param to конечная вершина
         */
        Edge(int from, int to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Edge)) return false;
            Edge edge = (Edge) o;
            return from == edge.from && to == edge.to;
        }
    }

    /**
     * Конструктор графа.
     */
    public IncidenceMatrixGraph() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        matrix = new ArrayList<>();
    }

    public void addVertex(int vertex) {
        if (vertex < 0) {
            throw new IllegalArgumentException("Vertex must be non-negative");
        }
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
            rebuildMatrix();
        }
    }

    public void removeVertex(int vertex) {
        if (!vertices.contains(vertex)) {
            return;
        }

        edges.removeIf(edge -> edge.from == vertex || edge.to == vertex);
        vertices.remove(Integer.valueOf(vertex));
        rebuildMatrix();
    }

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

    public void removeEdge(int from, int to) {
        Edge edgeToRemove = new Edge(from, to);
        if (edges.remove(edgeToRemove)) {
            rebuildMatrix();
        }
    }

    public List<Integer> getNeighbors(int vertex) {
        if (!vertices.contains(vertex)) {
            throw new IllegalArgumentException("Vertex does not exist in the graph");
        }

        List<Integer> neighbors = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.from == vertex && !neighbors.contains(edge.to)) {
                neighbors.add(edge.to);
            }
        }
        return neighbors;
    }

    public List<Integer> getVertices() {
        return new ArrayList<>(vertices);
    }

    public List<Integer> topologicalSort() {
        return TopologicalSorter.khanTopologicalSort(this);
    }

    public boolean hasEdge(int from, int to) {
        return edges.contains(new Edge(from, to));
    }

    public int getVertexCount() {
        return vertices.size();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IncidenceMatrixGraph:\n");
        sb.append("Vertices: ").append(vertices).append("\n");
        sb.append("Edges:\n");

        for (Edge edge : edges) {
            sb.append("  ").append(edge.from).append(" -> ").append(edge.to).append("\n");
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
                row.add(0);
            }
            matrix.add(row);
        }

        for (int j = 0; j < m; j++) {
            Edge edge = edges.get(j);
            int fromIndex = vertices.indexOf(edge.from);
            int toIndex = vertices.indexOf(edge.to);

            if (fromIndex != -1) {
                matrix.get(fromIndex).set(j, 1);
            }
            if (toIndex != -1) {
                matrix.get(toIndex).set(j, -1);
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
