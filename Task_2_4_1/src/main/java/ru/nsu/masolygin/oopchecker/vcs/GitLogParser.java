package ru.nsu.masolygin.oopchecker.vcs;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Парсит вывод git log в объекты Commit.
 */
final class GitLogParser {

    private static final String FIELD_SEP = "\u001F";
    private static final String PRETTY_ARG = "--pretty=format:%H" + FIELD_SEP + "%aI"
        + FIELD_SEP + "%an" + FIELD_SEP + "%ae" + FIELD_SEP + "%s";

    private GitLogParser() {
    }

    /**
     * Возвращает аргумент для git log для форматирования вывода.
     *
     * @return строка аргумента --pretty=format:...
     */
    static String prettyFormatArg() {
        return PRETTY_ARG;
    }

    /**
     * Парсит многострочный вывод git log.
     *
     * @param stdout вывод git log
     * @return список коммитов
     */
    static List<Commit> parseMany(String stdout) {
        if (stdout.isBlank()) {
            return List.of();
        }
        return Arrays.stream(stdout.split("\n"))
            .filter(line -> !line.isBlank())
            .map(GitLogParser::parseCommit)
            .toList();
    }

    /**
     * Парсит одну строку вывода git log.
     *
     * @param stdout вывод git log (одна строка)
     * @return опциональный коммит
     */
    static Optional<Commit> parseSingle(String stdout) {
        if (stdout.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(parseCommit(stdout.trim()));
    }

    /**
     * Парсит одну строку в объект Commit.
     *
     * @param line одна строка из git log
     * @return объект Commit
     * @throws GitException если формат строки неверный
     */
    private static Commit parseCommit(String line) {
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