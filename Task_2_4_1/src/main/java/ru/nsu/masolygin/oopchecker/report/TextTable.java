package ru.nsu.masolygin.oopchecker.report;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ASCII-рендерер таблиц с автоматическим выбором ширины колонок и выравниванием.
 */
public class TextTable {

    private final List<String> headers;
    private final List<List<String>> rows = new ArrayList<>();

    /**
     * Создаёт таблицу с заданными заголовками колонок.
     *
     * @param headers список заголовков
     */
    public TextTable(List<String> headers) {
        Objects.requireNonNull(headers, "headers must not be null");
        this.headers = List.copyOf(headers);
    }

    /**
     * Добавляет строку в таблицу.
     *
     * @param row список значений (должен совпадать по размеру с заголовками)
     * @throws IllegalArgumentException если размер строки не совпадает с количеством колонок
     */
    public void addRow(List<String> row) {
        Objects.requireNonNull(row, "row must not be null");
        if (row.size() != headers.size()) {
            throw new IllegalArgumentException("row size " + row.size()
                + " != headers size " + headers.size());
        }
        rows.add(List.copyOf(row));
    }

    /**
     * Рендерит таблицу в ASCII-формат.
     *
     * @return текстовое представление таблицы с разделителем
     */
    public String render() {
        int[] widths = new int[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            widths[i] = headers.get(i).length();
        }
        for (List<String> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                widths[i] = Math.max(widths[i], row.get(i).length());
            }
        }

        StringBuilder sb = new StringBuilder();
        appendRow(sb, headers, widths);
        appendSeparator(sb, widths);
        for (List<String> row : rows) {
            appendRow(sb, row, widths);
        }
        return sb.toString();
    }

    /**
     * Добавляет строку с выравниванием по ширине колонок.
     *
     * @param sb     StringBuilder для накопления результата
     * @param cells  значения ячеек
     * @param widths ширина каждой колонки
     */
    private void appendRow(StringBuilder sb, List<String> cells, int[] widths) {
        for (int i = 0; i < cells.size(); i++) {
            if (i > 0) {
                sb.append("  ");
            }
            sb.append(padRight(cells.get(i), widths[i]));
        }
        sb.append('\n');
    }

    /**
     * Добавляет разделитель (строка из дефисов).
     *
     * @param sb     StringBuilder для накопления результата
     * @param widths ширина каждой колонки
     */
    private void appendSeparator(StringBuilder sb, int[] widths) {
        for (int i = 0; i < widths.length; i++) {
            if (i > 0) {
                sb.append("  ");
            }
            sb.append("-".repeat(widths[i]));
        }
        sb.append('\n');
    }

    /**
     * Дополняет строку пробелами справа до заданной ширины.
     *
     * @param s     исходная строка
     * @param width требуемая ширина
     * @return строка, дополненная пробелами
     */
    private String padRight(String s, int width) {
        if (s.length() >= width) {
            return s;
        }
        return s + " ".repeat(width - s.length());
    }

}
