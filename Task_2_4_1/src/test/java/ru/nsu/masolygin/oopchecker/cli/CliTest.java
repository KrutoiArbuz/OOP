package ru.nsu.masolygin.oopchecker.cli;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CliTest {

    private final PrintStream originalErr = System.err;
    private ByteArrayOutputStream capturedErr;

    @BeforeEach
    void redirectErr() {
        capturedErr = new ByteArrayOutputStream();
        System.setErr(new PrintStream(capturedErr, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restoreErr() {
        System.setErr(originalErr);
    }

    @Test
    void emptyArgsPrintsUsage() throws IOException {
        new Cli(CliArgs.parse(new String[]{})).run();
        String err = capturedErr.toString(StandardCharsets.UTF_8);
        assertTrue(err.contains("Usage"));
    }

    @Test
    void unknownCommandPrintsErrorAndUsage() throws IOException {
        new Cli(CliArgs.parse(new String[]{"unknown"})).run();
        String err = capturedErr.toString(StandardCharsets.UTF_8);
        assertTrue(err.contains("unknown command"));
        assertTrue(err.contains("Usage"));
    }

    @Test
    void testCommandWithoutConfigThrows(@TempDir Path tmp) {
        String[] argv = {
            "--dir=" + tmp.resolve("work"),
            "--config=" + tmp.resolve("nope.groovy"),
            "test"
        };
        IllegalStateException e = assertThrows(IllegalStateException.class,
            () -> new Cli(CliArgs.parse(argv)).run());
        assertTrue(e.getMessage().contains("config not found"));
    }
}
