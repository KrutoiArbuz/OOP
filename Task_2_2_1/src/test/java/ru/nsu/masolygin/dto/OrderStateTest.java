package ru.nsu.masolygin.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class OrderStateTest {

    @Test
    void testInQueueDisplayName() {
        assertEquals("In Queue", OrderState.IN_QUEUE.getDisplayName());
    }

    @Test
    void testCookingDisplayName() {
        assertEquals("Cooking", OrderState.COOKING.getDisplayName());
    }

    @Test
    void testCookedDisplayName() {
        assertEquals("Cooked", OrderState.COOKED.getDisplayName());
    }

    @Test
    void testDeliveringDisplayName() {
        assertEquals("Delivering", OrderState.DELIVERING.getDisplayName());
    }

    @Test
    void testDeliveredDisplayName() {
        assertEquals("Delivered", OrderState.DELIVERED.getDisplayName());
    }

    @Test
    void testAllStatesExist() {
        assertNotNull(OrderState.IN_QUEUE);
        assertNotNull(OrderState.COOKING);
        assertNotNull(OrderState.COOKED);
        assertNotNull(OrderState.DELIVERING);
        assertNotNull(OrderState.DELIVERED);
    }

    @Test
    void testEnumValues() {
        OrderState[] states = OrderState.values();
        assertEquals(5, states.length);
    }

    @Test
    void testEnumValueOf() {
        assertEquals(OrderState.IN_QUEUE, OrderState.valueOf("IN_QUEUE"));
        assertEquals(OrderState.COOKING, OrderState.valueOf("COOKING"));
        assertEquals(OrderState.COOKED, OrderState.valueOf("COOKED"));
        assertEquals(OrderState.DELIVERING, OrderState.valueOf("DELIVERING"));
        assertEquals(OrderState.DELIVERED, OrderState.valueOf("DELIVERED"));
    }
}

