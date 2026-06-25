package ru.nsu.masolygin.oopchecker.runner;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.masolygin.oopchecker.runner.buildstrategy.BuildStrategy;
import ru.nsu.masolygin.oopchecker.runner.buildstrategy.GradleBuildStrategy;
import ru.nsu.masolygin.oopchecker.runner.buildstrategy.MavenBuildStrategy;

class BuildCommandFactoryTest {

    private BuildCommandFactory factory;

    @BeforeEach
    void setUp() {
        factory = new BuildCommandFactory();
    }

    @Test
    void detectsGradleByBuildGradle(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("build.gradle"));
        BuildStrategy strategy = factory.detect(tmp);
        assertTrue(strategy instanceof GradleBuildStrategy);
    }

    @Test
    void detectsGradleByGradlewWrapper(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("gradlew"));
        assertTrue(factory.detect(tmp) instanceof GradleBuildStrategy);
    }

    @Test
    void detectsGradleByGradlewBat(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("gradlew.bat"));
        assertTrue(factory.detect(tmp) instanceof GradleBuildStrategy);
    }

    @Test
    void detectsGradleByKotlinDsl(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("build.gradle.kts"));
        assertTrue(factory.detect(tmp) instanceof GradleBuildStrategy);
    }

    @Test
    void detectsMavenByPomXml(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("pom.xml"));
        assertTrue(factory.detect(tmp) instanceof MavenBuildStrategy);
    }

    @Test
    void detectsMavenByMvnw(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("mvnw"));
        assertTrue(factory.detect(tmp) instanceof MavenBuildStrategy);
    }

    @Test
    void detectsMavenByMvnwCmd(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("mvnw.cmd"));
        assertTrue(factory.detect(tmp) instanceof MavenBuildStrategy);
    }

    @Test
    void throwsWhenNoBuildSystemFound(@TempDir Path tmp) {
        IllegalStateException e = assertThrows(IllegalStateException.class,
            () -> factory.detect(tmp));
        assertTrue(e.getMessage().contains(tmp.toString()));
    }

    @Test
    void gradlePrioritisedOverMavenIfBothPresent(@TempDir Path tmp) throws IOException {
        Files.createFile(tmp.resolve("build.gradle"));
        Files.createFile(tmp.resolve("pom.xml"));
        assertTrue(factory.detect(tmp) instanceof GradleBuildStrategy);
    }

    @Test
    void detectInvocationsAreIndependent(@TempDir Path tmp1, @TempDir Path tmp2)
        throws IOException {
        Files.createFile(tmp1.resolve("build.gradle"));
        Files.createFile(tmp2.resolve("pom.xml"));
        assertTrue(factory.detect(tmp1) instanceof GradleBuildStrategy);
        assertTrue(factory.detect(tmp2) instanceof MavenBuildStrategy);
    }
}
