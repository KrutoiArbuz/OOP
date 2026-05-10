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
    void renderContainsAllHeaders() {
        String out = table.render();
        assertTrue(out.contains("Имя"));
        assertTrue(out.contains("Баллы"));
        assertTrue(out.contains("Оценка"));
    }

    @Test
    void renderContainsSeparatorRow() {
        String out = table.render();
        assertTrue(out.contains("---"));
    }

    @Test
    void renderEmptyTableHasHeaderAndSeparatorOnly() {
        String[] lines = table.render().split("\n");
        assertEquals(2, lines.length);
    }

    @Test
    void renderWithOneRowHasThreeLines() {
        table.addRow(List.of("Иванов", "10", "отлично"));
        assertEquals(3, table.render().split("\n").length);
    }

    @Test
    void renderIncludesAllRowsInOrder() {
        table.addRow(List.of("Иванов", "10", "отлично"));
        table.addRow(List.of("Петров", "7", "хорошо"));
        String out = table.render();
        assertTrue(out.indexOf("Иванов") < out.indexOf("Петров"));
    }

    @Test
    void columnWidthAdaptsToLongestValue() {
        table.addRow(List.of("Очень длинное имя студента", "5", "хорошо"));
        String[] lines = table.render().split("\n");
        assertEquals(lines[0].length(), lines[1].length());
    }

    @Test
    void allRowsHaveEqualLength() {
        table.addRow(List.of("A", "1", "B"));
        table.addRow(List.of("Очень длинная строка", "1000", "В"));
        String[] lines = table.render().split("\n");
        for (int i = 1; i < lines.length; i++) {
            assertEquals(lines[0].length(), lines[i].length(),
                "row " + i + " has different width");
        }
    }

    @Test
    void addRowWithWrongSizeThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> table.addRow(List.of("одно поле")));
    }

    @Test
    void addRowWithExtraColumnsThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> table.addRow(List.of("a", "b", "c", "d")));
    }

    @Test
    void nullHeadersAreRejected() {
        assertThrows(NullPointerException.class, () -> new TextTable(null));
    }

    @Test
    void nullRowIsRejected() {
        assertThrows(NullPointerException.class, () -> table.addRow(null));
    }

    @Test
    void emptyHeadersTableProducesEmptyLines() {
        TextTable empty = new TextTable(List.of());
        String[] lines = empty.render().split("\n", -1);
        assertEquals("", lines[0]);
    }

    @Test
    void cellShorterThanHeaderIsPaddedWithSpaces() {
        TextTable t = new TextTable(List.of("ОченьШирокийЗаголовок"));
        t.addRow(List.of("a"));
        String[] lines = t.render().split("\n");
        assertEquals(lines[0].length(), lines[2].length());
    }
}
