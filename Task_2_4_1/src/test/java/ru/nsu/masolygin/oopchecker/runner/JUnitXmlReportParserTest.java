package ru.nsu.masolygin.oopchecker.runner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class JunitXmlReportParserTest {

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

    private static final String MALFORMED_XML = "this is not xml at all <<<";

    @Test
    void returnsEmptyWhenResultsDirMissing(@TempDir Path tmp) {
        TestReport r = JunitXmlReportParser.parse(tmp, BuildCommandFactory.BuildSystem.GRADLE);
        assertEquals(TestReport.EMPTY, r);
    }

    @Test
    void parsesGradleTestsuiteSingleFile(@TempDir Path tmp) throws IOException {
        Path resultsDir = tmp.resolve("build/test-results/test");
        Files.createDirectories(resultsDir);
        Files.writeString(resultsDir.resolve("TEST-SomeTest.xml"), TESTSUITE_XML);

        TestReport r = JunitXmlReportParser.parse(tmp, BuildCommandFactory.BuildSystem.GRADLE);

        assertEquals(3, r.passed());
        assertEquals(1, r.failed());
        assertEquals(1, r.skipped());
    }

    @Test
    void parsesTestsuitesWrapper(@TempDir Path tmp) throws IOException {
        Path resultsDir = tmp.resolve("build/test-results/test");
        Files.createDirectories(resultsDir);
        Files.writeString(resultsDir.resolve("TEST-Suites.xml"), TESTSUITES_XML);

        TestReport r = JunitXmlReportParser.parse(tmp, BuildCommandFactory.BuildSystem.GRADLE);

        assertEquals(5, r.passed());
        assertEquals(2, r.failed());
        assertEquals(0, r.skipped());
    }

    @Test
    void parsesMavenSurefireReports(@TempDir Path tmp) throws IOException {
        Path resultsDir = tmp.resolve("target/surefire-reports");
        Files.createDirectories(resultsDir);
        Files.writeString(resultsDir.resolve("TEST-SomeTest.xml"), TESTSUITE_XML);

        TestReport r = JunitXmlReportParser.parse(tmp, BuildCommandFactory.BuildSystem.MAVEN);
        assertEquals(3, r.passed());
    }

    @Test
    void malformedXmlYieldsEmptyReport(@TempDir Path tmp) throws IOException {
        Path resultsDir = tmp.resolve("build/test-results/test");
        Files.createDirectories(resultsDir);
        Files.writeString(resultsDir.resolve("BROKEN.xml"), MALFORMED_XML);

        TestReport r = JunitXmlReportParser.parse(tmp, BuildCommandFactory.BuildSystem.GRADLE);
        assertEquals(0, r.passed());
        assertEquals(0, r.failed());
    }

    @Test
    void aggregatesMultipleXmlFiles(@TempDir Path tmp) throws IOException {
        Path resultsDir = tmp.resolve("build/test-results/test");
        Files.createDirectories(resultsDir);
        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <testsuite name="T" tests="4" failures="0" errors="0" skipped="0"/>
            """;
        Files.writeString(resultsDir.resolve("TEST-A.xml"), xml);
        Files.writeString(resultsDir.resolve("TEST-B.xml"), xml);

        TestReport r = JunitXmlReportParser.parse(tmp, BuildCommandFactory.BuildSystem.GRADLE);
        assertEquals(8, r.passed());
    }

    @Test
    void nonXmlFilesAreIgnored(@TempDir Path tmp) throws IOException {
        Path resultsDir = tmp.resolve("build/test-results/test");
        Files.createDirectories(resultsDir);
        Files.writeString(resultsDir.resolve("notes.txt"), "some text");

        TestReport r = JunitXmlReportParser.parse(tmp, BuildCommandFactory.BuildSystem.GRADLE);
        assertEquals(TestReport.EMPTY, r);
    }
}
