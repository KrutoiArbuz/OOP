package ru.nsu.masolygin.oopchecker.report;

import java.io.PrintStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import ru.nsu.masolygin.oopchecker.domain.Checkpoint;
import ru.nsu.masolygin.oopchecker.domain.Group;
import ru.nsu.masolygin.oopchecker.domain.Task;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfig;
import ru.nsu.masolygin.oopchecker.grader.Grader;
import ru.nsu.masolygin.oopchecker.grader.StudentActivity;
import ru.nsu.masolygin.oopchecker.runner.TaskExecutionResult;

/**
 * Базовый класс отчёта: обход групп/задач, рендеринг делегируется подклассам.
 */
public abstract class AbstractReport implements Report {

    protected final CourseConfig config;
    protected final Grader grader;

    public AbstractReport(CourseConfig config, Grader grader) {
        this.config = Objects.requireNonNull(config, "config must not be null");
        this.grader = Objects.requireNonNull(grader, "grader must not be null");
    }

    @Override
    public final void print(
        List<TaskExecutionResult> results,
        Map<String, StudentActivity> activity,
        PrintStream out
    ) {
        ReportData data = new ReportData(results);
        printDocumentHeader(out);

        for (Group group : config.groups()) {
            printGroupHeader(out, group);

            List<Task> tasks = config.tasksFor(group);
            for (Task task : tasks) {
                printTaskTable(out, group, task, data);
            }

            printGroupSummary(out, group, tasks, data, activity);
            printCheckpointTables(out, group, tasks, data);

            printGroupFooter(out, group);
        }

        printDocumentFooter(out);
    }

    /**
     * Форматирует число баллов: целое без дробной части, иначе 1 знак.
     *
     * @param score баллы
     * @return отформатированное значение
     */
    protected String fmt(double score) {
        if (Math.abs(score - Math.rint(score)) < 1e-9) {
            return String.valueOf((int) Math.rint(score));
        }
        return String.format(Locale.ROOT, "%.1f", score);
    }

    /**
     * Форматирует активность студента как процент или '-'.
     *
     * @param activity активность студента
     * @return строка с процентом или '-'
     */
    protected String fmtActivity(StudentActivity activity) {
        return activity.totalWeeks() == 0 ? "-"
            : (int) Math.round(activity.ratio() * 100) + "%";
    }

    /**
     * Диапазон дат контрольной точки: "startDate – date" или просто "date".
     *
     * @param cp контрольная точка
     * @return строка с диапазоном дат
     */
    protected String fmtDateRange(Checkpoint cp) {
        return cp.startDate()
            .map(start -> start + " – " + cp.date())
            .orElse(cp.date().toString());
    }

    protected abstract void printDocumentHeader(PrintStream out);

    protected abstract void printDocumentFooter(PrintStream out);

    protected abstract void printGroupHeader(PrintStream out, Group group);

    protected abstract void printGroupFooter(PrintStream out, Group group);

    protected abstract void printTaskTable(PrintStream out, Group group, Task task,
        ReportData data);

    protected abstract void printGroupSummary(PrintStream out, Group group, List<Task> tasks,
        ReportData data, Map<String, StudentActivity> activity);

    protected abstract void printCheckpointTables(PrintStream out, Group group, List<Task> tasks,
        ReportData data);
}
