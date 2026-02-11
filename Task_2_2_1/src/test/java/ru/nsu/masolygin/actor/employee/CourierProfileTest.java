package ru.nsu.masolygin.actor.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CourierProfileTest {

    @Test
    void testCourierProfile() {
        CourierProfile profile = new CourierProfile(1, 1000, 2);
        assertEquals(1, profile.id());
        assertEquals(1000, profile.deliverySpeed());
        assertEquals(2, profile.backpackCapacity());
    }
}

