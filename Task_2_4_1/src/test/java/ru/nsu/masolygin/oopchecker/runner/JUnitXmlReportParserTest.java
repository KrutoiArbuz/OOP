package ru.nsu.masolygin.oopchecker.runner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class JUnitXmlReportParserTest {

    private static final String TESTSUITE_XML = """
        <?xml version="1.0" encoding="UTF-8"?>
        <testsuite name="com.example.SomeTest" tests="5" failures="1" errors="0" skipped="1">
        </testsuite>
        """;

    private static final String TESTSUITES_XML = """
        <?xml version="1.0" encoding="UTF-8"?>
        <testsuites>
          <testsuite name="Suite1" tests="3" failures="0" errors="0" skipped="0"/>
          <testsuite name="Suite2" tests="4" failures="1" errors="1" skipped="0"/>
        </testsuites>
        """;

    private JUnitXmlReportParser parser;

    @BeforeEach
    void setUp() {
        parser = new JUnitXmlReportParser();
    }

    @Test
    void parsesSingleTestsuite(@TempDir Path tmp) throws IOException {
        Path xml = tmp.resolve("TEST-A.xml");
        Files.writeString(xml, TESTSUITE_XML);

        TestReport r = parser.parseXmlFile(xml);

        assertEquals(3, r.passed());
        assertEquals(1, r.failed());
        assertEquals(1, r.skipped());
    }

    @Test
    void parsesTestsuitesWrapper(@TempDir Path tmp) throws IOException {
        Path xml = tmp.resolve("TEST-Suites.xml");
        Files.writeString(xml, TESTSUITES_XML);

        TestReport r = parser.parseXmlFile(xml);

        assertEquals(5, r.passed());
        assertEquals(2, r.failed());
        assertEquals(0, r.skipped());
    }

    @Test
    void malformedXmlYieldsEmpty(@TempDir Path tmp) throws IOException {
        Path xml = tmp.resolve("BROKEN.xml");
        Files.writeString(xml, "this is not xml at all <<<");
        assertEquals(TestReport.EMPTY, parser.parseXmlFile(xml));
    }

    @Test
    void unknownRootElementYieldsEmpty(@TempDir Path tmp) throws IOException {
        Path xml = tmp.resolve("OTHER.xml");
        Files.writeString(xml, "<?xml version=\"1.0\"?><other/>");
        assertEquals(TestReport.EMPTY, parser.parseXmlFile(xml));
    }

    @Test
    void missingFileYieldsEmpty(@TempDir Path tmp) {
        assertEquals(TestReport.EMPTY, parser.parseXmlFile(tmp.resolve("nope.xml")));
    }

    @Test
    void zeroTestsAttributesYieldEmpty(@TempDir Path tmp) throws IOException {
        Path xml = tmp.resolve("EMPTY.xml");
        Files.writeString(xml, "<?xml version=\"1.0\"?>"
            + "<testsuite tests=\"0\" failures=\"0\" errors=\"0\" skipped=\"0\"/>");
        assertEquals(TestReport.EMPTY, parser.parseXmlFile(xml));
    }

    @Test
    void nonNumericAttributesAreTreatedAsZero(@TempDir Path tmp) throws IOException {
        Path xml = tmp.resolve("WEIRD.xml");
        Files.writeString(xml, "<?xml version=\"1.0\"?>"
            + "<testsuite tests=\"abc\" failures=\"2\" errors=\"0\" skipped=\"0\"/>");
        TestReport r = parser.parseXmlFile(xml);
        assertEquals(0, r.passed());
        assertEquals(2, r.failed());
    }

    @Test
    void missingAttributesAreTreatedAsZero(@TempDir Path tmp) throws IOException {
        Path xml = tmp.resolve("PARTIAL.xml");
        Files.writeString(xml, "<?xml version=\"1.0\"?><testsuite tests=\"3\"/>");
        TestReport r = parser.parseXmlFile(xml);
        assertEquals(3, r.passed());
        assertEquals(0, r.failed());
        assertEquals(0, r.skipped());
    }

    @Test
    void errorsAndFailuresAreSummedIntoFailed(@TempDir Path tmp) throws IOException {
        Path xml = tmp.resolve("ERR.xml");
        Files.writeString(xml,
            "<?xml version=\"1.0\"?>"
                + "<testsuite tests=\"5\" failures=\"2\" errors=\"1\" skipped=\"0\"/>");
        TestReport r = parser.parseXmlFile(xml);
        assertEquals(2, r.passed());
        assertEquals(3, r.failed());
    }

    @Test
    void passedNeverGoesNegative(@TempDir Path tmp) throws IOException {
        Path xml = tmp.resolve("BAD.xml");
        Files.writeString(xml,
            "<?xml version=\"1.0\"?>"
                + "<testsuite tests=\"1\" failures=\"5\" errors=\"0\" skipped=\"0\"/>");
        assertEquals(0, parser.parseXmlFile(xml).passed());
    }
}
