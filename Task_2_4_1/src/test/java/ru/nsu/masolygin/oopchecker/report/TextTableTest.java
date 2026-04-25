package ru.nsu.masolygin.oopchecker.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TextTableTest {

    private TextTable table;

    @BeforeEach
    void setUp() {
        table = new TextTable(List.of("Имя", "Баллы", "Оценка"));
    }

    @Test
    void renderContainsHeaders() {
        String out = table.render();
        assertTrue(out.contains("Имя"));
        assertTrue(out.contains("Баллы"));
        assertTrue(out.contains("Оценка"));
    }

    @Test
    void renderContainsSeparatorLine() {
        String out = table.render();
        assertTrue(out.contains("---"));
    }

    @Test
    void renderContainsAddedRowData() {
        table.addRow(List.of("Иванов", "10", "отлично"));
        String out = table.render();
        assertTrue(out.contains("Иванов"));
        assertTrue(out.contains("10"));
        assertTrue(out.contains("отлично"));
    }

    @Test
    void renderWithNoRowsHasTwoLines() {
        String out = table.render();
        String[] lines = out.split("\n");
        assertEquals(2, lines.length);
    }

    @Test
    void renderWithOneRowHasThreeLines() {
        table.addRow(List.of("Иванов", "10", "отлично"));
        String out = table.render();
        String[] lines = out.split("\n");
        assertEquals(3, lines.length);
    }

    @Test
    void addRowReturnsTableForChaining() {
        TextTable result = table.addRow(List.of("A", "1", "B"));
        assertEquals(table, result);
    }

    @Test
    void addRowWithWrongSizeThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> table.addRow(List.of("только один элемент")));
    }

    @Test
    void columnWidthAdaptsToLongestValue() {
        table.addRow(List.of("Очень длинное имя студента", "5", "хорошо"));
        String out = table.render();
        int headerLineLen = out.split("\n")[0].length();
        int separatorLen = out.split("\n")[1].length();
        assertEquals(headerLineLen, separatorLen);
    }

    @Test
    void multipleRowsAllAppearInOutput() {
        table.addRow(List.of("Иванов", "10", "отлично"));
        table.addRow(List.of("Петров", "7", "хорошо"));
        table.addRow(List.of("Сидоров", "3", "неудовлетворительно"));
        String out = table.render();
        assertTrue(out.contains("Иванов"));
        assertTrue(out.contains("Петров"));
        assertTrue(out.contains("Сидоров"));
    }
}
