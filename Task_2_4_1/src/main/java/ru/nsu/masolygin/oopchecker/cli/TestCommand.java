package ru.nsu.masolygin.oopchecker.cli;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ru.nsu.masolygin.oopchecker.domain.Assignment;
import ru.nsu.masolygin.oopchecker.domain.CourseConfig;
import ru.nsu.masolygin.oopchecker.domain.Student;
import ru.nsu.masolygin.oopchecker.dsl.DslParser;
import ru.nsu.masolygin.oopchecker.grader.Grader;
import ru.nsu.masolygin.oopchecker.grader.StudentActivity;
import ru.nsu.masolygin.oopchecker.report.ConsoleReport;
import ru.nsu.masolygin.oopchecker.report.HtmlReport;
import ru.nsu.masolygin.oopchecker.runner.BuildTestRunner;
import ru.nsu.masolygin.oopchecker.runner.ProcessRunner;
import ru.nsu.masolygin.oopchecker.runner.TaskExecutionResult;
import ru.nsu.masolygin.oopchecker.vcs.GitClient;
import ru.nsu.masolygin.oopchecker.vcs.GitException;

/**
 * Исполняет команду test: парсинг опций, подготовка репозиториев, запуск тестов и генерация
 * отчёта.
 */
final class TestCommand {

    private final Path workDir;

    /**
     * Создаёт исполнитель команды test.
     *
     * @param workDir рабочий каталог для клонирования репозиториев
     */
    TestCommand(Path workDir) {
        this.workDir = workDir;
    }

    /**
     * Извлекает первую строку из многострочного сообщения.
     *
     * @param msg исходное сообщение (может быть null)
     * @return первая строка или "(null)"
     */
    private static String firstLine(String msg) {
        if (msg == null) {
            return "(null)";
        }
        int nl = msg.indexOf('\n');
        return nl < 0 ? msg : msg.substring(0, nl);
    }

    /**
     * Запускает команду test с обработкой опций.
     *
     * @param args аргументы командной строки
     * @return true если выполнено успешно
     * @throws Exception при ошибке парсинга конфигурации или git операций
     */
    boolean run(String[] args) throws Exception {
        TestCommandOptions options = TestCommandOptions.parse(args);

        Path configFile = Paths.get(DslParser.DEFAULT_SCRIPT_NAME);
        if (!Files.exists(configFile)) {
            System.err.println("[oop-checker] ERROR: " + DslParser.DEFAULT_SCRIPT_NAME
                + " not found in " + Paths.get(".").toAbsolutePath().normalize());
            return false;
        }

        CourseConfig config = new DslParser().parseFile(configFile);
        GitClient git = new GitClient();
        git.ensureNonInteractive();
        Files.createDirectories(workDir);

        BuildTestRunner buildRunner = new BuildTestRunner(
            new ProcessRunner(Duration.ofSeconds(300), Map.of()),
            git
        );

        List<Assignment> assignments = filterAssignments(config.assignments(), options.student());
        Set<String> cloned = cloneRepos(config, assignments, git);

        List<TaskExecutionResult> results = new ArrayList<>();
        for (Assignment a : assignments) {
            if (!cloned.contains(a.studentGithub())) {
                continue;
            }
            Path repo = workDir.resolve(a.studentGithub());
            System.err.println("[oop-checker]   task " + a.taskId() + " / " + a.studentGithub());
            results.add(buildRunner.run(repo, a, config));
        }

        Map<String, StudentActivity> activity = computeActivity(config, cloned, git);

        Grader grader = new Grader(config);
        if (options.textOutput()) {
            new ConsoleReport(config, grader).print(results, activity, System.out);
        } else {
            new HtmlReport(config, grader).print(results, activity, System.out);
        }
        return true;
    }

    /**
     * Фильтрует задания по одному студенту (если задан фильтр).
     *
     * @param all     список всех заданий
     * @param student GitHub ник студента (null означает все задания)
     * @return отфильтрованный список заданий
     */
    private List<Assignment> filterAssignments(List<Assignment> all, String student) {
        if (student == null) {
            return all;
        }
        List<Assignment> filtered = all.stream()
            .filter(a -> a.studentGithub().equals(student))
            .toList();
        if (filtered.isEmpty()) {
            System.err.println("[oop-checker] WARNING: no assignments for --student " + student);
        }
        return filtered;
    }

    /**
     * Клонирует или обновляет репозитории студентов, обрабатывая ошибки gracefully.
     *
     * @param config      конфигурация курса с информацией о студентах
     * @param assignments список заданий
     * @param git         git клиент
     * @return множество GitHub ников успешно клонированных студентов
     */
    private Set<String> cloneRepos(CourseConfig config, List<Assignment> assignments,
        GitClient git) {
        Set<String> cloned = new LinkedHashSet<>();
        Set<String> processed = new LinkedHashSet<>();
        for (Assignment a : assignments) {
            String github = a.studentGithub();
            if (!processed.add(github)) {
                continue;
            }
            Student student = config.findStudent(github).orElse(null);
            if (student == null) {
                System.err.println("[oop-checker] WARNING: student '" + github
                    + "' not found in any group - skipping");
                continue;
            }
            System.err.println("[oop-checker] clone/update " + github + " <- " + student.repoUrl());
            Path repo = workDir.resolve(github);
            try {
                git.cloneOrUpdate(student.repoUrl(), repo);
            } catch (GitException e) {
                System.err.println("[oop-checker] WARNING: cannot clone/update " + github
                    + ": " + firstLine(e.getMessage()));
                // Если папка уже есть (предыдущий запуск) — работаем с тем, что есть.
                if (!Files.exists(repo.resolve(".git"))) {
                    continue;
                }
                System.err.println("[oop-checker] using cache from previous run: " + repo);
            }
            try {
                git.checkoutDefaultBranch(repo);
            } catch (GitException e) {
                System.err.println(
                    "[oop-checker] WARNING: cannot checkout default branch for " + github
                        + ": " + firstLine(e.getMessage()));
                // Ветку не переключили, но папка есть — проверяем как есть.
            }
            cloned.add(github);
        }
        return cloned;
    }

    /**
     * Вычисляет активность студентов по количеству недель с коммитами в семестре.
     *
     * @param config  конфигурация курса с датами семестра
     * @param githubs множество GitHub ников студентов
     * @param git     git клиент для проверки коммитов
     * @return отображение GitHub ник → активность
     */
    private Map<String, StudentActivity> computeActivity(
        CourseConfig config, Set<String> githubs, GitClient git
    ) {
        LocalDate start = config.settings().getSemesterStart();
        int weeks = config.settings().getSemesterWeeks();
        if (start == null) {
            start = LocalDate.now().minusWeeks(weeks).with(DayOfWeek.MONDAY);
            System.err.println("[oop-checker] semesterStart not set, defaulting to " + start);
        }
        final LocalDate semStart = start;

        Map<String, StudentActivity> map = new HashMap<>();
        for (String github : githubs) {
            Path repo = workDir.resolve(github);
            int active = 0;
            for (int i = 0; i < weeks; i++) {
                if (git.hasCommitInWeek(repo, semStart.plusWeeks(i))) {
                    active++;
                }
            }
            System.err.println("[oop-checker]   " + github + " activity: " + active + "/" + weeks);
            map.put(github, new StudentActivity(github, active, weeks));
        }
        return map;
    }

    /**
     * Опции команды test, распарсенные из аргументов командной строки.
     *
     * @param textOutput true если нужен текстовый отчёт (иначе HTML)
     * @param student    GitHub ник студента для фильтра (null означает всех)
     */
    private record TestCommandOptions(boolean textOutput, String student) {

        /**
         * Распарсивает опции из аргументов командной строки.
         *
         * @param args аргументы (--text, --student <ник>)
         * @return объект опций
         */
        private static TestCommandOptions parse(String[] args) {
            boolean textOutput = false;
            String student = null;

            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--text" -> textOutput = true;
                    case "--student" -> {
                        if (i + 1 < args.length) {
                            student = args[++i];
                        }
                    }
                    default -> {
                    }
                }
            }
            return new TestCommandOptions(textOutput, student);
        }
    }
}
