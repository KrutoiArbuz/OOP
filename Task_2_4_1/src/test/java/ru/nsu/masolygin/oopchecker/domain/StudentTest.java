package ru.nsu.masolygin.oopchecker.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class StudentTest {

    @Test
    void allFieldsAreAccessible() {
        Student s = new Student("ivanov", "Иванов И.И.", "https://github.com/ivanov/oop.git");
        assertAll(
            () -> assertEquals("ivanov", s.github()),
            () -> assertEquals("Иванов И.И.", s.fullName()),
            () -> assertEquals("https://github.com/ivanov/oop.git", s.repoUrl())
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  "})
    void blankGithubIsRejected(String bad) {
        assertThrows(IllegalArgumentException.class,
            () -> new Student(bad, "name", "url"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  "})
    void blankFullNameIsRejected(String bad) {
        assertThrows(IllegalArgumentException.class,
            () -> new Student("github", bad, "url"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  "})
    void blankRepoUrlIsRejected(String bad) {
        assertThrows(IllegalArgumentException.class,
            () -> new Student("github", "name", bad));
    }

    @Test
    void recordEqualityByValue() {
        Student a = new Student("u", "Name", "https://x");
        Student b = new Student("u", "Name", "https://x");
        assertEquals(a, b);
    }
}
