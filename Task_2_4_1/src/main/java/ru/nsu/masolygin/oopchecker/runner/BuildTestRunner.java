package ru.nsu.masolygin.oopchecker.runner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import ru.nsu.masolygin.oopchecker.domain.Assignment;
import ru.nsu.masolygin.oopchecker.domain.CourseConfig;
import ru.nsu.masolygin.oopchecker.domain.Task;
import ru.nsu.masolygin.oopchecker.vcs.GitClient;

/**
 * Запускает полный цикл проверки лабораторной работы (компиляция, документация, стиль, тесты).
 */
public class BuildTestRunner {

    private static final Duration BUILD_TIMEOUT = Duration.ofSeconds(300);

    private final ProcessRunner runner;
    private final GitClient git;

    /**
     * Создаёт исполнитель проверки с заданным процесс-раннером и git клиентом.
     *
     * @param runner раннер для запуска внешних команд
     * @param git    git клиент для определения даты сдачи
     */
    public BuildTestRunner(ProcessRunner runner, GitClient git) {
        this.runner = runner;
        this.git = git;
    }

    /**
     * Логирует сообщение о проверке задания.
     *
     * @param taskId  идентификатор задачи
     * @param student GitHub ник студента
     * @param msg     сообщение
     */
    private static void log(String taskId, String student, String msg) {
        System.err.println("[oop-checker]   [" + taskId + "/" + student + "] " + msg);
    }

    /**
     * Преобразует boolean в символ +/-.
     *
     * @param ok статус проверки
     * @return '+' если ok, иначе '-'
     */
    private static String mark(boolean ok) {
        return ok ? "+" : "-";
    }

    /**
     * Проверяет, что команда упала потому что задача/goal не существует в проекте, а не из-за
     * реальных ошибок (violations/compilation error).
     *
     * @param result результат выполнения команды
     * @return true если задача не найдена (инструмент не настроен)
     */
    private static boolean isTaskNotFound(ProcessResult result) {
        if (result.success()) {
            return false;
        }
        String combined = result.stdout() + result.stderr();
        return combined.contains("Task '") && combined.contains("' not found")
            || combined.contains("No plugin found for prefix")
            || combined.contains("Could not find goal")
            || combined.contains("is not a task in root project");
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

        Path labDir = repo.resolve(task.effectiveLabPath());
        if (!Files.exists(labDir) || !Files.isDirectory(labDir)) {
            log(task.id(), assignment.studentGithub(),
                "directory not found: " + task.effectiveLabPath());
            return TaskExecutionResult.notSubmitted(task.id(), assignment.studentGithub());
        }

        Optional<LocalDate> submitDate =
            git.lastCommitDateForPath(repo, task.effectiveLabPath());

        BuildCommandFactory.BuildSystem bs;
        try {
            bs = BuildCommandFactory.detectBuildSystem(labDir);
        } catch (IllegalStateException e) {
            log(task.id(), assignment.studentGithub(), "build system not detected in " + labDir);
            return TaskExecutionResult.notSubmitted(task.id(), assignment.studentGithub());
        }

        ProcessResult compile = exec(labDir, BuildCommandFactory.compileCmd(labDir, bs),
            BUILD_TIMEOUT);
        if (!compile.success()) {
            log(task.id(), assignment.studentGithub(),
                "compile FAILED (exit=" + compile.exitCode() + ")");
            return new TaskExecutionResult(task.id(), assignment.studentGithub(),
                false, false, false, TestReport.EMPTY, submitDate);
        }

        ProcessResult docsResult = exec(labDir, BuildCommandFactory.docsCmd(labDir, bs),
            BUILD_TIMEOUT);
        boolean docsOk = docsResult.success() || isTaskNotFound(docsResult);

        ProcessResult styleResult = exec(labDir, BuildCommandFactory.styleCmd(labDir, bs),
            BUILD_TIMEOUT);
        boolean styleOk = styleResult.success() || isTaskNotFound(styleResult);

        Duration testTimeout = Duration.ofSeconds(config.settings().getTestTimeoutSeconds());
        exec(labDir, BuildCommandFactory.testCmd(labDir, bs), testTimeout);
        TestReport tests = JunitXmlReportParser.parse(labDir, bs);

        log(task.id(), assignment.studentGithub(),
            "compile=+ docs=" + mark(docsOk) + " style=" + mark(styleOk)
                + " tests=" + tests.passed() + "/" + tests.failed() + "/" + tests.skipped());

        return new TaskExecutionResult(
            task.id(), assignment.studentGithub(),
            true, docsOk, styleOk, tests, submitDate);
    }

    /**
     * Запускает команду с заданным таймаутом, преобразуя исключения в неудачный результат.
     *
     * @param workDir рабочий каталог
     * @param cmd     команда для выполнения
     * @param timeout таймаут
     * @return результат выполнения или результат с кодом -1 при ошибке
     */
    private ProcessResult exec(Path workDir, List<String> cmd, Duration timeout) {
        try {
            return runner.run(workDir, cmd, timeout);
        } catch (Exception e) {
            return new ProcessResult(-1, "", e.getMessage(), false,
                Duration.ZERO);
        }
    }
}
