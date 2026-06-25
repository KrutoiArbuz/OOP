package ru.nsu.masolygin.oopchecker.cli;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ru.nsu.masolygin.oopchecker.domain.Assignment;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfig;
import ru.nsu.masolygin.oopchecker.dsl.DslParser;
import ru.nsu.masolygin.oopchecker.grader.Grader;
import ru.nsu.masolygin.oopchecker.grader.StudentActivity;
import ru.nsu.masolygin.oopchecker.report.ConsoleReport;
import ru.nsu.masolygin.oopchecker.report.HtmlReport;
import ru.nsu.masolygin.oopchecker.report.Report;
import ru.nsu.masolygin.oopchecker.runner.BuildTestRunner;
import ru.nsu.masolygin.oopchecker.runner.ProcessRunner;
import ru.nsu.masolygin.oopchecker.runner.TaskExecutionResult;
import ru.nsu.masolygin.oopchecker.vcs.GitClient;

/**
 * Оркестратор команды {@code test}: загружает конфиг, синхронизирует репозитории, запускает
 * проверки и печатает отчёт.
 */
public class TestCommand {

    private final CliArgs args;

    /**
     * Создаёт оркестратор команды test.
     *
     * @param args разобранные аргументы командной строки
     */
    public TestCommand(CliArgs args) {
        this.args = args;
    }

    /**
     * Запускает полный цикл: конфиг → git → сборка/тесты → активность → отчёт.
     *
     * @throws IOException при ошибке чтения конфигурационного файла
     */
    public void run() throws IOException {
        CourseConfig config = loadConfig();

        GitClient git = new GitClient();
        git.ensureNonInteractive();
        Files.createDirectories(args.workDir());

        List<Assignment> assignments = filterAssignments(config.assignments());
        Set<String> synced = new RepoSyncer(args.workDir(), git).sync(config, assignments);

        List<TaskExecutionResult> results = runChecks(config, synced, assignments, git);
        Map<String, StudentActivity> activity =
            new ActivityCollector(args.workDir(), git).collect(config, synced);

        if (args.outputFile() != null) {
            try (OutputStream file = Files.newOutputStream(args.outputFile());
                PrintStream ps = new PrintStream(file, true, StandardCharsets.UTF_8)) {
                buildReport(config).print(results, activity, ps);
            }
            System.err.println("[oop-checker] report written to " + args.outputFile());
        } else if (args.textOutput()) {
            buildReport(config).print(results, activity, System.err);
        } else {
            buildReport(config).print(results, activity, System.out);
        }
    }

    private List<TaskExecutionResult> runChecks(
        CourseConfig config, Set<String> synced,
        List<Assignment> assignments, GitClient git
    ) {
        BuildTestRunner buildRunner = new BuildTestRunner(
            new ProcessRunner(Duration.ofSeconds(300), Map.of()), git);

        List<TaskExecutionResult> results = new ArrayList<>();
        for (Assignment assignment : assignments) {
            if (!synced.contains(assignment.studentGithub())) {
                continue;
            }
            Path repo = args.workDir().resolve(assignment.studentGithub());
            System.err.println("[oop-checker]   task "
                + assignment.taskId() + " / " + assignment.studentGithub());
            results.add(buildRunner.run(repo, assignment, config));
        }
        return results;
    }

    private List<Assignment> filterAssignments(List<Assignment> all) {
        if (args.studentFilter() == null) {
            return all;
        }
        List<Assignment> filtered = all.stream()
            .filter(a -> a.studentGithub().equals(args.studentFilter()))
            .toList();
        if (filtered.isEmpty()) {
            System.err.println("[oop-checker] WARNING: no assignments for --student="
                + args.studentFilter());
        }
        return filtered;
    }

    private Report buildReport(CourseConfig config) {
        Grader grader = new Grader(config);
        return args.textOutput()
            ? new ConsoleReport(config, grader)
            : new HtmlReport(config, grader);
    }

    private CourseConfig loadConfig() throws IOException {
        Path configFile = args.configFile();
        if (!Files.exists(configFile)) {
            throw new IllegalStateException(
                "config not found: " + configFile
                    + " (cwd: " + Paths.get(".").toAbsolutePath().normalize() + ")");
        }
        return new DslParser().parseFile(configFile);
    }
}
