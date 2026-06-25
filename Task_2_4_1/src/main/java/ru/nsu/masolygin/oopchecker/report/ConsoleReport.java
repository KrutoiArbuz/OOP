package ru.nsu.masolygin.oopchecker.report;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import ru.nsu.masolygin.oopchecker.domain.Checkpoint;
import ru.nsu.masolygin.oopchecker.domain.Group;
import ru.nsu.masolygin.oopchecker.domain.Student;
import ru.nsu.masolygin.oopchecker.domain.Task;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfig;
import ru.nsu.masolygin.oopchecker.grader.Grader;
import ru.nsu.masolygin.oopchecker.grader.GradingResult;
import ru.nsu.masolygin.oopchecker.grader.StudentActivity;

/**
 * Текстовый отчёт в формате ASCII-таблиц для вывода в консоль.
 */
public class ConsoleReport extends AbstractReport {

    public ConsoleReport(CourseConfig config, Grader grader) {
        super(config, grader);
    }

    @Override
    protected void printDocumentHeader(PrintStream out) {
    }

    @Override
    protected void printDocumentFooter(PrintStream out) {
    }

    @Override
    protected void printGroupHeader(PrintStream out, Group group) {
        out.println("Группа " + group.name());
        out.println();
    }

    @Override
    protected void printGroupFooter(PrintStream out, Group group) {
        out.println();
    }

    @Override
    protected void printTaskTable(PrintStream out, Group group, Task task, ReportData data) {
        out.println("Лабораторная " + task.id() + " (" + task.name() + ")");
        TextTable table = new TextTable(List.of(
            "Студент", "Сборка", "Документация", "Style guide",
            "Тесты", "Доп. балл", "Общий балл"
        ));
        for (Student student : group.students()) {
            data.find(task.id(), student.github()).ifPresent(r -> {
                GradingResult g = grader.grade(r);
                table.addRow(List.of(
                    student.fullName(),
                    textMark(r.compileOk()),
                    textMark(r.docsOk()),
                    textMark(r.styleOk()),
                    r.tests().passed() + "/" + r.tests().failed() + "/" + r.tests().skipped(),
                    String.valueOf(g.extraPoints()),
                    fmt(g.score())
                ));
            });
        }
        out.print(table.render());
        out.println();
    }

    @Override
    protected void printGroupSummary(PrintStream out, Group group, List<Task> tasks,
        ReportData data, Map<String, StudentActivity> activity) {
        out.println("Общая статистика группы " + group.name());

        List<String> headers = new ArrayList<>();
        headers.add("Студент");
        tasks.forEach(t -> headers.add(t.id()));
        headers.addAll(List.of("Сумма", "Активность", "Оценка"));
        TextTable table = new TextTable(headers);

        for (Student student : group.students()) {
            List<String> taskScores = tasks.stream()
                .map(t -> data.find(t.id(), student.github())
                    .map(r -> fmt(grader.grade(r).score()))
                    .orElse("-"))
                .toList();

            StudentActivity act = activity.getOrDefault(student.github(),
                StudentActivity.absent(student.github()));
            double total = grader.totalScore(data.of(student.github()));
            double withAct = grader.totalWithActivity(data.of(student.github()), act);

            List<String> row = Stream.concat(
                Stream.concat(Stream.of(student.fullName()), taskScores.stream()),
                Stream.of(fmt(total), fmtActivity(act), grader.gradeFor(withAct))
            ).toList();
            table.addRow(row);
        }
        out.print(table.render());
    }

    @Override
    protected void printCheckpointTables(PrintStream out, Group group, List<Task> tasks,
        ReportData data) {
        for (Checkpoint cp : config.checkpoints()) {
            String dateLabel = fmtDateRange(cp);
            out.println();
            out.println("Контрольная точка: " + cp.name() + " (" + dateLabel + ")");

            TextTable table = new TextTable(List.of("Студент", "Баллов", "Оценка"));
            for (Student student : group.students()) {
                double cpScore = grader.checkpointScore(data.of(student.github()), cp);
                table.addRow(List.of(
                    student.fullName(),
                    fmt(cpScore),
                    grader.gradeFor(cpScore)
                ));
            }
            out.print(table.render());
        }
    }

    private String textMark(boolean ok) {
        return ok ? "+" : "-";
    }
}
