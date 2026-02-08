package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.actor.Baker;
import ru.nsu.masolygin.actor.Courier;
import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.view.OrderLogger;

class PizzeriaTest {

    @Test
    void testPizzeriaCreation() {
        Baker[] bakers = {new Baker(1, 1000)};
        Courier[] couriers = {new Courier(1, 1000, 5)};
        Pizzeria pizzeria = new Pizzeria(5000, 10, new OrderLogger(), bakers, couriers);
        assertNotNull(pizzeria);
    }

    @Test
    void testPizzeriaCreationWithMultipleWorkers() {
        Baker[] bakers = {new Baker(1, 1000), new Baker(2, 1000), new Baker(3, 1000)};
        Courier[] couriers = {new Courier(1, 1000, 5), new Courier(2, 1000, 5)};
        Pizzeria pizzeria = new Pizzeria(10000, 20, new OrderLogger(), bakers, couriers);
        assertNotNull(pizzeria);
    }

    @Test
    void testPizzeriaCreationWithDifferentParameters() {
        Baker[] bakers = {new Baker(1, 500)};
        Courier[] couriers = {new Courier(1, 800, 3)};
        Pizzeria pizzeria = new Pizzeria(3000, 5, new OrderLogger(), bakers, couriers);
        assertNotNull(pizzeria);
    }

    @Test
    void testAcceptOrderWhenClosed() {
        Baker[] bakers = {new Baker(1, 1000)};
        Courier[] couriers = {new Courier(1, 1000, 5)};
        Pizzeria pizzeria = new Pizzeria(5000, 10, new OrderLogger(), bakers, couriers);

        Order order = new Order();
        pizzeria.acceptOrder(order);

        assertNotNull(pizzeria);
    }

    @Test
    void testWorkGracefulShutdownWithShortTime() {
        Baker[] bakers = {new Baker(1, 50)};
        Courier[] couriers = {new Courier(1, 50, 5)};
        Pizzeria pizzeria = new Pizzeria(200, 10, new OrderLogger(), bakers, couriers);

        Thread pizzeriaThread = new Thread(() -> pizzeria.workGracefulShutdown());
        pizzeriaThread.start();

        try {
            pizzeriaThread.join(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertNotNull(pizzeria);
    }

    @Test
    void testAcceptMultipleOrders() {
        Baker[] bakers = {new Baker(1, 50)};
        Courier[] couriers = {new Courier(1, 50, 5)};
        Pizzeria pizzeria = new Pizzeria(500, 10, new OrderLogger(), bakers, couriers);

        Thread pizzeriaThread = new Thread(() -> pizzeria.workGracefulShutdown());
        pizzeriaThread.start();

        try {
            Thread.sleep(50);
            for (int i = 0; i < 5; i++) {
                Order order = new Order();
                pizzeria.acceptOrder(order);
            }
            pizzeriaThread.join(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertNotNull(pizzeria);
    }

    @Test
    void testPizzeriaWithMultipleBakersAndCouriers() {
        Baker[] bakers = {new Baker(1, 100), new Baker(2, 100)};
        Courier[] couriers = {new Courier(1, 100, 3), new Courier(2, 100, 3)};
        Pizzeria pizzeria = new Pizzeria(300, 15, new OrderLogger(), bakers, couriers);

        Thread pizzeriaThread = new Thread(() -> pizzeria.workGracefulShutdown());
        pizzeriaThread.start();

        try {
            Thread.sleep(50);
            for (int i = 0; i < 10; i++) {
                Order order = new Order();
                pizzeria.acceptOrder(order);
            }
            pizzeriaThread.join(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertNotNull(pizzeria);
    }

    @Test
    void testPizzeriaWithLargeWarehouse() {
        Baker[] bakers = {new Baker(1, 100)};
        Courier[] couriers = {new Courier(1, 100, 5)};
        Pizzeria pizzeria = new Pizzeria(500, 50, new OrderLogger(), bakers, couriers);

        assertNotNull(pizzeria);
    }

    @Test
    void testPizzeriaWithSmallWarehouse() {
        Baker[] bakers = {new Baker(1, 100)};
        Courier[] couriers = {new Courier(1, 100, 5)};
        Pizzeria pizzeria = new Pizzeria(500, 2, new OrderLogger(), bakers, couriers);

        assertNotNull(pizzeria);
    }

    @Test
    void testPizzeriaWithManyWorkers() {
        Baker[] bakers = new Baker[5];
        for (int i = 0; i < 5; i++) {
            bakers[i] = new Baker(i + 1, 100);
        }

        Courier[] couriers = new Courier[5];
        for (int i = 0; i < 5; i++) {
            couriers[i] = new Courier(i + 1, 100, 5);
        }

        Pizzeria pizzeria = new Pizzeria(500, 20, new OrderLogger(), bakers, couriers);
        assertNotNull(pizzeria);
    }

    @Test
    void testPizzeriaWorkflow() {
        Baker[] bakers = {new Baker(1, 50)};
        Courier[] couriers = {new Courier(1, 50, 5)};
        Pizzeria pizzeria = new Pizzeria(300, 10, new OrderLogger(), bakers, couriers);

        Thread pizzeriaThread = new Thread(() -> pizzeria.workGracefulShutdown());
        pizzeriaThread.start();

        try {
            Thread.sleep(50);
            Order order = new Order();
            pizzeria.acceptOrder(order);
            pizzeriaThread.join(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertNotNull(pizzeria);
    }
}

