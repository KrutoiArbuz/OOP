package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.solutions.ParallelPrimeChecker;
import ru.nsu.masolygin.solutions.SequentialPrimeChecker;

class MainTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testGenerateArraySize() {
        int size = 100;
        int[] array = Main.generateArray(size);
        assertEquals(size, array.length);
    }

    @Test
    void testGenerateArrayContent() {
        int[] array = Main.generateArray(10);
        int heavyPrime = Integer.MAX_VALUE;
        int heavyPrime2 = 2147483629;

        for (int i = 0; i < array.length; i++) {
            if (i % 2 == 0) {
                assertEquals(heavyPrime, array[i]);
            } else {
                assertEquals(heavyPrime2, array[i]);
            }
        }
    }

    @Test
    void testGenerateArrayEmpty() {
        int[] array = Main.generateArray(0);
        assertEquals(0, array.length);
    }

    @Test
    void testGenerateArrayLargeSize() {
        int size = 10000;
        int[] array = Main.generateArray(size);
        assertEquals(size, array.length);
    }

    @Test
    void testGenerateFunnyArraySize() {
        int size = 100;
        int[] array = Main.generateFunnyArray(size);
        assertEquals(size, array.length);
    }

    @Test
    void testGenerateFunnyArrayContent() {
        int size = 10;
        int[] array = Main.generateFunnyArray(size);

        for (int i = 0; i < size - 1; i++) {
            assertEquals(Integer.MAX_VALUE, array[i]);
        }
        assertEquals(22, array[size - 1]);
    }

    @Test
    void testGenerateFunnyArrayLastElement() {
        int size = 1;
        int[] array = Main.generateFunnyArray(size);
        assertEquals(22, array[0]);
    }

    @Test
    void testGenerateFunnyArrayLargeSize() {
        int size = 10000;
        int[] array = Main.generateFunnyArray(size);
        assertEquals(size, array.length);
        assertEquals(22, array[size - 1]);
    }

    @Test
    void testTesterOutput() {
        SequentialPrimeChecker checker = new SequentialPrimeChecker();
        int[] data = {2, 3, 5, 7};
        String name = "Test Checker";

        Main.tester(checker, data, name);

        String output = outContent.toString();
        assertTrue(output.contains(name));
        assertTrue(output.contains("Time:"));
        assertTrue(output.contains("ms"));
    }

    @Test
    void testTesterWithNonPrimes() {
        SequentialPrimeChecker checker = new SequentialPrimeChecker();
        int[] data = {4, 6, 8, 10};
        String name = "Non-Prime Checker";

        Main.tester(checker, data, name);

        String output = outContent.toString();
        assertTrue(output.contains("true"));
    }

    @Test
    void testTesterWithPrimes() {
        SequentialPrimeChecker checker = new SequentialPrimeChecker();
        int[] data = {2, 3, 5, 7, 11};
        String name = "Prime Checker";

        Main.tester(checker, data, name);

        String output = outContent.toString();
        assertTrue(output.contains("false"));
    }

    @Test
    void testTesterWithEmptyArray() {
        SequentialPrimeChecker checker = new SequentialPrimeChecker();
        int[] data = {};
        String name = "Empty Array Checker";

        Main.tester(checker, data, name);

        String output = outContent.toString();
        assertTrue(output.contains(name));
    }

    @Test
    void testTesterWithParallelChecker() {
        ParallelPrimeChecker checker = new ParallelPrimeChecker();
        int[] data = {2, 4, 6, 8};
        String name = "Parallel Checker";

        Main.tester(checker, data, name);

        String output = outContent.toString();
        assertTrue(output.contains(name));
        assertTrue(output.contains("true"));
    }

    @Test
    void testMainMethodExecution() {
        Main.main(new String[]{});

        String output = outContent.toString();
        assertFalse(output.isEmpty());
        assertTrue(output.contains("Sequential Checker"));
        assertTrue(output.contains("Threaded Checker"));
        assertTrue(output.contains("Parallel Checker"));
    }

    @Test
    void testMainMethodOutputStructure() {
        Main.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Big Data"));
        assertTrue(output.contains("Funny Data"));
    }

    @Test
    void testMainMethodAllCheckers() {
        Main.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Sequential Checker (Big Data)"));
        assertTrue(output.contains("Threaded Checker (1 Thread) (Big Data)"));
        assertTrue(output.contains("Threaded Checker (3 Threads) (Big Data)"));
        assertTrue(output.contains("Threaded Checker (5 Threads) (Big Data)"));
        assertTrue(output.contains("Threaded Checker (8 Threads) (Big Data)"));
        assertTrue(output.contains("Parallel Checker (Big Data)"));
    }

    @Test
    void testGenerateArrayNotNull() {
        int[] array = Main.generateArray(10);
        assertNotNull(array);
    }

    @Test
    void testGenerateFunnyArrayNotNull() {
        int[] array = Main.generateFunnyArray(10);
        assertNotNull(array);
    }

}
