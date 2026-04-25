package ru.nsu.masolygin.oopchecker.dsl;

import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.codehaus.groovy.control.CompilerConfiguration;
import ru.nsu.masolygin.oopchecker.domain.CourseConfig;

/**
 * Парсер DSL конфигурации курса на основе Groovy.
 */
public class DslParser {

    public static final String DEFAULT_SCRIPT_NAME = "config/oopchecker.groovy";

    /**
     * Парсит основной конфиг из указанного каталога.
     *
     * @param workingDir рабочий каталог
     * @return конфигурация курса
     * @throws IOException при ошибке чтения файла
     */
    public CourseConfig parseWorkingDir(Path workingDir) throws IOException {
        return parseFile(workingDir.resolve(DEFAULT_SCRIPT_NAME));
    }

    /**
     * Парсит файл конфигурации.
     *
     * @param scriptFile путь к файлу конфигурации
     * @return конфигурация курса
     * @throws IOException при ошибке чтения файла
     */
    public CourseConfig parseFile(Path scriptFile) throws IOException {
        String source = Files.readString(scriptFile);
        Path baseDir = scriptFile.toAbsolutePath().getParent();
        return parseSource(source, scriptFile.getFileName().toString(), baseDir);
    }

    /**
     * Парсит конфигурацию из строки с текущим каталогом как baseDir.
     *
     * @param source исходник DSL
     * @return конфигурация курса
     */
    public CourseConfig parseSource(String source) {
        return parseSource(source, "script.groovy", Paths.get("."));
    }

    /**
     * Парсит конфигурацию из строки с заданным именем и базовым каталогом.
     *
     * @param source     исходник DSL
     * @param scriptName имя скрипта для диагностики ошибок
     * @param baseDir    базовый каталог для importConfig
     * @return конфигурация курса
     */
    public CourseConfig parseSource(String source, String scriptName, Path baseDir) {
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(DelegatingScript.class.getName());

        GroovyShell shell = new GroovyShell(cc);
        DelegatingScript script = (DelegatingScript) shell.parse(source, scriptName);

        CourseConfig config = new CourseConfig();
        script.setDelegate(new RootDelegate(config, baseDir));
        script.run();
        return config;
    }
}
