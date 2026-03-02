package ru.nsu.masolygin.solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SequentialPrimeCheckerTest {

    private SequentialPrimeChecker checker;

    @BeforeEach
    void setUp() {
        checker = new SequentialPrimeChecker();
    }

    @Test
    void testEquals() {
        SequentialPrimeChecker checker1 = new SequentialPrimeChecker();
        SequentialPrimeChecker checker2 = new SequentialPrimeChecker();
        assertEquals(checker1.getClass(), checker2.getClass());
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(checker, checker);
    }

    @Test
    void testEqualsNull() {
        assertNotEquals(checker, null);
    }

    @Test
    void testEqualsDifferentClass() {
        assertNotEquals(checker, "checker");
    }

    @Test
    void testToString() {
        String result = checker.toString();
        assertTrue(result.contains("SequentialPrimeChecker"));
    }

    @Test
    void testHashCode() {
        SequentialPrimeChecker checker1 = new SequentialPrimeChecker();
        SequentialPrimeChecker checker2 = new SequentialPrimeChecker();
        assertEquals(checker1.hashCode(), checker1.hashCode());
    }

    @Test
    void testInstanceOf() {
        assertTrue(checker instanceof PrimeChecker);
        assertTrue(checker instanceof SequentialPrimeChecker);
    }
}
