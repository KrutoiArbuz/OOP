package ru.nsu.masolygin.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.monitor.OrderQueue;
import ru.nsu.masolygin.monitor.Warehouse;
import ru.nsu.masolygin.view.ConsoleLogger;
import ru.nsu.masolygin.view.OrderLogger;

class PizzeriaContextTest {

    @Test
    void testPizzeriaContext() {
        OrderQueue orderQueue = new OrderQueue();
        Warehouse warehouse = new Warehouse(1);
        OrderLogger logger = new ConsoleLogger();
        PizzeriaContext context = new PizzeriaContext(orderQueue, warehouse, logger);
        assertEquals(orderQueue, context.orderQueue());
        assertEquals(warehouse, context.warehouse());
        assertEquals(logger, context.orderLogger());
    }
}

