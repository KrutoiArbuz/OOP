package ru.nsu.masolygin.view;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.dto.OrderState;

class OrderLoggerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testOrderLoggerCreation() {
        OrderLogger logger = new OrderLogger();
        assertNotNull(logger);
    }

    @Test
    void testLogOrderWithDifferentStates() {
        OrderLogger logger = new OrderLogger();
        Order order = new Order();
        order.setInfo(5, OrderState.COOKING);

        logger.log(order, "Baker started cooking");

        String output = outContent.toString();
        assert (output.contains("[5]"));
        assert (output.contains("[Cooking]"));
        assert (output.contains("Baker started cooking"));
    }

    @Test
    void testLogMultipleOrders() {
        OrderLogger logger = new OrderLogger();
        Order order1 = new Order();
        order1.setInfo(1, OrderState.IN_QUEUE);
        Order order2 = new Order();
        order2.setInfo(2, OrderState.COOKING);

        logger.log(order1, "First message");
        logger.log(order2, "Second message");

        String output = outContent.toString();
        assertTrue(output.contains("[1]"));
        assertTrue(output.contains("First message"));
        assertTrue(output.contains("[2]"));
        assertTrue(output.contains("Second message"));
    }

    @Test
    void testLogWithDeliveredState() {
        OrderLogger logger = new OrderLogger();
        Order order = new Order();
        order.setInfo(10, OrderState.DELIVERED);

        logger.log(order, "Order delivered");

        String output = outContent.toString();
        assertTrue(output.contains("[10]"));
        assertTrue(output.contains("[Delivered]"));
        assertTrue(output.contains("Order delivered"));
    }

    @Test
    void testLogWithCookedState() {
        OrderLogger logger = new OrderLogger();
        Order order = new Order();
        order.setInfo(7, OrderState.COOKED);

        logger.log(order, "Baker finished cooking");

        String output = outContent.toString();
        assert (output.contains("[7]"));
        assert (output.contains("[Cooked]"));
        assert (output.contains("Baker finished cooking"));
    }

    @Test
    void testLogWithDeliveringState() {
        OrderLogger logger = new OrderLogger();
        Order order = new Order();
        order.setInfo(15, OrderState.DELIVERING);

        logger.log(order, "Courier delivering");

        String output = outContent.toString();
        assertTrue(output.contains("[15]"));
        assertTrue(output.contains("[Delivering]"));
        assertTrue(output.contains("Courier delivering"));
    }
}

