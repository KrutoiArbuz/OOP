package ru.nsu.masolygin;

import java.util.List;
import ru.nsu.masolygin.actor.employee.BakerProfile;
import ru.nsu.masolygin.actor.employee.CourierProfile;
import ru.nsu.masolygin.actor.employee.Worker;
import ru.nsu.masolygin.dto.PizzeriaContext;
import ru.nsu.masolygin.fileloader.PizzeriaConfig;
import ru.nsu.masolygin.monitor.OrderQueue;
import ru.nsu.masolygin.monitor.Warehouse;
import ru.nsu.masolygin.view.ConsoleLogger;
import ru.nsu.masolygin.view.OrderLogger;

public class PizzeriaBuilder {

    private final PizzeriaConfig config;

    public PizzeriaBuilder(PizzeriaConfig config) {
        this.config = config;
    }

    public Pizzeria build() {
        OrderLogger logger = new ConsoleLogger();
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(config.getWarehouseCapacity());

        PizzeriaContext context = new PizzeriaContext(queue, warehouse, logger);

        List<Worker<BakerProfile>> bakers = config.getBakers().stream()
        .map(cfg -> {
            BakerProfile profile = new BakerProfile(cfg.getId(), cfg.getCookingTime());
            return Worker.createBaker(profile, context);
        })
        .toList();


        List<Worker<CourierProfile>> couriers = config.getCouriers().stream()
        .map(cfg -> {
            CourierProfile profile = new CourierProfile(cfg.getId(), cfg.getDeliveryTime(),
            cfg.getBackpackCapacity());
            return Worker.createCourier(profile, context);
        })
        .toList();

        return new Pizzeria(config.getWorkTime(), queue,logger,bakers,couriers);
    }
}
