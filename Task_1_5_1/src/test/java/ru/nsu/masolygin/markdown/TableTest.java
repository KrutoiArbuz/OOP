package ru.nsu.masolygin.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TableTest {

    @Test
    void testSimpleTable() {
        Table table = new Table.Builder()
            .addRow("Name", "Age")
            .addRow("Alice", 25)
            .addRow("Bob", 30)
            .build();

        String result = table.toMarkdown();
        assertTrue(result.contains("Name"));
        assertTrue(result.contains("Age"));
        assertTrue(result.contains("Alice"));
        assertTrue(result.contains("Bob"));
    }

    @Test
    void testTableWithRowLimit() {
        Table table = new Table.Builder()
            .withRowLimit(3)
            .addRow("Header", "Value")
            .addRow("Row 1", 1)
            .addRow("Row 2", 2)
            .addRow("Row 3", 3)
            .addRow("Row 4", 4)
            .build();

        String result = table.toMarkdown();
        assertTrue(result.contains("Row 1"));
        assertTrue(result.contains("Row 2"));
        // Row 4 should not be included due to limit
    }

    @Test
    void testTableWithFormattedElements() {
        Table table = new Table.Builder()
            .addRow("Name", "Status")
            .addRow(new Text.Bold("Bold"), new Text.Italic("Italic"))
            .addRow(new Text.Code("code"), new Text.Strikethrough("strike"))
            .build();

        String result = table.toMarkdown();
        assertTrue(result.contains("**Bold**"));
        assertTrue(result.contains("*Italic*"));
        assertTrue(result.contains("`code`"));
        assertTrue(result.contains("~~strike~~"));
    }

    @Test
    void testTableWithMixedTypes() {
        Table table = new Table.Builder()
            .addRow("Column1", "Column2", "Column3")
            .addRow("Text", 123, new Text.Bold("Bold"))
            .build();

        String result = table.toMarkdown();
        assertTrue(result.contains("Text"));
        assertTrue(result.contains("123"));
        assertTrue(result.contains("**Bold**"));
    }

    @Test
    void testEmptyTableThrowsException() {
        assertThrows(IllegalStateException.class, () -> {
            new Table.Builder().build();
        });
    }

    @Test
    void testTableWithOnlyHeader() {
        Table table = new Table.Builder()
            .addRow("Header1", "Header2")
            .build();

        String result = table.toMarkdown();
        assertTrue(result.contains("Header1"));
        assertTrue(result.contains("Header2"));
        assertTrue(result.contains("|"));
        assertTrue(result.contains("---"));
    }

    @Test
    void testTableWithDifferentColumnCounts() {
        Table table = new Table.Builder()
            .addRow("A", "B", "C")
            .addRow("1", "2")
            .addRow("X")
            .build();

        String result = table.toMarkdown();
        assertTrue(result.contains("A"));
        assertTrue(result.contains("B"));
        assertTrue(result.contains("C"));
    }

    @Test
    void testTableWithNullCells() {
        Table table = new Table.Builder()
            .addRow("Header", "Value")
            .addRow(null, "Test")
            .addRow("Test", null)
            .build();

        String result = table.toMarkdown();
        assertTrue(result.contains("Header"));
        assertTrue(result.contains("Test"));
    }

    @Test
    void testTableRightAlignment() {
        Table table = new Table.Builder()
            .withAlignments(Table.ALIGN_RIGHT, Table.ALIGN_RIGHT)
            .addRow("Index", "Random")
            .addRow(1, 5)
            .build();

        String result = table.toMarkdown();
        assertTrue(result.contains("---:"));
    }

    @Test
    void testTableLeftAlignment() {
        Table table = new Table.Builder()
            .withAlignments(Table.ALIGN_LEFT)
            .addRow("Left")
            .addRow("Data")
            .build();

        String result = table.toMarkdown();
        assertTrue(result.contains("---"));
    }

    @Test
    void testTableWithZeroRowLimit() {
        Table table = new Table.Builder()
            .withRowLimit(0)
            .addRow("Header")
            .addRow("Row1")
            .addRow("Row2")
            .build();

        String result = table.toMarkdown();
        // With rowLimit = 0, all rows should be shown
        assertTrue(result.contains("Row1"));
        assertTrue(result.contains("Row2"));
    }

    @Test
    void testEquals() {
        Table table1 = new Table.Builder()
            .addRow("A", "B")
            .addRow("1", "2")
            .build();
        Table table2 = new Table.Builder()
            .addRow("A", "B")
            .addRow("1", "2")
            .build();
        assertEquals(table1, table2);
    }

    @Test
    void testEqualsSameObject() {
        Table table = new Table.Builder()
            .addRow("A", "B")
            .build();
        assertEquals(table, table);
    }

    @Test
    void testEqualsDifferentRows() {
        Table table1 = new Table.Builder()
            .addRow("A", "B")
            .build();
        Table table2 = new Table.Builder()
            .addRow("A", "C")
            .build();
        assertNotEquals(table1, table2);
    }

    @Test
    void testEqualsNull() {
        Table table = new Table.Builder()
            .addRow("A", "B")
            .build();
        assertNotEquals(table, null);
    }

    @Test
    void testEqualsDifferentClass() {
        Table table = new Table.Builder()
            .addRow("A", "B")
            .build();
        assertNotEquals(table, "Table");
    }

    @Test
    void testHashCode() {
        Table table1 = new Table.Builder()
            .addRow("A", "B")
            .build();
        Table table2 = new Table.Builder()
            .addRow("A", "B")
            .build();
        assertEquals(table1.hashCode(), table2.hashCode());
    }

    @Test
    void testToString() {
        Table table = new Table.Builder()
            .addRow("A", "B")
            .build();
        String result = table.toString();
        assertTrue(result.contains("A"));
        assertTrue(result.contains("B"));
    }

    @Test
    void testTableFromTaskExample() {
        Table.Builder tableBuilder = new Table.Builder()
            .withAlignments(Table.ALIGN_RIGHT, Table.ALIGN_LEFT)
            .withRowLimit(8)
            .addRow("Index", "Random");

        for (int i = 1; i <= 5; i++) {
            tableBuilder.addRow(i, new Text.Bold(String.valueOf(i * 2)));
        }

        Table table = tableBuilder.build();
        String result = table.toMarkdown();

        assertTrue(result.contains("Index"));
        assertTrue(result.contains("Random"));
        assertTrue(result.contains("**"));
    }
}

