package ru.nsu.masolygin.oopchecker.cli;

import java.io.IOException;

/**
 * Роутер команд: принимает разобранные аргументы и запускает нужную команду.
 */
public class Cli {

    private final CliArgs args;

    /**
     * Создаёт роутер с разобранными аргументами командной строки.
     *
     * @param args аргументы командной строки
     */
    public Cli(CliArgs args) {
        this.args = args;
    }

    /**
     * Маршрутизирует выполнение по имени команды.
     *
     * @throws IOException при ошибке ввода-вывода внутри команды
     */
    public void run() throws IOException {
        if ("test".equals(args.command())) {
            new TestCommand(args).run();
        } else {
            if (!args.command().isBlank()) {
                System.err.println("[oop-checker] unknown command: " + args.command());
            }
            printUsage();
        }
    }

    private void printUsage() {
        System.err.println("Usage: oop-checker [options] <command> [command-options]");
        System.err.println("Options:");
        System.err.println("  --dir=<path>           рабочая директория (по умолчанию: work)");
        System.err.println(
            "  --config=<path>        путь к конфигу (по умолчанию: config/oopchecker.groovy)");
        System.err.println("Commands:");
        System.err.println("  test [--text] [--student=<github>] [--out=<file>]");
        System.err.println("       --text               текстовый отчёт (по умолчанию: HTML)");
        System.err.println("       --student=<github>   проверить только этого студента");
        System.err.println("       --out=<file>         записать отчёт в файл (UTF-8)");
    }
}
