package ru.nsu.masolygin.oopchecker.runner.buildstrategy;

import java.nio.file.Path;
import java.util.List;

/**
 * Стратегия построения команд для конкретной системы сборки (Gradle, Maven и т.д.).
 */
public interface BuildStrategy {

    /**
     * Проверяет, применима ли стратегия к проекту в указанном каталоге.
     *
     * @param dir каталог проекта
     * @return true если система сборки обнаружена
     */
    boolean isApplicable(Path dir);

    /**
     * @param dir каталог проекта
     * @return команда компиляции
     */
    List<String> compileCmd(Path dir);

    /**
     * @param dir каталог проекта
     * @return команда генерации документации
     */
    List<String> docsCmd(Path dir);

    /**
     * @param dir каталог проекта
     * @return команда проверки стиля
     */
    List<String> styleCmd(Path dir);

    /**
     * @param dir каталог проекта
     * @return команда запуска тестов
     */
    List<String> testCmd(Path dir);
}