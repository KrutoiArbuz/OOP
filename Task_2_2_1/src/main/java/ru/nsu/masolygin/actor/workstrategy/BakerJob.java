package ru.nsu.masolygin.actor.workstrategy;

import ru.nsu.masolygin.actor.employee.BakerProfile;
import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.dto.OrderState;
import ru.nsu.masolygin.dto.PizzeriaContext;

/**
 * Стратегия работы пекаря.
 */
public class BakerJob implements WorkStrategy<BakerProfile> {

    @Override
    public void work(BakerProfile profile, PizzeriaContext context) throws InterruptedException {
        Order order = context.orderQueue().takeOrder();

        order.setState(OrderState.COOKING);
        context.orderLogger().log(order, "Baker " + profile.id() + " started cooking order");

        Thread.sleep(profile.bakingSpeed());

        order.setState(OrderState.COOKED);
        context.orderLogger().log(order, "Baker " + profile.id() + " finished cooking order");

        context.warehouse().addOrder(order);
    }


}
