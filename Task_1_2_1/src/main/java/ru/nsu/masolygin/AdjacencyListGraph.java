package ru.nsu.masolygin;

import ru.nsu.masolygin.model.Vertex;
import ru.nsu.masolygin.strategy.TopologicalSortStrategy;
import ru.nsu.masolygin.strategy.DfsTopologicalSort;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация графа с помощью списка смежности.
 */
public class AdjacencyListGraph implements Graph {
    private List<Vertex> vertices;
    private TopologicalSortStrategy topologicalSortStrategy;

    /**
     * Конструктор графа.
     */
    public AdjacencyListGraph() {
        vertices = new ArrayList<>();
        topologicalSortStrategy = new DfsTopologicalSort();
    }

    /**
     * Конструктор графа со стратегией топологической сортировки.
     *
     * @param strategy стратегия топологической сортировки
     */
    public AdjacencyListGraph(TopologicalSortStrategy strategy) {
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
            otherVertex.removeNeighbor(vertex);
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

        fromVertex.addNeighbor(to);
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
            fromVertex.removeNeighbor(to);
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
        return new ArrayList<>(v.getNeighbors());
    }

    /**
     * Получить список всех вершин.
     *
     * @return список вершин
     */
    public List<Integer> getVertices() {
        List<Integer> result = new ArrayList<>();
        for (Vertex v : vertices) {
            result.add(v.getId());
        }
        return result;
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
        return fromVertex.getNeighbors().contains(to);
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
        sb.append("AdjacencyListGraph:\n");
        sb.append("Vertices: ").append(getVertices()).append("\n");
        sb.append("Edges:\n");

        for (Vertex v : vertices) {
            for (Integer neighbor : v.getNeighbors()) {
                sb.append("  ").append(v.getId()).append(" -> ").append(neighbor).append("\n");
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
            if (v.getId() == id) {
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AdjacencyListGraph that = (AdjacencyListGraph) o;

        List<Integer> thisVertices = this.getVertices();
        List<Integer> thatVertices = that.getVertices();

        if (!thisVertices.equals(thatVertices)) {
            return false;
        }

        for (Integer vertex : thisVertices) {
            List<Integer> thisNeighbors = this.getNeighbors(vertex);
            List<Integer> thatNeighbors = that.getNeighbors(vertex);
            thisNeighbors.sort(Integer::compareTo);
            thatNeighbors.sort(Integer::compareTo);
            if (!thisNeighbors.equals(thatNeighbors)) {
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
