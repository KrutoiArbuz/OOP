package ru.nsu.masolygin.oopchecker.runner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Определяет систему сборки и строит команды для каждого этапа проверки.
 */
final class BuildCommandFactory {

    private static final boolean WINDOWS =
        System.getProperty("os.name", "").toLowerCase().contains("win");

    private BuildCommandFactory() {
    }

    /**
     * Определяет систему сборки по наличию конфигурационных файлов.
     *
     * @param dir каталог для проверки
     * @return тип системы сборки
     * @throws IllegalStateException если ни одна система сборки не найдена
     */
    static BuildSystem detectBuildSystem(Path dir) {
        if (Files.exists(dir.resolve("gradlew"))
            || Files.exists(dir.resolve("gradlew.bat"))
            || Files.exists(dir.resolve("build.gradle"))
            || Files.exists(dir.resolve("build.gradle.kts"))) {
            return BuildSystem.GRADLE;
        }
        if (Files.exists(dir.resolve("mvnw"))
            || Files.exists(dir.resolve("mvnw.cmd"))
            || Files.exists(dir.resolve("pom.xml"))) {
            return BuildSystem.MAVEN;
        }
        throw new IllegalStateException("No build system found in " + dir);
    }

    /**
     * Строит команду компиляции для заданной системы сборки.
     *
     * @param dir каталог проекта
     * @param bs  система сборки
     * @return аргументы команды
     */
    static List<String> compileCmd(Path dir, BuildSystem bs) {
        return bs == BuildSystem.GRADLE
            ? gradleCmd(dir, "compileJava")
            : mavenCmd(dir, "compile");
    }

    /**
     * Строит команду генерации документации для заданной системы сборки.
     *
     * @param dir каталог проекта
     * @param bs  система сборки
     * @return аргументы команды
     */
    static List<String> docsCmd(Path dir, BuildSystem bs) {
        return bs == BuildSystem.GRADLE
            ? gradleCmd(dir, "javadoc")
            : mavenCmd(dir, "javadoc:javadoc");
    }

    /**
     * Строит команду проверки стиля кода для заданной системы сборки.
     *
     * @param dir каталог проекта
     * @param bs  система сборки
     * @return аргументы команды
     */
    static List<String> styleCmd(Path dir, BuildSystem bs) {
        return bs == BuildSystem.GRADLE
            ? gradleCmd(dir, "checkstyleMain")
            : mavenCmd(dir, "checkstyle:check");
    }

    /**
     * Строит команду запуска тестов для заданной системы сборки.
     *
     * @param dir каталог проекта
     * @param bs  система сборки
     * @return аргументы команды
     */
    static List<String> testCmd(Path dir, BuildSystem bs) {
        return bs == BuildSystem.GRADLE
            ? gradleCmd(dir, "test")
            : mavenCmd(dir, "test");
    }

    /**
     * Строит команду Gradle с приоритетом: gradlew.bat (Windows), gradlew (Unix), gradle (global).
     *
     * @param dir   каталог проекта
     * @param tasks задачи gradle для выполнения
     * @return аргументы команды
     */
    private static List<String> gradleCmd(Path dir, String... tasks) {
        List<String> cmd = new ArrayList<>();
        if (WINDOWS && Files.exists(dir.resolve("gradlew.bat"))) {
            cmd.addAll(List.of("cmd", "/c", "gradlew.bat"));
        } else if (Files.exists(dir.resolve("gradlew"))) {
            cmd.add(dir.resolve("gradlew").toAbsolutePath().toString());
        } else {
            cmd.add("gradle");
        }
        cmd.addAll(Arrays.asList(tasks));
        return cmd;
    }

    /**
     * Строит команду Maven с приоритетом: mvnw.cmd (Windows), mvnw (Unix), mvn (global).
     *
     * @param dir   каталог проекта
     * @param goals goals Maven для выполнения
     * @return аргументы команды
     */
    private static List<String> mavenCmd(Path dir, String... goals) {
        List<String> cmd = new ArrayList<>();
        if (WINDOWS && Files.exists(dir.resolve("mvnw.cmd"))) {
            cmd.addAll(List.of("cmd", "/c", "mvnw.cmd"));
        } else if (Files.exists(dir.resolve("mvnw"))) {
            cmd.add(dir.resolve("mvnw").toAbsolutePath().toString());
        } else {
            cmd.add("mvn");
        }
        cmd.addAll(Arrays.asList(goals));
        return cmd;
    }

    /**
     * Поддерживаемые системы сборки.
     */
    enum BuildSystem {GRADLE, MAVEN}
}
