package ru.nsu.masolygin.actor.workstrategy;

import java.util.List;
import ru.nsu.masolygin.actor.employee.CourierProfile;
import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.dto.OrderState;
import ru.nsu.masolygin.dto.PizzeriaContext;

/**
 * Стратегия работы курьера.
 */
public class CourierJob implements WorkStrategy<CourierProfile> {

    @Override
    public void work(CourierProfile profile, PizzeriaContext context) throws InterruptedException {
        List<Order> orders = context.warehouse().takeOrders(profile.backpackCapacity());

        for (Order order : orders) {
            order.setState(OrderState.DELIVERING);
            context.orderLogger()
                .log(order, "Courier " + profile.id() + " started delivering order");
        }

        for (Order order : orders) {
            Thread.sleep(profile.deliverySpeed());
            order.setState(OrderState.DELIVERED);
            context.orderLogger().log(order, "Courier " + profile.id() + " delivered order");
        }
    }
}
