package ru.nsu.masolygin.oopchecker.runner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;
import ru.nsu.masolygin.oopchecker.domain.Assignment;
import ru.nsu.masolygin.oopchecker.domain.Task;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfig;
import ru.nsu.masolygin.oopchecker.runner.buildstrategy.BuildStrategy;
import ru.nsu.masolygin.oopchecker.vcs.GitClient;

/**
 * Запускает полный цикл проверки лабораторной работы (компиляция, документация, стиль, тесты).
 */
public class BuildTestRunner {

    /**
     * Стандартные директории JUnit XML отчётов: Gradle и Maven.
     */
    private static final List<String> TEST_REPORT_DIRS = List.of(
        "build/test-results/test",
        "target/surefire-reports"
    );

    private final ProcessRunner runner;
    private final GitClient git;
    private final BuildCommandFactory buildCommandFactory = new BuildCommandFactory();
    private final JUnitXmlReportParser xmlParser = new JUnitXmlReportParser();

    public BuildTestRunner(ProcessRunner runner, GitClient git) {
        this.runner = runner;
        this.git = git;
    }

    /**
     * Запускает полный цикл проверки задания: компиляция, документация, стиль, тесты.
     *
     * @param repo       корень склонированного репозитория студента
     * @param assignment проверяемое задание
     * @param config     конфигурация курса с информацией о задачах и таймаутах
     * @return результат проверки с флагами и результатами тестов
     */
    public TaskExecutionResult run(Path repo, Assignment assignment, CourseConfig config) {
        Task task = config.findTask(assignment.taskId()).orElseThrow(
            () -> new IllegalArgumentException("Task not found: " + assignment.taskId()));

        Path labDir = repo.resolve(task.labPath());
        if (!Files.exists(labDir) || !Files.isDirectory(labDir)) {
            log(task.id(), assignment.studentGithub(), "directory not found: " + task.labPath());
            return TaskExecutionResult.notSubmitted(task.id(), assignment.studentGithub());
        }

        var submitDate = git.lastCommitDateForPath(repo, task.labPath()).orElse(null);

        BuildStrategy strategy;
        try {
            strategy = buildCommandFactory.detect(labDir);
        } catch (IllegalStateException e) {
            log(task.id(), assignment.studentGithub(), "build system not detected in " + labDir);
            return TaskExecutionResult.notSubmitted(task.id(), assignment.studentGithub());
        }

        Duration buildTimeout = Duration.ofSeconds(config.settings().buildTimeoutSeconds());
        Duration testTimeout = Duration.ofSeconds(config.settings().testTimeoutSeconds());

        ProcessResult compile = exec(labDir, strategy.compileCmd(labDir), buildTimeout);
        if (!compile.success()) {
            log(task.id(), assignment.studentGithub(),
                "compile FAILED (exit=" + compile.exitCode() + ")");
            return new TaskExecutionResult(
                task.id(), assignment.studentGithub(),
                false, false, false, TestReport.EMPTY, submitDate);
        }

        ProcessResult docsResult = exec(labDir, strategy.docsCmd(labDir), buildTimeout);
        boolean docsOk = docsResult.success() || isToolNotConfigured(docsResult);

        ProcessResult styleResult = exec(labDir, strategy.styleCmd(labDir), buildTimeout);
        boolean styleOk = styleResult.success() || isToolNotConfigured(styleResult);
        exec(labDir, strategy.testCmd(labDir), testTimeout);
        TestReport tests = parseTestReports(labDir);

        log(task.id(), assignment.studentGithub(),
            "compile=+ docs=" + mark(docsOk) + " style=" + mark(styleOk)
                + " tests=" + tests.passed() + "/" + tests.failed() + "/" + tests.skipped());

        return new TaskExecutionResult(
            task.id(), assignment.studentGithub(),
            true, docsOk, styleOk, tests, submitDate);
    }

    /**
     * Ищет JUnit XML отчёты в стандартных директориях и агрегирует результаты.
     *
     * @param labDir корень проекта задачи
     * @return агрегированный отчёт или {@link TestReport#EMPTY} если отчётов нет
     */
    private TestReport parseTestReports(Path labDir) {
        for (String relDir : TEST_REPORT_DIRS) {
            Path reportsDir = labDir.resolve(relDir);
            if (!Files.isDirectory(reportsDir)) {
                continue;
            }
            try (Stream<Path> stream = Files.list(reportsDir)) {
                List<Path> xmlFiles = stream
                    .filter(p -> p.toString().endsWith(".xml"))
                    .toList();
                if (xmlFiles.isEmpty()) {
                    continue;
                }
                int passed = 0;
                int failed = 0;
                int skipped = 0;
                for (Path xml : xmlFiles) {
                    TestReport r = xmlParser.parseXmlFile(xml);
                    passed += r.passed();
                    failed += r.failed();
                    skipped += r.skipped();
                }
                return new TestReport(passed, failed, skipped);
            } catch (IOException e) {
                System.err.println("[build-runner] cannot read reports in "
                    + reportsDir + ": " + e.getMessage());
            }
        }
        return TestReport.EMPTY;
    }

    /**
     * Проверяет, что команда упала из-за отсутствия инструмента/цели, а не реальных ошибок.
     *
     * @param result результат выполнения команды
     * @return true если инструмент не настроен в проекте
     */
    private boolean isToolNotConfigured(ProcessResult result) {
        if (result.success()) {
            return false;
        }
        String combined = result.stdout() + result.stderr();
        return combined.contains("Task '") && combined.contains("' not found")
            || combined.contains("No plugin found for prefix")
            || combined.contains("Could not find goal")
            || combined.contains("is not a task in root project");
    }

    private ProcessResult exec(Path workDir, List<String> cmd, Duration timeout) {
        try {
            return runner.run(workDir, cmd, timeout);
        } catch (Exception e) {
            return new ProcessResult(-1, "", e.getMessage() != null ? e.getMessage() : "",
                false, Duration.ZERO);
        }
    }

    private void log(String taskId, String student, String msg) {
        System.err.println("[oop-checker]   [" + taskId + "/" + student + "] " + msg);
    }

    private String mark(boolean ok) {
        return ok ? "+" : "-";
    }
}
