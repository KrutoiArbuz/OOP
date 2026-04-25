package ru.nsu.masolygin.oopchecker.report;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ru.nsu.masolygin.oopchecker.domain.Checkpoint;
import ru.nsu.masolygin.oopchecker.domain.CourseConfig;
import ru.nsu.masolygin.oopchecker.domain.Group;
import ru.nsu.masolygin.oopchecker.domain.Student;
import ru.nsu.masolygin.oopchecker.domain.Task;
import ru.nsu.masolygin.oopchecker.grader.Grader;
import ru.nsu.masolygin.oopchecker.grader.GradingResult;
import ru.nsu.masolygin.oopchecker.grader.StudentActivity;
import ru.nsu.masolygin.oopchecker.runner.TaskExecutionResult;

/**
 * Генерирует текстовый отчёт с результатами по группам, задачам, активности и контрольным точкам.
 */
public class ConsoleReport {

    private final CourseConfig config;
    private final Grader grader;

    /**
     * Создаёт генератор консольного отчёта.
     *
     * @param config конфигурация курса
     * @param grader оценщик для расчёта баллов
     */
    public ConsoleReport(CourseConfig config, Grader grader) {
        this.config = config;
        this.grader = grader;
    }

    /**
     * Проверяет, входит ли задача в окно контрольной точки [startDate, date].
     *
     * @param task задача
     * @param cp   контрольная точка
     * @return true если дедлайн задачи попадает в окно
     */
    private static boolean isInCheckpointRange(Task task, Checkpoint cp) {
        java.time.LocalDate dl = task.softDeadline() != null
            ? task.softDeadline() : task.hardDeadline();
        if (dl == null) {
            return true;
        }
        if (dl.isAfter(cp.date())) {
            return false;
        }
        return cp.startDate() == null || !dl.isBefore(cp.startDate());
    }

    /**
     * Печатает отчёт по всем группам.
     *
     * @param allResults        результаты выполнения задач всеми студентами
     * @param activityByStudent активность студентов по GitHub нику
     * @param out               поток вывода для печати
     */
    public void print(
        List<TaskExecutionResult> allResults,
        Map<String, StudentActivity> activityByStudent,
        PrintStream out
    ) {
        for (Group group : config.groups()) {
            printGroup(group, allResults, activityByStudent, out);
            out.println();
        }
    }

    /**
     * Печатает отчёт для одной группы.
     *
     * @param group             группа студентов
     * @param allResults        результаты всех проверок
     * @param activityByStudent активность студентов
     * @param out               поток вывода
     */
    private void printGroup(
        Group group,
        List<TaskExecutionResult> allResults,
        Map<String, StudentActivity> activityByStudent,
        PrintStream out
    ) {
        out.println("Группа " + group.name());
        out.println();

        List<Task> tasks = ReportSupport.tasksAssignedIn(config, group);

        for (Task task : tasks) {
            printTaskTable(group, task, allResults, out);
            out.println();
        }

        printGroupSummary(group, tasks, allResults, activityByStudent, out);
        printCheckpointTables(group, tasks, allResults, out);
    }

    /**
     * Печатает таблицу с результатами по одной лабораторной для группы.
     *
     * @param group      группа студентов
     * @param task       лабораторная работа
     * @param allResults результаты всех проверок
     * @param out        поток вывода
     */
    private void printTaskTable(
        Group group, Task task,
        List<TaskExecutionResult> allResults, PrintStream out
    ) {
        out.println("Лабораторная " + task.id() + " (" + task.name() + ")");
        TextTable table = new TextTable(List.of(
            "Студент", "Сборка", "Документация", "Style guide",
            "Тесты", "Доп. балл", "Общий балл"
        ));
        for (Student student : group.students()) {
            TaskExecutionResult r = ReportSupport.find(allResults, task.id(), student.github());
            if (r == null) {
                continue;
            }
            GradingResult g = grader.grade(r);
            table.addRow(List.of(
                student.fullName(),
                ReportSupport.textMark(r.compileOk()),
                ReportSupport.textMark(r.docsOk()),
                ReportSupport.textMark(r.styleOk()),
                r.tests().passed() + "/" + r.tests().failed() + "/" + r.tests().skipped(),
                String.valueOf(g.extraPoints()),
                ReportSupport.fmt(g.score())
            ));
        }
        out.print(table.render());
    }

    /**
     * Печатает сводную таблицу группы с суммарными баллами и оценками.
     *
     * @param group             группа студентов
     * @param tasks             список лабораторных
     * @param allResults        результаты всех проверок
     * @param activityByStudent активность студентов
     * @param out               поток вывода
     */
    private void printGroupSummary(
        Group group, List<Task> tasks,
        List<TaskExecutionResult> allResults,
        Map<String, StudentActivity> activityByStudent,
        PrintStream out
    ) {
        out.println();
        out.println("Общая статистика группы " + group.name());

        List<String> headers = new ArrayList<>();
        headers.add("Студент");
        tasks.forEach(t -> headers.add(t.id()));
        headers.add("Сумма");
        headers.add("Активность");
        headers.add("Оценка");
        TextTable table = new TextTable(headers);

        double maxScore = tasks.stream().mapToInt(Task::maxPoints).sum();

        for (Student student : group.students()) {
            List<TaskExecutionResult> sr = ReportSupport.resultsOf(allResults, student.github());
            StudentActivity activity = activityByStudent.getOrDefault(
                student.github(), new StudentActivity(student.github(), 0, 0));

            List<String> row = new ArrayList<>();
            row.add(student.fullName());
            tasks.forEach(t -> {
                TaskExecutionResult r = ReportSupport.find(sr, t.id(), student.github());
                row.add(r == null ? "-" : ReportSupport.fmt(grader.grade(r).score()));
            });
            double total = grader.totalScore(sr);
            double withAct = grader.totalWithActivity(sr, activity);
            row.add(ReportSupport.fmt(total));
            row.add(ReportSupport.fmtActivity(activity));
            row.add(grader.gradeFor(withAct, maxScore));
            table.addRow(row);
        }
        out.print(table.render());
    }

    /**
     * Печатает таблицы контрольных точек для группы.
     *
     * @param group      группа студентов
     * @param tasks      список лабораторных
     * @param allResults результаты всех проверок
     * @param out        поток вывода
     */
    private void printCheckpointTables(
        Group group, List<Task> tasks,
        List<TaskExecutionResult> allResults,
        PrintStream out
    ) {
        for (Checkpoint cp : config.checkpoints()) {
            String dateLabel = cp.startDate() != null
                ? cp.startDate() + " – " + cp.date()
                : cp.date().toString();
            out.println();
            out.println("Контрольная точка: " + cp.name() + " (" + dateLabel + ")");

            List<String> cpTaskIds = tasks.stream()
                .filter(t -> isInCheckpointRange(t, cp))
                .map(Task::id)
                .toList();
            double maxCp = grader.maxScoreFor(cpTaskIds);

            TextTable table = new TextTable(List.of("Студент", "Баллов", "Оценка"));
            for (Student student : group.students()) {
                List<TaskExecutionResult> sr = ReportSupport.resultsOf(allResults,
                    student.github());
                double cpScore = grader.checkpointScore(sr, cp);
                table.addRow(List.of(
                    student.fullName(),
                    ReportSupport.fmt(cpScore),
                    grader.gradeFor(cpScore, maxCp)
                ));
            }
            out.print(table.render());
        }
    }
}
