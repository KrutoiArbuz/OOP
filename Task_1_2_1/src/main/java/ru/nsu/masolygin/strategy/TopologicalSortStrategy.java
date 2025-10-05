package ru.nsu.masolygin.strategy;

import ru.nsu.masolygin.Graph;
import java.util.List;

/**
 * Интерфейс стратегии топологической сортировки.
 */
public interface TopologicalSortStrategy {
    /**
     * Выполнить топологическую сортировку графа.
     *
     * @param graph граф для сортировки
     * @return список вершин в топологическом порядке
     * @throws IllegalStateException если граф содержит циклы
     */
    List<Integer> sort(Graph graph);
}
