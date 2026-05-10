package ru.nsu.masolygin.oopchecker.report;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import ru.nsu.masolygin.oopchecker.domain.Checkpoint;
import ru.nsu.masolygin.oopchecker.domain.Group;
import ru.nsu.masolygin.oopchecker.domain.Student;
import ru.nsu.masolygin.oopchecker.domain.Task;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfig;
import ru.nsu.masolygin.oopchecker.grader.Grader;
import ru.nsu.masolygin.oopchecker.grader.GradingResult;
import ru.nsu.masolygin.oopchecker.grader.StudentActivity;

/**
 * HTML-отчёт с таблицами по группам, задачам и контрольным точкам.
 */
public class HtmlReport extends AbstractReport {

    private static final String STYLE = """
        body { font-family: sans-serif; margin: 2em }
        table { border-collapse: collapse; margin: .5em 0 1.5em }
        th, td { border: 1px solid #999; padding: 4px 10px; text-align: left }
        th { background: #eee }
        .ok { color: #2a7; font-weight: bold }
        .bad { color: #c33; font-weight: bold }
        h2 { margin-top: 2em }
        h3 { margin-top: 1.5em; color: #444 }
        """;

    private static final String HEADER = """
        <!doctype html>
        <html lang="ru">
        <head><meta charset="utf-8"><title>oop-checker report</title>
        <style>%s</style></head><body>""".formatted(STYLE);

    public HtmlReport(CourseConfig config, Grader grader) {
        super(config, grader);
    }

    @Override
    protected void printDocumentHeader(PrintStream out) {
        out.print(HEADER);
    }

    @Override
    protected void printDocumentFooter(PrintStream out) {
        out.println("</body></html>");
    }

    @Override
    protected void printGroupHeader(PrintStream out, Group group) {
        out.println("<h2>Группа " + esc(group.name()) + "</h2>");
    }

    @Override
    protected void printGroupFooter(PrintStream out, Group group) {
    }

    @Override
    protected void printTaskTable(PrintStream out, Group group, Task task, ReportData data) {
        out.println("<h3>Лабораторная " + esc(task.id()) + " (" + esc(task.name()) + ")</h3>");
        out.println("<table><tr>"
            + "<th>Студент</th><th>Сборка</th><th>Документация</th>"
            + "<th>Style guide</th><th>Тесты</th><th>Доп. балл</th>"
            + "<th>Общий балл</th></tr>");

        for (Student student : group.students()) {
            data.find(task.id(), student.github()).ifPresent(r -> {
                GradingResult g = grader.grade(r);
                out.println("<tr>"
                    + td(esc(student.fullName()))
                    + td(htmlMark(r.compileOk()))
                    + td(htmlMark(r.docsOk()))
                    + td(htmlMark(r.styleOk()))
                    + td(r.tests().passed() + "/" + r.tests().failed() + "/" + r.tests().skipped())
                    + td(String.valueOf(g.extraPoints()))
                    + td(fmt(g.score()))
                    + "</tr>");
            });
        }
        out.println("</table>");
    }

    @Override
    protected void printGroupSummary(PrintStream out, Group group, List<Task> tasks,
        ReportData data, Map<String, StudentActivity> activity) {
        out.println("<h3>Общая статистика группы " + esc(group.name())
            + "</h3><table><tr><th>Студент</th>");
        tasks.forEach(t -> out.print("<th>" + esc(t.id()) + "</th>"));
        out.println("<th>Сумма</th><th>Активность</th><th>Оценка</th></tr>");

        for (Student student : group.students()) {
            StudentActivity act = activity.getOrDefault(student.github(),
                StudentActivity.absent(student.github()));

            out.print("<tr>" + td(esc(student.fullName())));
            tasks.forEach(t -> out.print(td(
                data.find(t.id(), student.github())
                    .map(r -> fmt(grader.grade(r).score()))
                    .orElse("-")
            )));
            double total = grader.totalScore(data.of(student.github()));
            double withAct = grader.totalWithActivity(data.of(student.github()), act);
            out.println(td(fmt(total))
                + td(fmtActivity(act))
                + td(esc(grader.gradeFor(withAct)))
                + "</tr>");
        }
        out.println("</table>");
    }

    @Override
    protected void printCheckpointTables(PrintStream out, Group group, List<Task> tasks,
        ReportData data) {
        for (Checkpoint cp : config.checkpoints()) {
            String dateLabel = fmtDateRange(cp);
            out.println(
                "<h3>Контрольная точка: " + esc(cp.name()) + " (" + esc(dateLabel) + ")</h3>");

            out.println("<table><tr><th>Студент</th><th>Баллов</th><th>Оценка</th></tr>");
            for (Student student : group.students()) {
                double cpScore = grader.checkpointScore(data.of(student.github()), cp);
                out.println("<tr>"
                    + td(esc(student.fullName()))
                    + td(fmt(cpScore))
                    + td(esc(grader.gradeFor(cpScore)))
                    + "</tr>");
            }
            out.println("</table>");
        }
    }

    private String htmlMark(boolean ok) {
        return ok ? "<span class=\"ok\">+</span>" : "<span class=\"bad\">-</span>";
    }

    private String td(String content) {
        return "<td>" + content + "</td>";
    }

    private String esc(String s) {
        return s.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;");
    }
}
