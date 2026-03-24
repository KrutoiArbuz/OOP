package ru.nsu.masolygin.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void testOrderCreation() {
        Order order = new Order();
        assertNotNull(order);
    }

    @Test
    void testSetInfo() {
        Order order = new Order();
        order.setInfo(1, OrderState.IN_QUEUE);
        assertEquals(1, order.getId());
        assertEquals(OrderState.IN_QUEUE, order.getState());
    }

    @Test
    void testSetInfoWithDifferentValues() {
        Order order = new Order();
        order.setInfo(42, OrderState.COOKING);
        assertEquals(42, order.getId());
        assertEquals(OrderState.COOKING, order.getState());
    }

    @Test
    void testSetState() {
        Order order = new Order();
        order.setInfo(1, OrderState.IN_QUEUE);
        order.setState(OrderState.COOKING);
        assertEquals(OrderState.COOKING, order.getState());
    }

    @Test
    void testSetStateMultipleTimes() {
        Order order = new Order();
        order.setInfo(1, OrderState.IN_QUEUE);
        order.setState(OrderState.COOKING);
        order.setState(OrderState.COOKED);
        order.setState(OrderState.DELIVERING);
        order.setState(OrderState.DELIVERED);
        assertEquals(OrderState.DELIVERED, order.getState());
    }

    @Test
    void testGetId() {
        Order order = new Order();
        order.setInfo(100, OrderState.IN_QUEUE);
        assertEquals(100, order.getId());
    }

    @Test
    void testGetState() {
        Order order = new Order();
        order.setInfo(1, OrderState.COOKED);
        assertEquals(OrderState.COOKED, order.getState());
    }

    @Test
    void testStateTransition() {
        Order order = new Order();
        order.setInfo(5, OrderState.IN_QUEUE);
        assertEquals(OrderState.IN_QUEUE, order.getState());

        order.setState(OrderState.COOKING);
        assertEquals(OrderState.COOKING, order.getState());

        order.setState(OrderState.COOKED);
        assertEquals(OrderState.COOKED, order.getState());

        order.setState(OrderState.DELIVERING);
        assertEquals(OrderState.DELIVERING, order.getState());

        order.setState(OrderState.DELIVERED);
        assertEquals(OrderState.DELIVERED, order.getState());
    }
}

