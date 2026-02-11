package ru.nsu.masolygin;

import java.util.List;
import ru.nsu.masolygin.actor.OrderGenerator;
import ru.nsu.masolygin.actor.employee.BakerProfile;
import ru.nsu.masolygin.actor.employee.CourierProfile;
import ru.nsu.masolygin.actor.employee.Worker;
import ru.nsu.masolygin.dto.PizzeriaContext;
import ru.nsu.masolygin.fileloader.ConfigLoader;
import ru.nsu.masolygin.fileloader.PizzeriaConfig;
import ru.nsu.masolygin.monitor.OrderQueue;
import ru.nsu.masolygin.monitor.Warehouse;
import ru.nsu.masolygin.view.ConsoleLogger;
import ru.nsu.masolygin.view.OrderLogger;

/**
 * Main класс.
 */
public class Main {

    /**
     * Main.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {

        ConfigLoader loader = new ConfigLoader();
        PizzeriaConfig config = loader.load("src/main/resources/config.json");

        OrderLogger orderLogger = new ConsoleLogger();

        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(config.getWarehouseCapacity());

        PizzeriaContext context = new PizzeriaContext(queue, warehouse, orderLogger);

        List<Worker<BakerProfile>> bakers = config.getBakers().stream()
        .map(cfg -> {
            BakerProfile profile = new BakerProfile(cfg.getId(), cfg.getCookingTime());
            return Worker.createBaker(profile, context);
        })
        .toList();

        // Курьеры
        List<Worker<CourierProfile>> couriers = config.getCouriers().stream()
        .map(cfg -> {
            CourierProfile profile = new CourierProfile(cfg.getId(), cfg.getDeliveryTime(),
            cfg.getBackpackCapacity());
            return Worker.createCourier(profile, context);
        })
        .toList();

        Pizzeria pizzeria = new Pizzeria(
        config.getWorkTime(),
        queue,
        orderLogger,
        bakers,
        couriers);

        OrderGenerator ordersGenerator = new OrderGenerator(pizzeria);

        Thread generatorThread = new Thread(ordersGenerator);
        generatorThread.start();

        pizzeria.workGracefulShutdown();

        generatorThread.interrupt();

    }
}
