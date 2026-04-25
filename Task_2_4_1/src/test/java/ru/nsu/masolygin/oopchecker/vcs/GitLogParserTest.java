package ru.nsu.masolygin.oopchecker.vcs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class GitLogParserTest {

    private static final String SEP = String.valueOf((char) 0x1F);

    private static final String VALID_LINE =
        "abc123" + SEP + "2024-03-15T10:30:00+00:00"
            + SEP + "Ivan Ivanov" + SEP + "ivan@example.com" + SEP + "Add feature";

    @Test
    void prettyFormatArgContainsPrettyFlag() {
        String arg = GitLogParser.prettyFormatArg();
        assertTrue(arg.startsWith("--pretty=format:"));
    }

    @Test
    void parseManyReturnsEmptyListForBlankInput() {
        List<Commit> commits = GitLogParser.parseMany("   ");
        assertTrue(commits.isEmpty());
    }

    @Test
    void parseManyReturnsEmptyListForEmptyString() {
        List<Commit> commits = GitLogParser.parseMany("");
        assertTrue(commits.isEmpty());
    }

    @Test
    void parseManyParsesOneCommit() {
        List<Commit> commits = GitLogParser.parseMany(VALID_LINE);
        assertEquals(1, commits.size());
        assertEquals("abc123", commits.get(0).hash());
        assertEquals("Ivan Ivanov", commits.get(0).authorName());
        assertEquals("ivan@example.com", commits.get(0).authorEmail());
        assertEquals("Add feature", commits.get(0).subject());
    }

    @Test
    void parsedTimestampIsNotNull() {
        List<Commit> commits = GitLogParser.parseMany(VALID_LINE);
        assertTrue(commits.get(0).timestamp() != null);
    }

    @Test
    void parseManyHandlesMultipleLines() {
        String line2 = "def456" + SEP + "2024-03-16T12:00:00+00:00"
            + SEP + "Petr Petrov" + SEP + "petr@example.com" + SEP + "Fix bug";
        List<Commit> commits = GitLogParser.parseMany(VALID_LINE + "\n" + line2);
        assertEquals(2, commits.size());
        assertEquals("abc123", commits.get(0).hash());
        assertEquals("def456", commits.get(1).hash());
    }

    @Test
    void parseSingleReturnsEmptyForBlankInput() {
        Optional<Commit> commit = GitLogParser.parseSingle("   ");
        assertFalse(commit.isPresent());
    }

    @Test
    void parseSingleReturnsPresentForValidLine() {
        Optional<Commit> commit = GitLogParser.parseSingle(VALID_LINE);
        assertTrue(commit.isPresent());
        assertEquals("abc123", commit.get().hash());
    }

    @Test
    void parseSingleTrimsSurroundingWhitespace() {
        Optional<Commit> commit = GitLogParser.parseSingle("  " + VALID_LINE + "  ");
        assertTrue(commit.isPresent());
    }

    @Test
    void parseManyThrowsOnMalformedLine() {
        assertThrows(Exception.class,
            () -> GitLogParser.parseMany("tooshort" + SEP + "only2fields"));
    }

    @Test
    void commitSubjectIsEmptyWhenNotProvided() {
        String lineWithoutSubject = "abc123" + SEP + "2024-03-15T10:30:00+00:00"
            + SEP + "Ivan" + SEP + "ivan@example.com";
        List<Commit> commits = GitLogParser.parseMany(lineWithoutSubject);
        assertEquals(1, commits.size());
        assertEquals("", commits.get(0).subject());
    }
}
