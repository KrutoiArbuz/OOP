package ru.nsu.masolygin;

import ru.nsu.masolygin.markdown.Table;
import ru.nsu.masolygin.markdown.Text;

/**
 * Демонстрация библиотеки генерации Markdown.
 */
public class Main {

    /**
     * Приватный конструктор для утилитарного класса.
     */
    private Main() {
    }

    /**
     * Точка входа в программу.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(Table.ALIGN_CENTER, Table.ALIGN_LEFT)
                .withRowLimit(8)
                .addRow("Index", "Random");

        for (int i = 1; i <= 5; i++) {
            final var value = (int) (Math.random() * 10);
            if (value > 5) {
                tableBuilder.addRow(i, new Text.Bold(String.valueOf(value)));
            } else {
                tableBuilder.addRow(i, (int) (Math.random() * 10));
            }
        }

        System.out.println(tableBuilder.build());
    }
}
