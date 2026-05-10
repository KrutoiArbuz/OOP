package ru.nsu.masolygin.oopchecker.runner;

import java.io.IOException;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Парсит JUnit XML-отчёты в TestReport.
 */
public class JUnitXmlReportParser {

    /**
     * Парсит XML-файл отчёта.
     *
     * @param xmlFile путь к файлу
     * @return отчёт или {@link TestReport#EMPTY} при ошибке парсинга
     */
    public TestReport parseXmlFile(Path xmlFile) {
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
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println("[xml-parser] malformed report " + xmlFile + ": " + e.getMessage());
        }
        return TestReport.EMPTY;
    }

    /**
     * Парсит элемент testsuite и извлекает статистику тестов.
     *
     * @param suite элемент testsuite
     * @return отчёт с количеством пройденных, провалившихся и пропущенных тестов
     */
    private TestReport parseSuite(Element suite) {
        int total = attrInt(suite, "tests");
        int failures = attrInt(suite, "failures");
        int errors = attrInt(suite, "errors");
        int skipped = attrInt(suite, "skipped");
        int passed = Math.max(0, total - failures - errors - skipped);
        return new TestReport(passed, failures + errors, skipped);
    }

    /**
     * Целочисленный атрибут элемента; 0 если отсутствует или не число.
     *
     * @param el   XML-элемент
     * @param attr имя атрибута
     * @return значение атрибута или 0
     */
    private int attrInt(Element el, String attr) {
        String value = el.getAttribute(attr);
        if (value.isBlank()) {
            return 0;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
