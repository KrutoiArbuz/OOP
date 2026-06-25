package ru.nsu.masolygin.oopchecker.runner.buildstrategy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Стратегия сборки для Maven-проектов; поддерживает mvnw/mvnw.cmd и системный mvn.
 */
public class MavenBuildStrategy implements BuildStrategy {

    private static final boolean WINDOWS =
        System.getProperty("os.name", "").toLowerCase().contains("win");

    @Override
    public boolean isApplicable(Path dir) {
        return Files.exists(dir.resolve("mvnw"))
            || Files.exists(dir.resolve("mvnw.cmd"))
            || Files.exists(dir.resolve("pom.xml"));
    }

    @Override
    public List<String> compileCmd(Path dir) {
        return cmd(dir, "compile");
    }

    @Override
    public List<String> docsCmd(Path dir) {
        return cmd(dir, "javadoc:javadoc");
    }

    @Override
    public List<String> styleCmd(Path dir) {
        return cmd(dir, "checkstyle:check");
    }

    @Override
    public List<String> testCmd(Path dir) {
        return cmd(dir, "test");
    }

    private List<String> cmd(Path dir, String... goals) {
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
}