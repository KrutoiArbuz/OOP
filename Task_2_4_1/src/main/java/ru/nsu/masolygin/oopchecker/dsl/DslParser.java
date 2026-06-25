package ru.nsu.masolygin.oopchecker.dsl;

import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.codehaus.groovy.control.CompilerConfiguration;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfig;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfigBuilder;


/**
 * Парсер DSL конфигурации курса на основе Groovy.
 */
public class DslParser {

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

        CourseConfigBuilder builder = new CourseConfigBuilder();
        script.setDelegate(new RootDelegate(builder, baseDir));
        script.run();
        return builder.build();
    }
}
