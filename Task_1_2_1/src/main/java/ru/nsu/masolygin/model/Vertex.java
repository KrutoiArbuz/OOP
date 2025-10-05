package ru.nsu.masolygin.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Вершина графа.
 */
public class Vertex {
    private int id;
    private List<Integer> neighbors;

    /**
     * Конструктор вершины.
     *
     * @param id идентификатор вершины
     */
    public Vertex(int id) {
        this.id = id;
        this.neighbors = new ArrayList<>();
    }

    /**
     * Получить идентификатор вершины.
     *
     * @return идентификатор вершины
     */
    public int getId() {
        return id;
    }

    /**
     * Получить список соседей.
     *
     * @return список соседей
     */
    public List<Integer> getNeighbors() {
        return neighbors;
    }

    /**
     * Добавить соседа.
     *
     * @param neighbor идентификатор соседа
     */
    public void addNeighbor(int neighbor) {
        if (!neighbors.contains(neighbor)) {
            neighbors.add(neighbor);
        }
    }

    /**
     * Удалить соседа.
     *
     * @param neighbor идентификатор соседа
     */
    public void removeNeighbor(int neighbor) {
        neighbors.remove(Integer.valueOf(neighbor));
    }
}
