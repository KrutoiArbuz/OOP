package ru.nsu.masolygin.actor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.dto.OrderState;
import ru.nsu.masolygin.monitor.OrderQueue;
import ru.nsu.masolygin.monitor.Warehouse;
import ru.nsu.masolygin.view.OrderLogger;

class BakerTest {

    @Test
    void testBakerCreation() {
        Baker baker = new Baker(1, 1000);
        assertNotNull(baker);
    }

    @Test
    void testBakerCreationWithDifferentParameters() {
        Baker baker1 = new Baker(1, 500);
        Baker baker2 = new Baker(2, 1000);
        Baker baker3 = new Baker(3, 1500);
        assertNotNull(baker1);
        assertNotNull(baker2);
        assertNotNull(baker3);
    }

    @Test
    void testEmploy() {
        Baker baker = new Baker(1, 1000);
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(5);
        OrderLogger logger = new OrderLogger();

        baker.employ(queue, warehouse, logger);
        assertNotNull(baker);
    }

    @Test
    void testRunWithoutEmployThrowsException() {
        Baker baker = new Baker(1, 1000);
        assertThrows(IllegalStateException.class, baker::run);
    }

    @Test
    void testBakerProcessesOrder() throws InterruptedException {
        Baker baker = new Baker(1, 100);
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(5);
        OrderLogger logger = new OrderLogger();

        baker.employ(queue, warehouse, logger);

        Order order = new Order();
        order.setInfo(1, OrderState.IN_QUEUE);
        queue.addOrder(order);

        Thread bakerThread = new Thread(() -> {
            try {
                baker.run();
            } catch (IllegalStateException e) {
            }
        });

        bakerThread.start();
        Thread.sleep(200);
        bakerThread.interrupt();
        bakerThread.join();

        assertEquals(OrderState.COOKED, order.getState());
    }

    @Test
    void testBakerProcessesMultipleOrders() throws InterruptedException {
        Baker baker = new Baker(1, 50);
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(10);
        OrderLogger logger = new OrderLogger();

        baker.employ(queue, warehouse, logger);

        Order order1 = new Order();
        order1.setInfo(1, OrderState.IN_QUEUE);
        Order order2 = new Order();
        order2.setInfo(2, OrderState.IN_QUEUE);
        Order order3 = new Order();
        order3.setInfo(3, OrderState.IN_QUEUE);

        queue.addOrder(order1);
        queue.addOrder(order2);
        queue.addOrder(order3);

        Thread bakerThread = new Thread(() -> {
            try {
                baker.run();
            } catch (IllegalStateException e) {
            }
        });

        bakerThread.start();
        Thread.sleep(200);
        bakerThread.interrupt();
        bakerThread.join();

        assertEquals(OrderState.COOKED, order1.getState());
        assertEquals(OrderState.COOKED, order2.getState());
    }

    @Test
    void testBakerStopsWhenInterrupted() throws InterruptedException {
        Baker baker = new Baker(1, 100);
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(5);
        OrderLogger logger = new OrderLogger();

        baker.employ(queue, warehouse, logger);

        Thread bakerThread = new Thread(() -> {
            try {
                baker.run();
            } catch (IllegalStateException e) {
            }
        });

        bakerThread.start();
        Thread.sleep(50);
        bakerThread.interrupt();
        bakerThread.join(1000);

        assertEquals(false, bakerThread.isAlive());
    }

    @Test
    void testMultipleBakersProcessOrders() throws InterruptedException {
        Baker baker1 = new Baker(1, 50);
        Baker baker2 = new Baker(2, 50);
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(10);
        OrderLogger logger = new OrderLogger();

        baker1.employ(queue, warehouse, logger);
        baker2.employ(queue, warehouse, logger);

        for (int i = 0; i < 5; i++) {
            Order order = new Order();
            order.setInfo(i, OrderState.IN_QUEUE);
            queue.addOrder(order);
        }

        Thread bakerThread1 = new Thread(() -> {
            try {
                baker1.run();
            } catch (IllegalStateException e) {
            }
        });

        Thread bakerThread2 = new Thread(() -> {
            try {
                baker2.run();
            } catch (IllegalStateException e) {
            }
        });

        bakerThread1.start();
        bakerThread2.start();
        Thread.sleep(200);
        bakerThread1.interrupt();
        bakerThread2.interrupt();
        bakerThread1.join();
        bakerThread2.join();

        assertNotNull(baker1);
        assertNotNull(baker2);
    }
}

