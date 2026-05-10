package ru.nsu.masolygin.oopchecker.runner.buildstrategy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Стратегия сборки для Gradle-проектов; поддерживает gradlew/gradlew.bat и системный gradle.
 */
public class GradleBuildStrategy implements BuildStrategy {

    private static final boolean WINDOWS =
        System.getProperty("os.name", "").toLowerCase().contains("win");

    @Override
    public boolean isApplicable(Path dir) {
        return Files.exists(dir.resolve("gradlew"))
            || Files.exists(dir.resolve("gradlew.bat"))
            || Files.exists(dir.resolve("build.gradle"))
            || Files.exists(dir.resolve("build.gradle.kts"));
    }

    @Override
    public List<String> compileCmd(Path dir) {
        return cmd(dir, "compileJava");
    }

    @Override
    public List<String> docsCmd(Path dir) {
        return cmd(dir, "javadoc");
    }

    @Override
    public List<String> styleCmd(Path dir) {
        return cmd(dir, "checkstyleMain");
    }

    @Override
    public List<String> testCmd(Path dir) {
        return cmd(dir, "test");
    }

    private List<String> cmd(Path dir, String... tasks) {
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
}