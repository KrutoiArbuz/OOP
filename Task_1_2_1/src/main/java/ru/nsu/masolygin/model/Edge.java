package ru.nsu.masolygin.model;

/**
 * Ребро графа.
 */
public class Edge {
    private int from;
    private int to;

    /**
     * Конструктор ребра.
     *
     * @param from начальная вершина
     * @param to конечная вершина
     */
    public Edge(int from, int to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Получить начальную вершину.
     *
     * @return начальная вершина
     */
    public int getFrom() {
        return from;
    }

    /**
     * Получить конечную вершину.
     *
     * @return конечная вершина
     */
    public int getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Edge)) {
            return false;
        }
        Edge edge = (Edge) o;
        return from == edge.from && to == edge.to;
    }

    @Override
    public String toString() {
        return from + " -> " + to;
    }
}
