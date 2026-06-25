package ru.nsu.masolygin.oopchecker.vcs;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GitLogParserTest {

    private static final String SEP = String.valueOf((char) 0x1F);
    private static final String VALID_LINE = String.join(SEP,
        "abc123",
        "2024-03-15T10:30:00+00:00",
        "Ivan Ivanov",
        "ivan@example.com",
        "Add feature");

    private GitLogParser parser;

    @BeforeEach
    void setUp() {
        parser = new GitLogParser();
    }

    @Test
    void buildLogCommandIncludesPrettyAndDateRange() {
        List<String> cmd = parser.buildLogCommand(
            LocalDate.of(2024, 1, 1), LocalDate.of(2024, 2, 1));

        assertAll(
            () -> assertEquals("git", cmd.get(0)),
            () -> assertEquals("log", cmd.get(1)),
            () -> assertTrue(cmd.stream().anyMatch(s -> s.startsWith("--pretty=format:"))),
            () -> assertTrue(cmd.contains("--since=2024-01-01")),
            () -> assertTrue(cmd.contains("--until=2024-02-01"))
        );
    }

    @Test
    void prettyFormatContainsAllFields() {
        List<String> cmd = parser.buildLogCommand(LocalDate.now(), LocalDate.now());
        String pretty = cmd.stream().filter(s -> s.startsWith("--pretty=")).findFirst()
            .orElseThrow();
        assertTrue(pretty.contains("%H"));
        assertTrue(pretty.contains("%aI"));
        assertTrue(pretty.contains("%an"));
        assertTrue(pretty.contains("%ae"));
        assertTrue(pretty.contains("%s"));
    }

    @Test
    void parseManyOnEmptyStringReturnsEmptyList() {
        assertTrue(parser.parseMany("").isEmpty());
    }

    @Test
    void parseManyOnBlankStringReturnsEmptyList() {
        assertTrue(parser.parseMany("   \n\t").isEmpty());
    }

    @Test
    void parseManyParsesSingleLine() {
        List<Commit> commits = parser.parseMany(VALID_LINE);
        assertEquals(1, commits.size());
    }

    @Test
    void parsedCommitHasAllFields() {
        Commit c = parser.parseMany(VALID_LINE).get(0);
        assertAll(
            () -> assertEquals("abc123", c.hash()),
            () -> assertEquals("Ivan Ivanov", c.authorName()),
            () -> assertEquals("ivan@example.com", c.authorEmail()),
            () -> assertEquals("Add feature", c.subject())
        );
    }

    @Test
    void parsedCommitHasNonNullTimestamp() {
        Commit c = parser.parseMany(VALID_LINE).get(0);
        assertTrue(c.timestamp() != null);
    }

    @Test
    void parseManySplitsByNewline() {
        String line2 = String.join(SEP, "def456", "2024-03-16T12:00:00+00:00",
            "Petr", "petr@x", "Fix bug");
        List<Commit> commits = parser.parseMany(VALID_LINE + "\n" + line2);
        assertEquals(2, commits.size());
        assertEquals("abc123", commits.get(0).hash());
        assertEquals("def456", commits.get(1).hash());
    }

    @Test
    void parseManySkipsBlankLines() {
        String input = VALID_LINE + "\n\n   \n";
        assertEquals(1, parser.parseMany(input).size());
    }

    @Test
    void parseCommitOnMalformedLineThrows() {
        assertThrows(GitException.class,
            () -> parser.parseCommit("only" + SEP + "two"));
    }

    @Test
    void parseManyPropagatesGitException() {
        assertThrows(GitException.class,
            () -> parser.parseMany("only" + SEP + "two"));
    }

    @Test
    void emptySubjectIsAcceptedWhenSeparatorPresent() {
        String line = String.join(SEP, "h", "2024-03-15T10:30:00+00:00", "n", "e", "");
        Commit c = parser.parseCommit(line);
        assertEquals("", c.subject());
    }

    @Test
    void missingSubjectColumnDefaultsToEmpty() {
        String line = String.join(SEP, "h", "2024-03-15T10:30:00+00:00", "n", "e");
        Commit c = parser.parseCommit(line);
        assertEquals("", c.subject());
    }
}
