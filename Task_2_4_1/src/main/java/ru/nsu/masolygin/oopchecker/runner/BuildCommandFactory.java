package ru.nsu.masolygin.oopchecker.runner;

import java.nio.file.Path;
import java.util.List;
import ru.nsu.masolygin.oopchecker.runner.buildstrategy.BuildStrategy;
import ru.nsu.masolygin.oopchecker.runner.buildstrategy.GradleBuildStrategy;
import ru.nsu.masolygin.oopchecker.runner.buildstrategy.MavenBuildStrategy;

/**
 * Определяет систему сборки и строит команды для каждого этапа проверки.
 */
public class BuildCommandFactory {

    private final List<BuildStrategy> strategies = List.of(
        new GradleBuildStrategy(),
        new MavenBuildStrategy()
    );

    /**
     * Определяет систему сборки и возвращает стратегию построения команд.
     *
     * @param dir каталог проекта
     * @return стратегия сборки
     * @throws IllegalStateException если ни одна система сборки не найдена
     */
    public BuildStrategy detect(Path dir) {
        return strategies.stream()
            .filter(strategy -> strategy.isApplicable(dir))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No build system found in " + dir));
    }

}
