package ru.nsu.masolygin.parser;

import java.io.IOException;
import ru.nsu.masolygin.Graph;

/**
 * Интерфейс для парсинга графов из различных источников.
 */
public interface GraphReader {
    /**
     * Прочитать граф из источника.
     *
     * @param graph граф для заполнения
     * @throws IOException ошибка ввода-вывода
     */
    void readGraph(Graph graph) throws IOException;
}
