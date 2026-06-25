package ru.nsu.masolygin.oopchecker.runner.buildstrategy;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MavenBuildStrategyTest {

    private MavenBuildStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new MavenBuildStrategy();
    }

    @Test
    void applicableWhenPomXmlPresent(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("pom.xml"));
        assertTrue(strategy.isApplicable(tmp));
    }

    @Test
    void applicableWhenMvnwPresent(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("mvnw"));
        assertTrue(strategy.isApplicable(tmp));
    }

    @Test
    void applicableWhenMvnwCmdPresent(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("mvnw.cmd"));
        assertTrue(strategy.isApplicable(tmp));
    }

    @Test
    void notApplicableForEmptyDirectory(@TempDir Path tmp) {
        assertFalse(strategy.isApplicable(tmp));
    }

    @Test
    void notApplicableForGradleProject(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("build.gradle"));
        assertFalse(strategy.isApplicable(tmp));
    }

    @Test
    void compileCmdContainsCompileGoal(@TempDir Path tmp) {
        assertTrue(strategy.compileCmd(tmp).contains("compile"));
    }

    @Test
    void docsCmdContainsJavadocPluginGoal(@TempDir Path tmp) {
        assertTrue(strategy.docsCmd(tmp).contains("javadoc:javadoc"));
    }

    @Test
    void styleCmdContainsCheckstyleCheckGoal(@TempDir Path tmp) {
        assertTrue(strategy.styleCmd(tmp).contains("checkstyle:check"));
    }

    @Test
    void testCmdContainsTestGoal(@TempDir Path tmp) {
        assertTrue(strategy.testCmd(tmp).contains("test"));
    }

    @Test
    void usesMvnFromPathWhenNoWrapper(@TempDir Path tmp) {
        assertTrue(strategy.compileCmd(tmp).get(0).equals("mvn"));
    }
}
