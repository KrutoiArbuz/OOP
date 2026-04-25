package ru.nsu.masolygin.oopchecker.dsl;

import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.codehaus.groovy.control.CompilerConfiguration;
import ru.nsu.masolygin.oopchecker.domain.CourseConfig;

/**
 * Делегат корневого скрипта, предоставляющий методы {@code course} и {@code importConfig}.
 */
public class RootDelegate {

    private final CourseConfig config;
    private final Path baseDir;

    /**
     * Конструктор.
     *
     * @param config  конфигурация курса, в которую пишутся все данные
     * @param baseDir каталог скрипта, относительно которого разрешаются импорты
     */
    public RootDelegate(CourseConfig config, Path baseDir) {
        this.config = Objects.requireNonNull(config);
        this.baseDir = baseDir;
    }

    /**
     * Выполняет тело блока {@code course { ... }} с соответствующим делегатом.
     *
     * @param body тело блока
     */
    public void course(Closure<?> body) {
        DslSupport.runClosure(body, new CourseDelegate(config));
    }

    /**
     * Подключает другой DSL-файл, добавляя его данные в ту же {@link CourseConfig}.
     *
     * @param relativePath путь к импортируемому файлу относительно текущего скрипта
     * @throws IOException при ошибке чтения файла
     */
    public void importConfig(String relativePath) throws IOException {
        Path target = baseDir != null
            ? baseDir.resolve(relativePath)
            : Paths.get(relativePath);

        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(DelegatingScript.class.getName());
        GroovyShell shell = new GroovyShell(cc);
        DelegatingScript imported = (DelegatingScript) shell.parse(
            Files.readString(target), target.getFileName().toString());
        imported.setDelegate(new RootDelegate(config, target.getParent()));
        imported.run();
    }

    /**
     * Возвращает текущую конфигурацию курса.
     *
     * @return конфигурация курса
     */
    public CourseConfig getConfig() {
        return config;
    }
}
