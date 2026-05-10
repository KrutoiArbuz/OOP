package ru.nsu.masolygin.oopchecker.vcs;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Парсит вывод git log в объекты Commit.
 */
public class GitLogParser {

    private static final String FIELD_SEP = "\u001F";
    private static final String PRETTY_ARG =
        "--pretty=format:" + String.join(FIELD_SEP, "%H", "%aI", "%an", "%ae", "%s");

    /**
     * Команда git log для диапазона дат [since, until).
     *
     * @param since начало диапазона включительно
     * @param until конец диапазона исключительно
     * @return аргументы команды
     */
    public List<String> buildLogCommand(LocalDate since, LocalDate until) {
        return List.of(
            "git", "log", PRETTY_ARG,
            "--since=" + since,
            "--until=" + until
        );
    }

    /**
     * Парсит многострочный вывод git log.
     *
     * @param stdout вывод git log
     * @return список коммитов
     */
    public List<Commit> parseMany(String stdout) {
        if (stdout.isBlank()) {
            return List.of();
        }
        return Arrays.stream(stdout.split("\n"))
            .filter(line -> !line.isBlank())
            .map(this::parseCommit)
            .toList();
    }

    /**
     * Парсит одну строку в объект Commit.
     *
     * @param line одна строка из git log
     * @return объект Commit
     * @throws GitException если формат строки неверный
     */
    public Commit parseCommit(String line) {
        String[] f = line.split(FIELD_SEP, -1);
        if (f.length < 4) {
            throw new GitException("unexpected git log line: " + line);
        }
        return new Commit(
            f[0],
            OffsetDateTime.parse(f[1]).toInstant(),
            f[2],
            f[3],
            f.length > 4 ? f[4] : ""
        );
    }
}
