package ru.nsu.masolygin.worker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class ProcessResultTest {

    @Test
    void hasThreeValues() {
        assertEquals(3, ProcessResult.values().length);
    }

    @Test
    void compositeFoundExists() {
        assertNotNull(ProcessResult.valueOf("COMPOSITE_FOUND"));
    }

    @Test
    void allPrimeExists() {
        assertNotNull(ProcessResult.valueOf("ALL_PRIME"));
    }

    @Test
    void cancelledExists() {
        assertNotNull(ProcessResult.valueOf("CANCELLED"));
    }
}
