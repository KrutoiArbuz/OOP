package ru.nsu.masolygin.actor.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BakerProfileTest {

    @Test
    void testBakerProfile() {
        BakerProfile profile = new BakerProfile(1, 1000);
        assertEquals(1, profile.id());
        assertEquals(1000, profile.bakingSpeed());
    }
}

