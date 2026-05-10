package ru.nsu.masolygin.oopchecker.report;

import java.io.PrintStream;
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
 * Генерирует HTML отчёт с результатами по группам, задачам и контрольным точкам.
 */
public class HtmlReport {

    private static final String STYLE =
        "body{font-family:sans-serif;margin:2em}"
            + "table{border-collapse:collapse;margin:.5em 0 1.5em}"
            + "th,td{border:1px solid #999;padding:4px 10px;text-align:left}"
            + "th{background:#eee}"
            + ".ok{color:#2a7;font-weight:bold}"
            + ".bad{color:#c33;font-weight:bold}"
            + "h2{margin-top:2em}h3{margin-top:1.5em;color:#444}";

    private final CourseConfig config;
    private final Grader grader;

    /**
     * Создаёт генератор HTML отчёта.
     *
     * @param config конфигурация курса
     * @param grader оценщик для расчёта баллов
     */
    public HtmlReport(CourseConfig config, Grader grader) {
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
     * Печатает HTML отчёт по всем группам.
     *
     * @param results  результаты выполнения задач всеми студентами
     * @param activity активность студентов по GitHub нику
     * @param out      поток вывода для печати
     */
    public void print(
        List<TaskExecutionResult> results,
        Map<String, StudentActivity> activity,
        PrintStream out
    ) {
        out.println("<!doctype html>");
        out.println("<html lang=\"ru\"><head><meta charset=\"utf-8\">"
            + "<title>oop-checker report</title>"
            + "<style>" + STYLE + "</style></head><body>");
        config.groups().forEach(g -> printGroup(out, g, results, activity));
        out.println("</body></html>");
    }

    /**
     * Печатает HTML отчёт для одной группы.
     *
     * @param out      поток вывода
     * @param group    группа студентов
     * @param results  результаты всех проверок
     * @param activity активность студентов
     */
    private void printGroup(
        PrintStream out, Group group,
        List<TaskExecutionResult> results,
        Map<String, StudentActivity> activity
    ) {
        out.println("<h2>Группа " + ReportSupport.esc(group.name()) + "</h2>");
        List<Task> tasks = ReportSupport.tasksAssignedIn(config, group);
        tasks.forEach(t -> printTaskTable(out, group, t, results));
        printGroupSummary(out, group, tasks, results, activity);
        printCheckpointTables(out, group, tasks, results);
    }

    /**
     * Печатает HTML таблицу с результатами по одной лабораторной для группы.
     *
     * @param out     поток вывода
     * @param group   группа студентов
     * @param task    лабораторная работа
     * @param results результаты всех проверок
     */
    private void printTaskTable(
        PrintStream out, Group group, Task task,
        List<TaskExecutionResult> results
    ) {
        out.println("<h3>Лабораторная " + ReportSupport.esc(task.id())
            + " (" + ReportSupport.esc(task.name()) + ")</h3>");
        out.println("<table><tr>"
            + "<th>Студент</th><th>Сборка</th><th>Документация</th>"
            + "<th>Style guide</th><th>Тесты</th><th>Доп. балл</th>"
            + "<th>Общий балл</th></tr>");
        for (Student s : group.students()) {
            TaskExecutionResult r = ReportSupport.find(results, task.id(), s.github());
            if (r == null) {
                continue;
            }
            GradingResult g = grader.grade(r);
            out.println("<tr>"
                + ReportSupport.td(ReportSupport.esc(s.fullName()))
                + ReportSupport.td(ReportSupport.htmlMark(r.compileOk()))
                + ReportSupport.td(ReportSupport.htmlMark(r.docsOk()))
                + ReportSupport.td(ReportSupport.htmlMark(r.styleOk()))
                + ReportSupport.td(
                r.tests().passed() + "/" + r.tests().failed() + "/" + r.tests().skipped())
                + ReportSupport.td(g.extraPoints())
                + ReportSupport.td(ReportSupport.fmt(g.score()))
                + "</tr>");
        }
        out.println("</table>");
    }

    /**
     * Печатает HTML сводную таблицу группы с суммарными баллами и оценками.
     *
     * @param out               поток вывода
     * @param group             группа студентов
     * @param tasks             список лабораторных
     * @param results           результаты всех проверок
     * @param activityByStudent активность студентов
     */
    private void printGroupSummary(
        PrintStream out, Group group, List<Task> tasks,
        List<TaskExecutionResult> results,
        Map<String, StudentActivity> activityByStudent
    ) {
        out.println(
            "<h3>Общая статистика группы " + ReportSupport.esc(group.name()) + "</h3><table><tr>"
                + "<th>Студент</th>");
        tasks.forEach(t -> out.print("<th>" + ReportSupport.esc(t.id()) + "</th>"));
        out.println("<th>Сумма</th><th>Активность</th><th>Оценка</th></tr>");

        double maxScore = tasks.stream().mapToInt(Task::maxPoints).sum();

        for (Student s : group.students()) {
            List<TaskExecutionResult> sr = ReportSupport.resultsOf(results, s.github());
            StudentActivity a = activityByStudent.getOrDefault(
                s.github(), new StudentActivity(s.github(), 0, 0));

            out.print("<tr>" + ReportSupport.td(ReportSupport.esc(s.fullName())));
            tasks.forEach(t -> {
                TaskExecutionResult r = ReportSupport.find(sr, t.id(), s.github());
                out.print(
                    ReportSupport.td(r == null ? "-" : ReportSupport.fmt(grader.grade(r).score())));
            });
            double total = grader.totalScore(sr);
            double withAct = grader.totalWithActivity(sr, a);
            out.println(ReportSupport.td(ReportSupport.fmt(total))
                + ReportSupport.td(ReportSupport.fmtActivity(a))
                + ReportSupport.td(ReportSupport.esc(grader.gradeFor(withAct, maxScore)))
                + "</tr>");
        }
        out.println("</table>");
    }

    /**
     * Печатает HTML таблицы контрольных точек для группы.
     *
     * @param out     поток вывода
     * @param group   группа студентов
     * @param tasks   список лабораторных
     * @param results результаты всех проверок
     */
    private void printCheckpointTables(
        PrintStream out, Group group, List<Task> tasks,
        List<TaskExecutionResult> results
    ) {
        for (Checkpoint cp : config.checkpoints()) {
            String dateLabel = cp.startDate() != null
                ? cp.startDate() + " \u2013 " + cp.date()
                : cp.date().toString();
            out.println("<h3>Контрольная точка: " + ReportSupport.esc(cp.name())
                + " (" + ReportSupport.esc(dateLabel) + ")</h3>");

            List<String> cpTaskIds = tasks.stream()
                .filter(t -> isInCheckpointRange(t, cp))
                .map(Task::id).toList();
            double maxCp = grader.maxScoreFor(cpTaskIds);

            out.println("<table><tr><th>Студент</th><th>Баллов</th><th>Оценка</th></tr>");
            for (Student s : group.students()) {
                List<TaskExecutionResult> sr = ReportSupport.resultsOf(results, s.github());
                double cpScore = grader.checkpointScore(sr, cp);
                out.println("<tr>"
                    + ReportSupport.td(ReportSupport.esc(s.fullName()))
                    + ReportSupport.td(ReportSupport.fmt(cpScore))
                    + ReportSupport.td(ReportSupport.esc(grader.gradeFor(cpScore, maxCp)))
                    + "</tr>");
            }
            out.println("</table>");
        }
    }
}
