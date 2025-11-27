package ru.nsu.masolygin.markdown;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Таблица в Markdown.
 */
public class Table extends Element {
    /** Выравнивание по левому краю. */
    public static final int ALIGN_LEFT = 0;
    /** Выравнивание по центру. */
    public static final int ALIGN_CENTER = 1;
    /** Выравнивание по правому краю. */
    public static final int ALIGN_RIGHT = 2;

    private final List<List<Element>> rows;
    private final int[] alignments;
    private final int rowLimit;

    private Table(List<List<Element>> rows, int[] alignments, int rowLimit) {
        this.rows = new ArrayList<>(rows);
        this.alignments = alignments != null ? alignments.clone() : null;
        this.rowLimit = rowLimit;
    }

    @Override
    public String toMarkdown() {
        if (rows.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        int maxColumns = 0;
        for (List<Element> row : rows) {
            maxColumns = Math.max(maxColumns, row.size());
        }

        int[] columnWidths = new int[maxColumns];
        for (int col = 0; col < maxColumns; col++) {
            for (List<Element> row : rows) {
                if (col < row.size()) {
                    String cellContent = getCellContent(row.get(col));
                    columnWidths[col] = Math.max(columnWidths[col], cellContent.length());
                }
            }
            columnWidths[col] = Math.max(columnWidths[col], 3);
        }

        int actualRowCount = rowLimit > 0 ? Math.min(rows.size(), rowLimit) : rows.size();

        for (int i = 0; i < actualRowCount; i++) {
            List<Element> row = rows.get(i);
            result.append("|");
            for (int col = 0; col < maxColumns; col++) {
                result.append(" ");
                if (col < row.size()) {
                    String cellContent = getCellContent(row.get(col));
                    result.append(padCell(cellContent, columnWidths[col]));
                } else {
                    result.append(" ".repeat(columnWidths[col]));
                }
                result.append(" |");
            }
            result.append("\n");

            if (i == 0) {
                result.append("|");
                for (int col = 0; col < maxColumns; col++) {
                    result.append(" ");
                    int alignment = (alignments != null && col < alignments.length)
                        ? alignments[col] : ALIGN_LEFT;
                    result.append(getAlignmentSeparator(columnWidths[col], alignment));
                    result.append(" |");
                }
                result.append("\n");
            }
        }

        return result.toString().trim();
    }

    private String getCellContent(Element cell) {
        return cell != null ? cell.toMarkdown() : "";
    }

    private String padCell(String content, int width) {
        if (content.length() >= width) {
            return content;
        }
        return content + " ".repeat(width - content.length());
    }

    private String getAlignmentSeparator(int width, int alignment) {
        String dashes = "-".repeat(width);
        switch (alignment) {
            case ALIGN_CENTER:
                return ":" + dashes.substring(2) + ":";
            case ALIGN_RIGHT:
                return dashes.substring(1) + ":";
            default:
                return dashes;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Table table = (Table) obj;
        return rowLimit == table.rowLimit
               && Objects.equals(rows, table.rows)
               && Objects.deepEquals(alignments, table.alignments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows, alignments, rowLimit);
    }

    /**
     * Строитель для создания таблицы.
     */
    public static class Builder {
        private final List<List<Element>> rows = new ArrayList<>();
        private int[] alignments = null;
        private int rowLimit = 0;

        /**
         * Создает новый строитель таблицы.
         */
        public Builder() {
        }

        /**
         * Устанавливает выравнивание столбцов.
         *
         * @param alignments массив значений выравнивания
         * @return строитель для цепочки вызовов
         */
        public Builder withAlignments(int... alignments) {
            this.alignments = alignments.clone();
            return this;
        }

        /**
         * Ограничивает количество строк.
         *
         * @param rowLimit максимальное количество строк
         * @return строитель для цепочки вызовов
         */
        public Builder withRowLimit(int rowLimit) {
            this.rowLimit = rowLimit;
            return this;
        }

        /**
         * Добавляет строку в таблицу.
         * Примитивные типы автоматически конвертируются в Text.
         *
         * @param cells ячейки строки (Element или любой объект для конвертации в Text)
         * @return строитель для цепочки вызовов
         */
        public Builder addRow(Object... cells) {
            List<Element> row = new ArrayList<>();
            for (Object cell : cells) {
                if (cell instanceof Element) {
                    row.add((Element) cell);
                } else {
                    row.add(new Text(cell != null ? cell.toString() : ""));
                }
            }
            rows.add(row);
            return this;
        }

        /**
         * Создает таблицу.
         *
         * @return готовая таблица
         */
        public Table build() {
            if (rows.isEmpty()) {
                throw new IllegalStateException("Table must have at least one row (header)");
            }
            return new Table(rows, alignments, rowLimit);
        }
    }
}