package ru.nsu.masolygin.oopchecker.runner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Парсит JUnit XML отчёты и агрегирует статистику тестов из стандартных директорий результатов.
 */
final class JUnitXmlReportParser {

    private JUnitXmlReportParser() {
    }

    /**
     * Парсит и агрегирует статистику тестов из всех JUnit XML файлов в стандартных директориях.
     *
     * @param labDir каталог лабораторной работы
     * @param bs     система сборки (определяет путь к результатам)
     * @return агрегированный отчёт или пустой отчёт если файлов нет
     */
    static TestReport parse(Path labDir, BuildCommandFactory.BuildSystem bs) {
        Path resultsRoot = bs == BuildCommandFactory.BuildSystem.GRADLE
            ? labDir.resolve("build/test-results")
            : labDir.resolve("target/surefire-reports");

        if (!Files.exists(resultsRoot)) {
            return TestReport.EMPTY;
        }

        int passed = 0;
        int failed = 0;
        int skipped = 0;
        try {
            List<Path> xmlFiles = Files.walk(resultsRoot)
                .filter(p -> p.toString().endsWith(".xml"))
                .toList();
            for (Path xml : xmlFiles) {
                TestReport r = parseXmlFile(xml);
                passed += r.passed();
                failed += r.failed();
                skipped += r.skipped();
            }
        } catch (IOException ignored) {
        }
        return new TestReport(passed, failed, skipped);
    }

    /**
     * Парсит отдельный XML файл с результатами тестов.
     *
     * @param xmlFile путь к XML файлу
     * @return отчёт или пустой отчёт при ошибке парсинга
     */
    private static TestReport parseXmlFile(Path xmlFile) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(xmlFile.toFile());
            Element root = doc.getDocumentElement();

            if ("testsuites".equals(root.getTagName())) {
                NodeList suites = root.getElementsByTagName("testsuite");
                int p = 0;
                int f = 0;
                int s = 0;
                for (int i = 0; i < suites.getLength(); i++) {
                    if (suites.item(i) instanceof Element el) {
                        TestReport r = parseSuite(el);
                        p += r.passed();
                        f += r.failed();
                        s += r.skipped();
                    }
                }
                return new TestReport(p, f, s);
            }
            if ("testsuite".equals(root.getTagName())) {
                return parseSuite(root);
            }
        } catch (Exception ignored) {
        }
        return TestReport.EMPTY;
    }

    /**
     * Парсит элемент testsuite и извлекает статистику тестов.
     *
     * @param suite элемент testsuite
     * @return отчёт с количеством пройденных, провалившихся и пропущенных тестов
     */
    private static TestReport parseSuite(Element suite) {
        int total = attrInt(suite, "tests", 0);
        int failures = attrInt(suite, "failures", 0);
        int errors = attrInt(suite, "errors", 0);
        int skipped = attrInt(suite, "skipped", 0);
        int passed = Math.max(0, total - failures - errors - skipped);
        return new TestReport(passed, failures + errors, skipped);
    }

    /**
     * Извлекает целое число из атрибута XML элемента.
     *
     * @param el   XML элемент
     * @param attr название атрибута
     * @param def  значение по умолчанию при отсутствии или ошибке парсинга
     * @return целое число или значение по умолчанию
     */
    private static int attrInt(Element el, String attr, int def) {
        String value = el.getAttribute(attr);
        if (value == null || value.isBlank()) {
            return def;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
