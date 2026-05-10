package ru.nsu.masolygin.oopchecker.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.masolygin.oopchecker.domain.SemesterInfo;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfig;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfigBuilder;
import ru.nsu.masolygin.oopchecker.grader.StudentActivity;
import ru.nsu.masolygin.oopchecker.vcs.GitClient;

class ActivityCollectorTest {

    private CourseConfig configWithSemester(LocalDate start, int weeks) {
        CourseConfigBuilder b = new CourseConfigBuilder();
        b.getSettingsBuilder().addSemester(1, new SemesterInfo(start, weeks));
        return b.build();
    }

    @Test
    void activityIsZeroWhenNoCommits(@TempDir Path workDir) {
        StubGit git = new StubGit();
        CourseConfig config = configWithSemester(LocalDate.of(2025, 9, 1), 17);

        Map<String, StudentActivity> result = new ActivityCollector(workDir, git)
            .collect(config, Set.of("alice"));

        StudentActivity a = result.get("alice");
        assertEquals(0, a.activeWeeks());
        assertEquals(17, a.totalWeeks());
    }

    @Test
    void activityCountsOnlyMatchingWeeks(@TempDir Path workDir) {
        StubGit git = new StubGit();
        LocalDate start = LocalDate.of(2025, 9, 1);
        git.activeWeeks.add(start);
        git.activeWeeks.add(start.plusWeeks(2));
        git.activeWeeks.add(start.plusWeeks(5));

        CourseConfig config = configWithSemester(start, 17);
        Map<String, StudentActivity> result = new ActivityCollector(workDir, git)
            .collect(config, Set.of("alice"));

        assertEquals(3, result.get("alice").activeWeeks());
    }

    @Test
    void everyStudentGetsAnEntry(@TempDir Path workDir) {
        StubGit git = new StubGit();
        CourseConfig config = configWithSemester(LocalDate.now(), 5);

        Map<String, StudentActivity> result = new ActivityCollector(workDir, git)
            .collect(config, Set.of("a", "b", "c"));

        assertEquals(3, result.size());
        assertTrue(result.containsKey("a"));
        assertTrue(result.containsKey("b"));
        assertTrue(result.containsKey("c"));
    }

    @Test
    void emptyStudentsSetReturnsEmptyMap(@TempDir Path workDir) {
        StubGit git = new StubGit();
        CourseConfig config = configWithSemester(LocalDate.now(), 5);
        assertTrue(new ActivityCollector(workDir, git).collect(config, Set.of()).isEmpty());
    }

    @Test
    void noSemesterConfiguredThrows(@TempDir Path workDir) {
        StubGit git = new StubGit();
        CourseConfig config = new CourseConfigBuilder().build();
        assertThrows(IllegalStateException.class,
            () -> new ActivityCollector(workDir, git).collect(config, Set.of("alice")));
    }

    @Test
    void totalWeeksMatchesSemesterConfig(@TempDir Path workDir) {
        StubGit git = new StubGit();
        CourseConfig config = configWithSemester(LocalDate.of(2025, 9, 1), 13);

        StudentActivity a = new ActivityCollector(workDir, git)
            .collect(config, Set.of("alice")).get("alice");
        assertEquals(13, a.totalWeeks());
    }

    @Test
    void usesFirstSemesterWhenMultipleConfigured(@TempDir Path workDir) {
        StubGit git = new StubGit();
        CourseConfigBuilder b = new CourseConfigBuilder();
        b.getSettingsBuilder().addSemester(1,
            new SemesterInfo(LocalDate.of(2025, 9, 1), 17));
        b.getSettingsBuilder().addSemester(2,
            new SemesterInfo(LocalDate.of(2026, 2, 1), 17));

        StudentActivity a = new ActivityCollector(workDir, git)
            .collect(b.build(), Set.of("alice")).get("alice");
        assertEquals(17, a.totalWeeks());
    }

    /**
     * Stub git клиента: возвращает заранее заданные «активные» недели.
     */
    private static class StubGit extends GitClient {

        final Set<LocalDate> activeWeeks = new HashSet<>();

        @Override
        public boolean hasCommitInWeek(Path repo, LocalDate weekStart) {
            return activeWeeks.contains(weekStart);
        }
    }
}
