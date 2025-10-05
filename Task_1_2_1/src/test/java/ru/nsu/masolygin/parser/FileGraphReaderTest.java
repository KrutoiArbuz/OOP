package ru.nsu.masolygin.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ru.nsu.masolygin.AdjacencyListGraph;
import ru.nsu.masolygin.Graph;

class FileGraphReaderTest {

    @Test
    void testReadSimpleGraph(@TempDir Path tempDir) throws IOException {
        File testFile = tempDir.resolve("simple_graph.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("0 1 2\n");
            writer.write("0 1\n");
            writer.write("1 2\n");
        }

        Graph graph = new AdjacencyListGraph();
        FileGraphReader reader = new FileGraphReader(testFile.getAbsolutePath());
        reader.readGraph(graph);

        assertEquals(3, graph.getVertexCount());
        assertTrue(graph.getVertices().contains(0));
        assertTrue(graph.getVertices().contains(1));
        assertTrue(graph.getVertices().contains(2));
        assertTrue(graph.hasEdge(0, 1));
        assertTrue(graph.hasEdge(1, 2));
        assertFalse(graph.hasEdge(0, 2));
    }

    @Test
    void testReadComplexGraph(@TempDir Path tempDir) throws IOException {
        File testFile = tempDir.resolve("complex_graph.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("0 1 2 3 4 5\n");
            writer.write("5 2\n");
            writer.write("5 0\n");
            writer.write("4 0\n");
            writer.write("4 1\n");
            writer.write("2 3\n");
            writer.write("3 1\n");
        }

        Graph graph = new AdjacencyListGraph();
        FileGraphReader reader = new FileGraphReader(testFile.getAbsolutePath());
        reader.readGraph(graph);

        assertEquals(6, graph.getVertexCount());
        assertTrue(graph.hasEdge(5, 2));
        assertTrue(graph.hasEdge(5, 0));
        assertTrue(graph.hasEdge(4, 0));
        assertTrue(graph.hasEdge(4, 1));
        assertTrue(graph.hasEdge(2, 3));
        assertTrue(graph.hasEdge(3, 1));
    }

    @Test
    void testReadGraphWithEmptyLines(@TempDir Path tempDir) throws IOException {
        File testFile = tempDir.resolve("graph_with_empty_lines.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("0 1 2\n");
            writer.write("\n");
            writer.write("0 1\n");
            writer.write("\n");
            writer.write("1 2\n");
        }

        Graph graph = new AdjacencyListGraph();
        FileGraphReader reader = new FileGraphReader(testFile.getAbsolutePath());
        reader.readGraph(graph);

        assertEquals(3, graph.getVertexCount());
        assertTrue(graph.hasEdge(0, 1));
        assertTrue(graph.hasEdge(1, 2));
    }

    @Test
    void testReadEmptyFile(@TempDir Path tempDir) throws IOException {
        File testFile = tempDir.resolve("empty_graph.txt").toFile();
        testFile.createNewFile();

        Graph graph = new AdjacencyListGraph();
        FileGraphReader reader = new FileGraphReader(testFile.getAbsolutePath());

        assertThrows(IOException.class, () -> reader.readGraph(graph));
    }

    @Test
    void testReadFileWithInvalidEdgeFormat(@TempDir Path tempDir) throws IOException {
        File testFile = tempDir.resolve("invalid_graph.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("0 1 2\n");
            writer.write("0\n");
        }

        Graph graph = new AdjacencyListGraph();
        FileGraphReader reader = new FileGraphReader(testFile.getAbsolutePath());

        assertThrows(IOException.class, () -> reader.readGraph(graph));
    }

    @Test
    void testReadFileWithTooManyEdgeElements(@TempDir Path tempDir) throws IOException {
        File testFile = tempDir.resolve("invalid_graph2.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("0 1 2\n");
            writer.write("0 1 2\n");
        }

        Graph graph = new AdjacencyListGraph();
        FileGraphReader reader = new FileGraphReader(testFile.getAbsolutePath());

        assertThrows(IOException.class, () -> reader.readGraph(graph));
    }

    @Test
    void testReadNonExistentFile() {
        Graph graph = new AdjacencyListGraph();
        FileGraphReader reader = new FileGraphReader("non_existent_file.txt");

        assertThrows(IOException.class, () -> reader.readGraph(graph));
    }

    @Test
    void testReadGraphWithOnlyVertices(@TempDir Path tempDir) throws IOException {
        File testFile = tempDir.resolve("vertices_only.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("0 1 2 3\n");
        }

        Graph graph = new AdjacencyListGraph();
        FileGraphReader reader = new FileGraphReader(testFile.getAbsolutePath());
        reader.readGraph(graph);

        assertEquals(4, graph.getVertexCount());
        for (int i = 0; i < 4; i++) {
            assertTrue(graph.getVertices().contains(i));
            assertTrue(graph.getNeighbors(i).isEmpty());
        }
    }

    @Test
    void testReadGraphWithLargeVertexNumbers(@TempDir Path tempDir) throws IOException {
        File testFile = tempDir.resolve("large_vertices.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("0 100 1000\n");
            writer.write("0 100\n");
            writer.write("100 1000\n");
        }

        Graph graph = new AdjacencyListGraph();
        FileGraphReader reader = new FileGraphReader(testFile.getAbsolutePath());
        reader.readGraph(graph);

        assertEquals(3, graph.getVertexCount());
        assertTrue(graph.hasEdge(0, 100));
        assertTrue(graph.hasEdge(100, 1000));
    }

    @Test
    void testReadGraphInterface(@TempDir Path tempDir) throws IOException {
        File testFile = tempDir.resolve("interface_test.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("1 2 3\n");
            writer.write("1 2\n");
            writer.write("2 3\n");
        }

        Graph graph = new AdjacencyListGraph();
        GraphReader reader = new FileGraphReader(testFile.getAbsolutePath());
        reader.readGraph(graph);

        assertEquals(3, graph.getVertexCount());
        assertTrue(graph.hasEdge(1, 2));
        assertTrue(graph.hasEdge(2, 3));
    }
}
