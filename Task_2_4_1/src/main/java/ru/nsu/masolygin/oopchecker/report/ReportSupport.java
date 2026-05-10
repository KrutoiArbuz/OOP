package ru.nsu.masolygin.oopchecker.report;

import java.util.List;
import java.util.Objects;
import ru.nsu.masolygin.oopchecker.domain.CourseConfig;
import ru.nsu.masolygin.oopchecker.domain.Group;
import ru.nsu.masolygin.oopchecker.domain.Task;
import ru.nsu.masolygin.oopchecker.grader.StudentActivity;
import ru.nsu.masolygin.oopchecker.runner.TaskExecutionResult;

/**
 * Общие утилиты для генерации отчётов (текстовых и HTML).
 */
final class ReportSupport {

    private ReportSupport() {
    }

    /**
     * Возвращает список лабораторных, назначенных студентам группы.
     *
     * @param config конфигурация курса
     * @param group  группа студентов
     * @return список уникальных лабораторных из заданий группы
     */
    static List<Task> tasksAssignedIn(CourseConfig config, Group group) {
        return config.assignments().stream()
            .filter(a -> group.students().stream()
                .anyMatch(s -> s.github().equals(a.studentGithub())))
            .map(a -> config.findTask(a.taskId()).orElse(null))
            .filter(Objects::nonNull)
            .distinct()
            .toList();
    }

    /**
     * Фильтрует результаты по GitHub нику студента.
     *
     * @param all    все результаты
     * @param github GitHub ник студента
     * @return результаты этого студента
     */
    static List<TaskExecutionResult> resultsOf(List<TaskExecutionResult> all, String github) {
        return all.stream().filter(r -> r.studentGithub().equals(github)).toList();
    }

    /**
     * Ищет результат по ID задачи и GitHub нику.
     *
     * @param results список результатов
     * @param taskId  ID задачи
     * @param github  GitHub ник студента
     * @return результат или null
     */
    static TaskExecutionResult find(
        List<TaskExecutionResult> results, String taskId, String github
    ) {
        return results.stream()
            .filter(r -> r.taskId().equals(taskId) && r.studentGithub().equals(github))
            .findFirst().orElse(null);
    }

    /**
     * Преобразует boolean в текстовый знак.
     *
     * @param ok статус проверки
     * @return '+' если ok, иначе '-'
     */
    static String textMark(boolean ok) {
        return ok ? "+" : "-";
    }

    /**
     * Преобразует boolean в HTML span с знаком и классом стиля.
     *
     * @param ok статус проверки
     * @return HTML span с '+' или '-'
     */
    static String htmlMark(boolean ok) {
        return ok ? "<span class=\"ok\">+</span>" : "<span class=\"bad\">-</span>";
    }

    /**
     * Форматирует число баллов (целое число без дробной части, иначе 1 знак).
     *
     * @param score баллы
     * @return отформатированное значение
     */
    static String fmt(double score) {
        if (Math.abs(score - Math.rint(score)) < 1e-9) {
            return String.valueOf((int) Math.rint(score));
        }
        return String.format(java.util.Locale.ROOT, "%.1f", score);
    }

    /**
     * Форматирует активность как процент или '-'.
     *
     * @param a активность студента
     * @return строка с процентом или '-'
     */
    static String fmtActivity(StudentActivity a) {
        return a.totalWeeks() == 0 ? "-"
            : (int) Math.round(a.ratio() * 100) + "%";
    }

    /**
     * Оборачивает содержимое в HTML тег td.
     *
     * @param content содержимое ячейки
     * @return HTML td элемент
     */
    static String td(Object content) {
        return "<td>" + content + "</td>";
    }

    /**
     * Экранирует строку для безопасного использования в HTML.
     *
     * @param s исходная строка
     * @return экранированная строка
     */
    static String esc(String s) {
        return s.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;");
    }
}
