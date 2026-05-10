package ru.nsu.masolygin.oopchecker.cli;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Точка входа приложения oop-checker.
 */
public class Main {

    /**
     * Запускает приложение oop-checker.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        try {
            new Cli(CliArgs.parse(args)).run();
        } catch (IOException | RuntimeException e) {
            System.err.println("[oop-checker] " + e.getClass().getSimpleName()
                + ": " + e.getMessage());
        }
    }
}
