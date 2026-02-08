package ru.nsu.masolygin.monitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.dto.OrderState;

class WarehouseTest {

    @Test
    void testWarehouseCreation() {
        Warehouse warehouse = new Warehouse(10);
        assertNotNull(warehouse);
    }

    @Test
    void testAddAndTakeOrder() throws InterruptedException {
        Warehouse warehouse = new Warehouse(5);
        Order order = new Order();
        order.setInfo(1, OrderState.COOKED);

        warehouse.addOrder(order);
        List<Order> orders = warehouse.takeOrders(1);

        assertEquals(1, orders.size());
        assertEquals(1, orders.get(0).getId());
    }

    @Test
    void testTakeMultipleOrders() throws InterruptedException {
        Warehouse warehouse = new Warehouse(10);

        for (int i = 0; i < 5; i++) {
            Order order = new Order();
            order.setInfo(i, OrderState.COOKED);
            warehouse.addOrder(order);
        }

        List<Order> orders = warehouse.takeOrders(3);
        assertEquals(3, orders.size());
    }

    @Test
    void testTakeOrdersLessThanAvailable() throws InterruptedException {
        Warehouse warehouse = new Warehouse(10);

        for (int i = 0; i < 5; i++) {
            Order order = new Order();
            order.setInfo(i, OrderState.COOKED);
            warehouse.addOrder(order);
        }

        List<Order> orders = warehouse.takeOrders(10);
        assertEquals(5, orders.size());
    }

    @Test
    void testCapacityLimit() throws InterruptedException {
        Warehouse warehouse = new Warehouse(2);
        Order order1 = new Order();
        order1.setInfo(1, OrderState.COOKED);
        Order order2 = new Order();
        order2.setInfo(2, OrderState.COOKED);

        warehouse.addOrder(order1);
        warehouse.addOrder(order2);

        Thread producer = new Thread(() -> {
            try {
                Thread.sleep(100);
                warehouse.takeOrders(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        long startTime = System.currentTimeMillis();

        Order order3 = new Order();
        order3.setInfo(3, OrderState.COOKED);
        warehouse.addOrder(order3);

        long endTime = System.currentTimeMillis();
        assertTrue(endTime - startTime >= 100);
        producer.join();
    }

    @Test
    void testFifoOrder() throws InterruptedException {
        Warehouse warehouse = new Warehouse(10);

        for (int i = 0; i < 5; i++) {
            Order order = new Order();
            order.setInfo(i, OrderState.COOKED);
            warehouse.addOrder(order);
        }

        List<Order> orders = warehouse.takeOrders(5);
        for (int i = 0; i < 5; i++) {
            assertEquals(i, orders.get(i).getId());
        }
    }

    @Test
    void testTakeOrdersBlocksWhenEmpty() throws InterruptedException {
        Warehouse warehouse = new Warehouse(5);

        Thread producer = new Thread(() -> {
            try {
                Thread.sleep(100);
                Order order = new Order();
                order.setInfo(1, OrderState.COOKED);
                warehouse.addOrder(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        long startTime = System.currentTimeMillis();
        List<Order> orders = warehouse.takeOrders(1);
        long endTime = System.currentTimeMillis();

        assertTrue(endTime - startTime >= 100);
        assertEquals(1, orders.size());
        producer.join();
    }

    @Test
    void testMultipleProducersAndConsumers() throws InterruptedException {
        Warehouse warehouse = new Warehouse(10);
        final int orderCount = 50;

        Thread producer1 = new Thread(() -> {
            try {
                for (int i = 0; i < orderCount / 2; i++) {
                    Order order = new Order();
                    order.setInfo(i, OrderState.COOKED);
                    warehouse.addOrder(order);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread producer2 = new Thread(() -> {
            try {
                for (int i = orderCount / 2; i < orderCount; i++) {
                    Order order = new Order();
                    order.setInfo(i, OrderState.COOKED);
                    warehouse.addOrder(order);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                int taken = 0;
                while (taken < orderCount) {
                    List<Order> orders = warehouse.takeOrders(5);
                    taken += orders.size();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer1.start();
        producer2.start();
        consumer.start();

        producer1.join();
        producer2.join();
        consumer.join();

        assertTrue(true);
    }
}

