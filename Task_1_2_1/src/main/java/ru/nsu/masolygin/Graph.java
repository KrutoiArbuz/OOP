package ru.nsu.masolygin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Интерфейс графа с основными операциями.
 */
public interface Graph {
    /**
     * Добавить вершину.
     * @param vertex номер вершины
     */
    void addVertex(int vertex);

    /**
     * Удалить вершину.
     * @param vertex номер вершины
     */
    void removeVertex(int vertex);

    /**
     * Добавить ребро.
     * @param from откуда
     * @param to куда
     */
    void addEdge(int from, int to);

    /**
     * Удалить ребро.
     * @param from откуда
     * @param to куда
     */
    void removeEdge(int from, int to);

    /**
     * Получить список соседей вершины.
     * @param vertex номер вершины
     * @return список соседей
     */
    List<Integer> getNeighbors(int vertex);

    /**
     * Получить список всех вершин.
     * @return список вершин
     */
    List<Integer> getVertices();

    /**
     * Топологическая сортировка.
     * @return список вершин в топологическом порядке
     */
    List<Integer> topologicalSort();

    /**
     * Проверить наличие ребра.
     * @param from откуда
     * @param to куда
     * @return true если есть ребро
     */
    boolean hasEdge(int from, int to);

    /**
     * Получить количество вершин.
     * @return число вершин
     */
    int getVertexCount();

    /**
     * Строковое представление графа.
     * @return строка
     */
    String toString();

    /**
     * Проверка на равенство графов.
     * @param o объект
     * @return true если графы равны
     */
    boolean equals(Object o);

    /**
     * Прочитать граф из файла фиксированного формата.
     * @param filename имя файла
     * @throws IOException ошибка ввода-вывода
     */
    default void readFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            if (line == null) {
                throw new IOException("Empty file");
            }

            String[] vertexStrings = line.trim().split("\\s+");
            for (String v : vertexStrings) {
                addVertex(Integer.parseInt(v));
            }

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\s+");
                if (parts.length != 2) {
                    throw new IOException("Invalid edge format: " + line);
                }
                int from = Integer.parseInt(parts[0]);
                int to = Integer.parseInt(parts[1]);
                addEdge(from, to);
            }
        }
    }
}
