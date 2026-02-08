package ru.nsu.masolygin.monitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.dto.OrderState;

class OrderQueueTest {

    @Test
    void testOrderQueueCreation() {
        OrderQueue queue = new OrderQueue();
        assertNotNull(queue);
    }

    @Test
    void testAddAndTakeOrder() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Order order = new Order();
        order.setInfo(1, OrderState.IN_QUEUE);

        queue.addOrder(order);
        Order takenOrder = queue.takeOrder();

        assertEquals(1, takenOrder.getId());
    }

    @Test
    void testAddMultipleOrders() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Order order1 = new Order();
        order1.setInfo(1, OrderState.IN_QUEUE);
        Order order2 = new Order();
        order2.setInfo(2, OrderState.IN_QUEUE);
        Order order3 = new Order();
        order3.setInfo(3, OrderState.IN_QUEUE);

        queue.addOrder(order1);
        queue.addOrder(order2);
        queue.addOrder(order3);

        assertEquals(1, queue.takeOrder().getId());
        assertEquals(2, queue.takeOrder().getId());
        assertEquals(3, queue.takeOrder().getId());
    }

    @Test
    void testFifoOrder() throws InterruptedException {
        OrderQueue queue = new OrderQueue();

        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setInfo(i, OrderState.IN_QUEUE);
            queue.addOrder(order);
        }

        for (int i = 0; i < 10; i++) {
            assertEquals(i, queue.takeOrder().getId());
        }
    }

    @Test
    void testTakeOrderBlocksWhenEmpty() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        long startTime = System.currentTimeMillis();

        Thread consumer = new Thread(() -> {
            try {
                Thread.sleep(100);
                Order order = new Order();
                order.setInfo(1, OrderState.IN_QUEUE);
                queue.addOrder(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        consumer.start();
        Order order = queue.takeOrder();
        long endTime = System.currentTimeMillis();

        assertTrue(endTime - startTime >= 100);
        assertEquals(1, order.getId());
        consumer.join();
    }

    @Test
    void testConcurrentAddAndTake() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        final int orderCount = 100;

        Thread producer = new Thread(() -> {
            for (int i = 0; i < orderCount; i++) {
                Order order = new Order();
                order.setInfo(i, OrderState.IN_QUEUE);
                queue.addOrder(order);
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < orderCount; i++) {
                    queue.takeOrder();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        assertTrue(true);
    }
}

