package ru.nsu.masolygin.oopchecker.report;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import ru.nsu.masolygin.oopchecker.grader.StudentActivity;
import ru.nsu.masolygin.oopchecker.runner.TaskExecutionResult;

/**
 * Контракт отчёта: принимает результаты проверок и активность, печатает в поток.
 */
public interface Report {

    /**
     * Печатает отчёт в указанный поток.
     *
     * @param results  результаты проверки задач
     * @param activity активность студентов за семестр
     * @param out      поток вывода
     */
    void print(
        List<TaskExecutionResult> results,
        Map<String, StudentActivity> activity,
        PrintStream out
    );
}