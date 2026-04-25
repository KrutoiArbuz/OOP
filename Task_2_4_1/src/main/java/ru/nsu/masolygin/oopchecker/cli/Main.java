package ru.nsu.masolygin.oopchecker.cli;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Точка входа приложения: парсит конфигурацию курса и выполняет команду test.
 */
public final class Main {

    private static final Path WORK_DIR = Paths.get("work");

    private Main() {
    }

    /**
     * Запускает приложение oop-checker.
     *
     * @param args аргументы команды (test [--text] [--student github])
     * @throws Exception при ошибке выполнения
     */
    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }
        switch (args[0]) {
            case "test" -> {
                boolean ok = new TestCommand(WORK_DIR)
                    .run(Arrays.copyOfRange(args, 1, args.length));
                if (!ok) {
                    System.exit(1);
                }
            }
            default -> {
                System.err.println("[oop-checker] unknown command: " + args[0]);
                printUsage();
                System.exit(1);
            }
        }
    }

    /**
     * Печатает справку по использованию приложения.
     */
    private static void printUsage() {
        System.err.println("Usage: oop-checker <command> [options]");
        System.err.println("Commands:");
        System.err.println("  test [--text] [--student <github>]");
        System.err.println("       --text       plain-text tables (default: HTML)");
        System.err.println("       --student X  check only this student");
    }
}
