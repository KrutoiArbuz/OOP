package ru.nsu.masolygin.oopchecker.dsl;

import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.codehaus.groovy.control.CompilerConfiguration;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfigBuilder;

/**
 * Делегат корневого скрипта, предоставляющий методы {@code course} и {@code importConfig}.
 */
public class RootDelegate {

    private final CourseConfigBuilder builder;
    private final Path baseDir;

    /**
     * Конструктор.
     *
     * @param baseDir каталог скрипта, относительно которого разрешаются импорты
     */
    public RootDelegate(CourseConfigBuilder builder, Path baseDir) {
        this.builder = builder;
        this.baseDir = baseDir;
    }

    /**
     * Выполняет тело блока {@code course { ... }} с соответствующим делегатом.
     *
     * @param body тело блока
     */
    public void course(Closure<?> body) {
        ClosureBinder.bindAndCall(body, new CourseDelegate(builder));
    }

    /**
     * Загружает и выполняет вложенный конфиг DSL относительно текущего скрипта.
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
        imported.setDelegate(new RootDelegate(builder, target.getParent()));
        imported.run();
    }

}
