package ru.nsu.masolygin;

import java.util.List;
import ru.nsu.masolygin.strategy.TopologicalSortStrategy;

/**
 * Интерфейс графа с основными операциями.
 */
public interface Graph {
    /**
     * Добавить вершину.
     *
     * @param vertex номер вершины
     */
    void addVertex(int vertex);

    /**
     * Удалить вершину.
     *
     * @param vertex номер вершины
     */
    void removeVertex(int vertex);

    /**
     * Добавить ребро.
     *
     * @param from откуда
     * @param to куда
     */
    void addEdge(int from, int to);

    /**
     * Удалить ребро.
     *
     * @param from откуда
     * @param to куда
     */
    void removeEdge(int from, int to);

    /**
     * Получить список соседей вершины.
     *
     * @param vertex номер вершины
     * @return список соседей
     */
    List<Integer> getNeighbors(int vertex);

    /**
     * Получить список всех вершин.
     *
     * @return список вершин
     */
    List<Integer> getVertices();

    /**
     * Топологическая сортировка.
     *
     * @return список вершин в топологическом порядке
     */
    List<Integer> topologicalSort();

    /**
     * Проверить наличие ребра.
     *
     * @param from откуда
     * @param to куда
     * @return true если есть ребро
     */
    boolean hasEdge(int from, int to);

    /**
     * Получить количество вершин.
     *
     * @return число вершин
     */
    int getVertexCount();

    /**
     * Строковое представление графа.
     *
     * @return строка
     */
    String toString();

    /**
     * Проверка на равенство графов.
     *
     * @param o объект
     * @return true если графы равны
     */
    boolean equals(Object o);

    /**
     * Установить стратегию топологической сортировки.
     *
     * @param strategy стратегия сортировки
     */
    void setTopologicalSortStrategy(TopologicalSortStrategy strategy);
}
