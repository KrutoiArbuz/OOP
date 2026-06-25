package ru.nsu.masolygin.oopchecker.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FlagTest {

    @Test
    void presentInDetectsBooleanFlag() {
        assertTrue(Flag.TEXT.presentIn(new String[]{"--text", "test"}));
    }

    @Test
    void presentInReturnsFalseWhenAbsent() {
        assertFalse(Flag.TEXT.presentIn(new String[]{"test"}));
    }

    @Test
    void presentInDetectsValueFlagWithEquals() {
        assertTrue(Flag.STUDENT.presentIn(new String[]{"--student=alice"}));
    }

    @Test
    void valueOfExtractsValueAfterEquals() {
        assertEquals("alice", Flag.STUDENT.valueOf(new String[]{"--student=alice"}).orElseThrow());
    }

    @Test
    void valueOfReturnsEmptyWhenFlagAbsent() {
        assertFalse(Flag.STUDENT.valueOf(new String[]{"test"}).isPresent());
    }

    @Test
    void valueOfReturnsEmptyWhenValueEmpty() {
        assertFalse(Flag.STUDENT.valueOf(new String[]{"--student="}).isPresent());
    }

    @Test
    void valueOfFindsFlagAmongOtherArgs() {
        String[] args = {"test", "--text", "--dir=work", "--student=ivanov"};
        assertEquals("ivanov", Flag.STUDENT.valueOf(args).orElseThrow());
        assertEquals("work", Flag.DIR.valueOf(args).orElseThrow());
    }

    @Test
    void valueOfReturnsFirstMatchOnly() {
        String[] args = {"--student=alice", "--student=bob"};
        assertEquals("alice", Flag.STUDENT.valueOf(args).orElseThrow());
    }

    @Test
    void allFlagsAreEnumerated() {
        Flag[] all = Flag.values();
        assertEquals(5, all.length);
    }

    @Test
    void textIsBooleanFlagWithoutEquals() {
        assertTrue(Flag.TEXT.presentIn(new String[]{"--text"}));
        assertFalse(Flag.TEXT.valueOf(new String[]{"--text"}).isPresent());
    }
}
