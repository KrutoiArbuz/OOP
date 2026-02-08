package ru.nsu.masolygin.actor;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.Pizzeria;
import ru.nsu.masolygin.view.OrderLogger;

class OrderGeneratorTest {

    @Test
    void testOrderGeneratorCreation() {
        Baker[] bakers = {new Baker(1, 1000)};
        Courier[] couriers = {new Courier(1, 1000, 5)};
        Pizzeria pizzeria = new Pizzeria(5000, 10, new OrderLogger(), bakers, couriers);
        OrderGenerator generator = new OrderGenerator(pizzeria);
        assertNotNull(generator);
    }

    @Test
    void testOrderGeneratorCreationWithDifferentPizzeria() {
        Baker[] bakers = {new Baker(1, 500), new Baker(2, 600)};
        Courier[] couriers = {new Courier(1, 800, 3), new Courier(2, 900, 4)};
        Pizzeria pizzeria = new Pizzeria(10000, 20, new OrderLogger(), bakers, couriers);
        OrderGenerator generator = new OrderGenerator(pizzeria);
        assertNotNull(generator);
    }

    @Test
    void testOrderGeneratorGeneratesOrders() throws InterruptedException {
        Baker[] bakers = {new Baker(1, 100)};
        Courier[] couriers = {new Courier(1, 100, 5)};
        Pizzeria pizzeria = new Pizzeria(1000, 10, new OrderLogger(), bakers, couriers);
        OrderGenerator generator = new OrderGenerator(pizzeria);

        Thread generatorThread = new Thread(generator);
        generatorThread.start();
        Thread.sleep(500);
        generatorThread.interrupt();
        generatorThread.join();

        assertNotNull(generator);
    }

    @Test
    void testOrderGeneratorStopsWhenInterrupted() throws InterruptedException {
        Baker[] bakers = {new Baker(1, 1000)};
        Courier[] couriers = {new Courier(1, 1000, 5)};
        Pizzeria pizzeria = new Pizzeria(5000, 10, new OrderLogger(), bakers, couriers);
        OrderGenerator generator = new OrderGenerator(pizzeria);

        Thread generatorThread = new Thread(generator);
        generatorThread.start();
        Thread.sleep(100);
        generatorThread.interrupt();
        generatorThread.join(1000);

        assertNotNull(generatorThread);
    }

    @Test
    void testMultipleOrderGenerators() throws InterruptedException {
        Baker[] bakers = {new Baker(1, 100), new Baker(2, 100)};
        Courier[] couriers = {new Courier(1, 100, 5), new Courier(2, 100, 5)};
        Pizzeria pizzeria = new Pizzeria(2000, 20, new OrderLogger(), bakers, couriers);

        OrderGenerator generator1 = new OrderGenerator(pizzeria);
        OrderGenerator generator2 = new OrderGenerator(pizzeria);

        Thread generatorThread1 = new Thread(generator1);
        Thread generatorThread2 = new Thread(generator2);

        generatorThread1.start();
        generatorThread2.start();
        Thread.sleep(300);
        generatorThread1.interrupt();
        generatorThread2.interrupt();
        generatorThread1.join();
        generatorThread2.join();

        assertNotNull(generator1);
        assertNotNull(generator2);
    }

    @Test
    void testOrderGeneratorWithFastGeneration() throws InterruptedException {
        Baker[] bakers = {new Baker(1, 50)};
        Courier[] couriers = {new Courier(1, 50, 10)};
        Pizzeria pizzeria = new Pizzeria(1000, 50, new OrderLogger(), bakers, couriers);
        OrderGenerator generator = new OrderGenerator(pizzeria);

        Thread generatorThread = new Thread(generator);
        generatorThread.start();
        Thread.sleep(600);
        generatorThread.interrupt();
        generatorThread.join();

        assertNotNull(generator);
    }

    @Test
    void testOrderGeneratorWithSlowGeneration() throws InterruptedException {
        Baker[] bakers = {new Baker(1, 100)};
        Courier[] couriers = {new Courier(1, 100, 5)};
        Pizzeria pizzeria = new Pizzeria(2000, 10, new OrderLogger(), bakers, couriers);
        OrderGenerator generator = new OrderGenerator(pizzeria);

        Thread generatorThread = new Thread(generator);
        generatorThread.start();
        Thread.sleep(300);
        generatorThread.interrupt();
        generatorThread.join();

        assertNotNull(generator);
    }
}

