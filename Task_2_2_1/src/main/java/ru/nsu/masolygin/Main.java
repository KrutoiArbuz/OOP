package ru.nsu.masolygin;

import ru.nsu.masolygin.actor.Baker;
import ru.nsu.masolygin.actor.Courier;
import ru.nsu.masolygin.actor.OrderGenerator;
import ru.nsu.masolygin.view.OrderLogger;

public class Main {

    public static void main(String[] args) {

        OrderLogger orderLogger = new OrderLogger();

        Baker[] bakers = {new Baker(1, 400), new Baker(2, 550)};
        Courier[] couriers = {new Courier(1, 200, 2), new Courier(2, 250, 2)};

        Pizzeria pizzeria = new Pizzeria(10000, 10, orderLogger, bakers, couriers);

        OrderGenerator ordersGenerator = new OrderGenerator(pizzeria);

        Thread generatorThread = new Thread(ordersGenerator);
        generatorThread.start();

        pizzeria.work();

        generatorThread.interrupt();

    }
}
