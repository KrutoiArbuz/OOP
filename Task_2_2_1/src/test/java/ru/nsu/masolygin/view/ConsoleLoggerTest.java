package ru.nsu.masolygin.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.dto.OrderState;

class ConsoleLoggerTest {

    @Test
    void testLog() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ConsoleLogger logger = new ConsoleLogger();
        Order order = new Order();
        order.setInfo(1, OrderState.IN_QUEUE);
        logger.log(order, "Test message");

        String expectedOutput = "[1] [In Queue] - Test message" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }
}

