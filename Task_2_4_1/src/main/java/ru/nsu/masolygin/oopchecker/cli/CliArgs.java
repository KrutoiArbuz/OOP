package ru.nsu.masolygin.oopchecker.cli;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Разобранные аргументы командной строки.
 *
 * @param command       имя команды (например, {@code test})
 * @param workDir       рабочая директория для репозиториев
 * @param configFile    путь к groovy-конфигу курса
 * @param textOutput    true — текстовый отчёт, false — HTML
 * @param studentFilter GitHub ник для фильтра по студенту; null означает всех
 */
public record CliArgs(
    String command,
    Path workDir,
    Path configFile,
    boolean textOutput,
    String studentFilter,
    Path outputFile
) {

    static final Path DEFAULT_WORK_DIR = Paths.get("work");
    static final Path DEFAULT_CONFIG_FILE = Paths.get("config/oopchecker.groovy");

    /**
     * Разбирает аргументы командной строки в объект {@code CliArgs}.
     *
     * @param args аргументы из {@code main(String[])}
     * @return разобранные аргументы
     */
    public static CliArgs parse(String[] args) {
        Path workDir = Flag.DIR.valueOf(args)
            .map(Paths::get)
            .orElse(DEFAULT_WORK_DIR);

        Path configFile = Flag.CONFIG.valueOf(args)
            .map(Paths::get)
            .orElse(DEFAULT_CONFIG_FILE);

        boolean textOutput = Flag.TEXT.presentIn(args);
        String studentFilter = Flag.STUDENT.valueOf(args).orElse(null);
        Path outputFile = Flag.OUT.valueOf(args).map(Paths::get).orElse(null);

        String command = Arrays.stream(args)
            .filter(a -> !a.startsWith("--"))
            .findFirst()
            .orElse("");

        return new CliArgs(command, workDir, configFile, textOutput, studentFilter, outputFile);
    }
}
