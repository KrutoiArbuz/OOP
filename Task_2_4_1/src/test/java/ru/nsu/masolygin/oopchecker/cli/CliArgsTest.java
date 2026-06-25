package ru.nsu.masolygin.oopchecker.cli;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class CliArgsTest {

    @Test
    void parsesEmptyArgsToDefaults() {
        CliArgs args = CliArgs.parse(new String[]{});
        assertAll(
            () -> assertEquals("", args.command()),
            () -> assertEquals(CliArgs.DEFAULT_WORK_DIR, args.workDir()),
            () -> assertEquals(CliArgs.DEFAULT_CONFIG_FILE, args.configFile()),
            () -> assertFalse(args.textOutput()),
            () -> assertNull(args.studentFilter()),
            () -> assertNull(args.outputFile())
        );
    }

    @Test
    void firstNonFlagBecomesCommand() {
        CliArgs args = CliArgs.parse(new String[]{"--text", "test", "--student=alice"});
        assertEquals("test", args.command());
    }

    @Test
    void missingCommandIsEmptyString() {
        CliArgs args = CliArgs.parse(new String[]{"--text"});
        assertEquals("", args.command());
    }

    @Test
    void textFlagSetsTextOutput() {
        assertTrue(CliArgs.parse(new String[]{"--text"}).textOutput());
    }

    @Test
    void absenceOfTextFlagDefaultsToHtml() {
        assertFalse(CliArgs.parse(new String[]{"test"}).textOutput());
    }

    @Test
    void studentFlagPopulatesFilter() {
        CliArgs args = CliArgs.parse(new String[]{"--student=ivanov", "test"});
        assertEquals("ivanov", args.studentFilter());
    }

    @Test
    void dirFlagOverridesWorkDir() {
        CliArgs args = CliArgs.parse(new String[]{"--dir=/tmp/work", "test"});
        assertEquals(Paths.get("/tmp/work"), args.workDir());
    }

    @Test
    void configFlagOverridesConfigFile() {
        CliArgs args = CliArgs.parse(new String[]{"--config=my.groovy", "test"});
        assertEquals(Paths.get("my.groovy"), args.configFile());
    }

    @Test
    void outFlagPopulatesOutputFile() {
        CliArgs args = CliArgs.parse(new String[]{"--out=report.html", "test"});
        assertEquals(Paths.get("report.html"), args.outputFile());
    }

    @Test
    void allFlagsCombineCorrectly() {
        String[] argv = {"--text", "--dir=w", "--student=alice", "--out=r.txt", "test"};
        CliArgs a = CliArgs.parse(argv);
        assertAll(
            () -> assertEquals("test", a.command()),
            () -> assertEquals(Path.of("w"), a.workDir()),
            () -> assertEquals(Path.of("r.txt"), a.outputFile()),
            () -> assertEquals("alice", a.studentFilter()),
            () -> assertTrue(a.textOutput())
        );
    }

    @Test
    void unknownFlagIsIgnored() {
        CliArgs a = CliArgs.parse(new String[]{"--garbage=foo", "test"});
        assertEquals("test", a.command());
    }
}
