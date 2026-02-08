package ru.nsu.masolygin;

import ru.nsu.masolygin.actor.Baker;
import ru.nsu.masolygin.actor.Courier;
import ru.nsu.masolygin.actor.OrderGenerator;
import ru.nsu.masolygin.fileLoader.ConfigLoader;
import ru.nsu.masolygin.fileLoader.PizzeriaConfig;
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

        OrderLogger orderLogger = new OrderLogger();

        Baker[] bakers = config.getBakers().stream()
        .map(bakerConfig -> new Baker(bakerConfig.getId(), bakerConfig.getCookingTime()))
        .toArray(Baker[]::new);

        Courier[] couriers = config.getCouriers().stream()
        .map(courierConfig -> new Courier(courierConfig.getId(), courierConfig.getDeliveryTime(),
        courierConfig.getBackpackCapacity()))
        .toArray(Courier[]::new);

        Pizzeria pizzeria = new Pizzeria( config.getWorkTime(), config.getWarehouseCapacity(), orderLogger, bakers, couriers);

        OrderGenerator ordersGenerator = new OrderGenerator(pizzeria);

        Thread generatorThread = new Thread(ordersGenerator);
        generatorThread.start();

        pizzeria.workGracefulShutdown();

        generatorThread.interrupt();

    }
}
