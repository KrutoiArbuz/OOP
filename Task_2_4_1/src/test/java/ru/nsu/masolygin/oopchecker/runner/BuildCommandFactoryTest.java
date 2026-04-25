package ru.nsu.masolygin.oopchecker.runner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class BuildCommandFactoryTest {

    @Test
    void detectsGradleViaBuildGradle(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("build.gradle"));
        assertEquals(BuildCommandFactory.BuildSystem.GRADLE,
            BuildCommandFactory.detectBuildSystem(tmp));
    }

    @Test
    void detectsGradleViaGradlew(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("gradlew"));
        assertEquals(BuildCommandFactory.BuildSystem.GRADLE,
            BuildCommandFactory.detectBuildSystem(tmp));
    }

    @Test
    void detectsGradleViaKotlinDsl(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("build.gradle.kts"));
        assertEquals(BuildCommandFactory.BuildSystem.GRADLE,
            BuildCommandFactory.detectBuildSystem(tmp));
    }

    @Test
    void detectsMavenViaPomXml(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("pom.xml"));
        assertEquals(BuildCommandFactory.BuildSystem.MAVEN,
            BuildCommandFactory.detectBuildSystem(tmp));
    }

    @Test
    void detectsMavenViaMvnw(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("mvnw"));
        assertEquals(BuildCommandFactory.BuildSystem.MAVEN,
            BuildCommandFactory.detectBuildSystem(tmp));
    }

    @Test
    void throwsWhenNoBuildSystemFound(@TempDir Path tmp) {
        assertThrows(IllegalStateException.class,
            () -> BuildCommandFactory.detectBuildSystem(tmp));
    }

    @Test
    void compileCmdForGradleContainsCompileJava(@TempDir Path tmp) {
        List<String> cmd = BuildCommandFactory.compileCmd(tmp,
            BuildCommandFactory.BuildSystem.GRADLE);
        assertTrue(cmd.contains("compileJava"));
    }

    @Test
    void compileCmdForMavenContainsCompile(@TempDir Path tmp) {
        List<String> cmd = BuildCommandFactory.compileCmd(tmp,
            BuildCommandFactory.BuildSystem.MAVEN);
        assertTrue(cmd.contains("compile"));
    }

    @Test
    void docsCmdForGradleContainsJavadoc(@TempDir Path tmp) {
        List<String> cmd = BuildCommandFactory.docsCmd(tmp, BuildCommandFactory.BuildSystem.GRADLE);
        assertTrue(cmd.contains("javadoc"));
    }

    @Test
    void docsCmdForMavenContainsJavadocGoal(@TempDir Path tmp) {
        List<String> cmd = BuildCommandFactory.docsCmd(tmp, BuildCommandFactory.BuildSystem.MAVEN);
        assertTrue(cmd.contains("javadoc:javadoc"));
    }

    @Test
    void styleCmdForGradleContainsCheckstyleMain(@TempDir Path tmp) {
        List<String> cmd = BuildCommandFactory.styleCmd(tmp,
            BuildCommandFactory.BuildSystem.GRADLE);
        assertTrue(cmd.contains("checkstyleMain"));
    }

    @Test
    void styleCmdForMavenContainsCheckstyleCheck(@TempDir Path tmp) {
        List<String> cmd = BuildCommandFactory.styleCmd(tmp, BuildCommandFactory.BuildSystem.MAVEN);
        assertTrue(cmd.contains("checkstyle:check"));
    }

    @Test
    void testCmdForGradleContainsTest(@TempDir Path tmp) {
        List<String> cmd = BuildCommandFactory.testCmd(tmp, BuildCommandFactory.BuildSystem.GRADLE);
        assertTrue(cmd.contains("test"));
    }

    @Test
    void testCmdForMavenContainsTest(@TempDir Path tmp) {
        List<String> cmd = BuildCommandFactory.testCmd(tmp, BuildCommandFactory.BuildSystem.MAVEN);
        assertTrue(cmd.contains("test"));
    }

    @Test
    void compileCmdIsNotEmpty(@TempDir Path tmp) {
        List<String> cmd = BuildCommandFactory.compileCmd(tmp,
            BuildCommandFactory.BuildSystem.GRADLE);
        assertTrue(!cmd.isEmpty());
    }
}
