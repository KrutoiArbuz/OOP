package ru.nsu.masolygin.parser;

import ru.nsu.masolygin.Graph;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Реализация чтения графа из файла.
 */
public class FileGraphReader implements GraphReader {
    private final String filename;

    /**
     * Конструктор для чтения графа из файла.
     *
     * @param filename имя файла
     */
    public FileGraphReader(String filename) {
        this.filename = filename;
    }

    /**
     * Прочитать граф из файла.
     *
     * @param graph граф для заполнения
     * @throws IOException ошибка ввода-вывода
     */
    public void readGraph(Graph graph) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            if (line == null) {
                throw new IOException("Empty file");
            }

            String[] vertexStrings = line.trim().split("\\s+");
            for (String v : vertexStrings) {
                graph.addVertex(Integer.parseInt(v));
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
                graph.addEdge(from, to);
            }
        }
    }
}
