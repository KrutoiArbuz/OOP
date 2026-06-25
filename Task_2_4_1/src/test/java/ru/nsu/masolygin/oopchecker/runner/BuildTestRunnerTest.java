package ru.nsu.masolygin.oopchecker.runner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.masolygin.oopchecker.domain.Assignment;
import ru.nsu.masolygin.oopchecker.domain.Task;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfig;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfigBuilder;
import ru.nsu.masolygin.oopchecker.vcs.GitClient;

class BuildTestRunnerTest {

    private CourseConfig config;
    private BuildTestRunner runner;

    @BeforeEach
    void setUp() {
        CourseConfigBuilder b = new CourseConfigBuilder();
        b.addTask(new Task("t1", "Task 1", 10, null, null, "Task_t1"));
        config = b.build();
        runner = new BuildTestRunner(
            new ProcessRunner(Duration.ofSeconds(5), Map.of()),
            new GitClient()
        );
    }

    @Test
    void unknownTaskIdThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> runner.run(Path.of("."), new Assignment("unknown", "u1"), config));
    }

    @Test
    void notSubmittedWhenLabDirectoryMissing(@TempDir Path repo) {
        TaskExecutionResult r = runner.run(repo, new Assignment("t1", "u1"), config);
        assertFalse(r.compileOk());
        assertFalse(r.docsOk());
        assertFalse(r.styleOk());
        assertEquals(TestReport.EMPTY, r.tests());
    }

    @Test
    void notSubmittedWhenLabDirIsAFile(@TempDir Path repo) throws IOException {
        Files.writeString(repo.resolve("Task_t1"), "not a dir");
        TaskExecutionResult r = runner.run(repo, new Assignment("t1", "u1"), config);
        assertFalse(r.compileOk());
    }

    @Test
    void notSubmittedWhenNoBuildSystemInLabDir(@TempDir Path repo) throws IOException {
        Files.createDirectories(repo.resolve("Task_t1"));
        TaskExecutionResult r = runner.run(repo, new Assignment("t1", "u1"), config);
        assertFalse(r.compileOk());
        assertEquals("t1", r.taskId());
        assertEquals("u1", r.studentGithub());
    }

    @Test
    void resultPreservesTaskAndStudent(@TempDir Path repo) {
        TaskExecutionResult r = runner.run(repo, new Assignment("t1", "u1"), config);
        assertEquals("t1", r.taskId());
        assertEquals("u1", r.studentGithub());
    }

    @Test
    void usesEffectiveLabPathForLookup(@TempDir Path repo) throws IOException {
        // labPath = "Task_t1", создаём только id
        Files.createDirectories(repo.resolve("t1"));
        TaskExecutionResult r = runner.run(repo, new Assignment("t1", "u1"), config);
        assertFalse(r.compileOk());
    }

    @Test
    void notSubmittedHasEmptySubmissionDate(@TempDir Path repo) {
        TaskExecutionResult r = runner.run(repo, new Assignment("t1", "u1"), config);
        assertFalse(r.submissionDate().isPresent());
    }

}
