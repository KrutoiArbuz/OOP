package ru.nsu.masolygin.oopchecker.runner.buildstrategy;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GradleBuildStrategyTest {

    private GradleBuildStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new GradleBuildStrategy();
    }

    @Test
    void applicableWhenBuildGradlePresent(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("build.gradle"));
        assertTrue(strategy.isApplicable(tmp));
    }

    @Test
    void applicableWhenBuildGradleKtsPresent(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("build.gradle.kts"));
        assertTrue(strategy.isApplicable(tmp));
    }

    @Test
    void applicableWhenGradlewWrapperPresent(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("gradlew"));
        assertTrue(strategy.isApplicable(tmp));
    }

    @Test
    void applicableWhenGradlewBatPresent(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("gradlew.bat"));
        assertTrue(strategy.isApplicable(tmp));
    }

    @Test
    void notApplicableForEmptyDirectory(@TempDir Path tmp) {
        assertFalse(strategy.isApplicable(tmp));
    }

    @Test
    void notApplicableForMavenProject(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("pom.xml"));
        assertFalse(strategy.isApplicable(tmp));
    }

    @Test
    void compileCmdContainsCompileJavaTask(@TempDir Path tmp) {
        List<String> cmd = strategy.compileCmd(tmp);
        assertTrue(cmd.contains("compileJava"));
    }

    @Test
    void docsCmdContainsJavadocTask(@TempDir Path tmp) {
        assertTrue(strategy.docsCmd(tmp).contains("javadoc"));
    }

    @Test
    void styleCmdContainsCheckstyleMainTask(@TempDir Path tmp) {
        assertTrue(strategy.styleCmd(tmp).contains("checkstyleMain"));
    }

    @Test
    void testCmdContainsTestTask(@TempDir Path tmp) {
        assertTrue(strategy.testCmd(tmp).contains("test"));
    }

    @Test
    void usesGradleFromPathWhenNoWrapper(@TempDir Path tmp) {
        List<String> cmd = strategy.compileCmd(tmp);
        assertTrue(cmd.get(0).equals("gradle"));
    }

    @Test
    void usesWrapperPathWhenGradlewExists(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("gradlew"));
        List<String> cmd = strategy.compileCmd(tmp);
        boolean isWindows = System.getProperty("os.name", "").toLowerCase().contains("win");
        if (!isWindows) {
            assertTrue(cmd.get(0).contains("gradlew"));
        }
    }

    @Test
    void commandIsNotEmpty(@TempDir Path tmp) {
        assertFalse(strategy.compileCmd(tmp).isEmpty());
        assertFalse(strategy.docsCmd(tmp).isEmpty());
        assertFalse(strategy.styleCmd(tmp).isEmpty());
        assertFalse(strategy.testCmd(tmp).isEmpty());
    }
}
