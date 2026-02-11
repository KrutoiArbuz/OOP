package ru.nsu.masolygin.actor.workstrategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.actor.employee.BakerProfile;
import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.dto.OrderState;
import ru.nsu.masolygin.dto.PizzeriaContext;
import ru.nsu.masolygin.monitor.OrderQueue;
import ru.nsu.masolygin.monitor.Warehouse;
import ru.nsu.masolygin.view.ConsoleLogger;

class BakerJobTest {

    @Test
    void testWork() throws InterruptedException {
        BakerProfile profile = new BakerProfile(1, 100);
        OrderQueue orderQueue = new OrderQueue();
        Warehouse warehouse = new Warehouse(1);
        PizzeriaContext context = new PizzeriaContext(orderQueue, warehouse, new ConsoleLogger());
        Order order = new Order();
        order.setInfo(1, OrderState.IN_QUEUE);
        orderQueue.addOrder(order);

        BakerJob bakerJob = new BakerJob();
        bakerJob.work(profile, context);

        assertEquals(OrderState.COOKED, order.getState());
    }
}

