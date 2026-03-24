package ru.nsu.masolygin.actor.workstrategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.actor.employee.CourierProfile;
import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.dto.OrderState;
import ru.nsu.masolygin.dto.PizzeriaContext;
import ru.nsu.masolygin.monitor.OrderQueue;
import ru.nsu.masolygin.monitor.Warehouse;
import ru.nsu.masolygin.view.ConsoleLogger;

class CourierJobTest {

    @Test
    void testWork() throws InterruptedException {
        CourierProfile profile = new CourierProfile(1, 100, 2);
        Warehouse warehouse = new Warehouse(2);
        PizzeriaContext context = new PizzeriaContext(new OrderQueue(), warehouse,
            new ConsoleLogger());
        Order order1 = new Order();
        order1.setInfo(1, OrderState.COOKED);
        Order order2 = new Order();
        order2.setInfo(2, OrderState.COOKED);
        warehouse.addOrder(order1);
        warehouse.addOrder(order2);

        CourierJob courierJob = new CourierJob();
        courierJob.work(profile, context);

        assertEquals(OrderState.DELIVERED, order1.getState());
        assertEquals(OrderState.DELIVERED, order2.getState());
    }
}

