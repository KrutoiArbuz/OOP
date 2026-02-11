package ru.nsu.masolygin;

import ru.nsu.masolygin.actor.OrderGenerator;
import ru.nsu.masolygin.fileloader.ConfigLoader;
import ru.nsu.masolygin.fileloader.PizzeriaConfig;

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

        Pizzeria pizzeria = new PizzeriaBuilder(config).build();

        OrderGenerator ordersGenerator = new OrderGenerator(pizzeria);
        Thread generatorThread = new Thread(ordersGenerator);
        generatorThread.start();

        pizzeria.workGracefulShutdown();

        generatorThread.interrupt();

    }
}
