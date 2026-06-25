package ru.nsu.masolygin.oopchecker.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.masolygin.oopchecker.domain.Assignment;
import ru.nsu.masolygin.oopchecker.domain.Group;
import ru.nsu.masolygin.oopchecker.domain.Student;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfig;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfigBuilder;
import ru.nsu.masolygin.oopchecker.vcs.GitClient;
import ru.nsu.masolygin.oopchecker.vcs.GitException;

class RepoSyncerTest {

    private CourseConfig configWithStudents(Student... students) {
        CourseConfigBuilder b = new CourseConfigBuilder();
        b.addGroup(new Group("g", List.of(students)));
        return b.build();
    }

    @Test
    void successfulCloneAndCheckoutMarksStudentSynced(@TempDir Path workDir) {
        StubGit git = new StubGit();
        CourseConfig config = configWithStudents(
            new Student("alice", "A", "https://x/a.git"));
        Set<String> synced = new RepoSyncer(workDir, git).sync(config,
            List.of(new Assignment("t1", "alice")));
        assertTrue(synced.contains("alice"));
        assertEquals(1, git.cloneCalls.size());
        assertEquals(1, git.checkoutCalls.size());
    }

    @Test
    void duplicateAssignmentsForSameStudentDoNotDuplicateClone(@TempDir Path workDir) {
        StubGit git = new StubGit();
        CourseConfig config = configWithStudents(
            new Student("alice", "A", "https://x/a.git"));
        new RepoSyncer(workDir, git).sync(config, List.of(
            new Assignment("t1", "alice"),
            new Assignment("t2", "alice"),
            new Assignment("t3", "alice")
        ));
        assertEquals(1, git.cloneCalls.size());
    }

    @Test
    void unknownStudentIsSkipped(@TempDir Path workDir) {
        StubGit git = new StubGit();
        CourseConfig config = configWithStudents(
            new Student("alice", "A", "https://x/a.git"));
        Set<String> synced = new RepoSyncer(workDir, git).sync(config,
            List.of(new Assignment("t1", "ghost")));
        assertFalse(synced.contains("ghost"));
        assertEquals(0, git.cloneCalls.size());
    }

    @Test
    void cloneFailureWithoutCacheSkipsStudent(@TempDir Path workDir) {
        StubGit git = new StubGit();
        git.cloneShouldFail = true;
        CourseConfig config = configWithStudents(
            new Student("alice", "A", "https://x/a.git"));
        Set<String> synced = new RepoSyncer(workDir, git).sync(config,
            List.of(new Assignment("t1", "alice")));
        assertFalse(synced.contains("alice"));
    }

    @Test
    void cloneFailureWithCachedRepoStillProceeds(@TempDir Path workDir) throws Exception {
        StubGit git = new StubGit();
        git.cloneShouldFail = true;
        Files.createDirectories(workDir.resolve("alice/.git"));
        CourseConfig config = configWithStudents(
            new Student("alice", "A", "https://x/a.git"));
        Set<String> synced = new RepoSyncer(workDir, git).sync(config,
            List.of(new Assignment("t1", "alice")));
        assertTrue(synced.contains("alice"));
    }

    @Test
    void checkoutFailureDoesNotRemoveStudentFromSynced(@TempDir Path workDir) {
        StubGit git = new StubGit();
        git.checkoutShouldFail = true;
        CourseConfig config = configWithStudents(
            new Student("alice", "A", "https://x/a.git"));
        Set<String> synced = new RepoSyncer(workDir, git).sync(config,
            List.of(new Assignment("t1", "alice")));
        assertTrue(synced.contains("alice"));
    }

    @Test
    void emptyAssignmentsListReturnsEmptySet(@TempDir Path workDir) {
        StubGit git = new StubGit();
        CourseConfig config = configWithStudents();
        Set<String> synced = new RepoSyncer(workDir, git).sync(config, List.of());
        assertTrue(synced.isEmpty());
    }

    @Test
    void multipleStudentsAreEachSynced(@TempDir Path workDir) {
        StubGit git = new StubGit();
        CourseConfig config = configWithStudents(
            new Student("alice", "A", "https://x/a.git"),
            new Student("bob", "B", "https://x/b.git"));
        Set<String> synced = new RepoSyncer(workDir, git).sync(config, List.of(
            new Assignment("t1", "alice"),
            new Assignment("t1", "bob")
        ));
        assertEquals(2, synced.size());
    }

    /**
     * Stub git клиента: не делает реальные сетевые вызовы.
     */
    private static class StubGit extends GitClient {

        final List<String> cloneCalls = new ArrayList<>();
        final List<String> checkoutCalls = new ArrayList<>();
        boolean cloneShouldFail = false;
        boolean checkoutShouldFail = false;

        @Override
        public void cloneOrUpdate(String url, Path target) {
            cloneCalls.add(url);
            if (cloneShouldFail) {
                throw new GitException("clone failed");
            }
        }

        @Override
        public void checkoutDefaultBranch(Path repo) {
            checkoutCalls.add(repo.toString());
            if (checkoutShouldFail) {
                throw new GitException("checkout failed");
            }
        }
    }
}
