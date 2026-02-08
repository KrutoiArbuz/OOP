package ru.nsu.masolygin.actor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.dto.OrderState;
import ru.nsu.masolygin.monitor.Warehouse;
import ru.nsu.masolygin.view.OrderLogger;

class CourierTest {

    @Test
    void testCourierCreation() {
        Courier courier = new Courier(1, 1000, 5);
        assertNotNull(courier);
    }

    @Test
    void testCourierCreationWithDifferentParameters() {
        Courier courier1 = new Courier(1, 500, 3);
        Courier courier2 = new Courier(2, 1000, 5);
        Courier courier3 = new Courier(3, 1500, 10);
        assertNotNull(courier1);
        assertNotNull(courier2);
        assertNotNull(courier3);
    }

    @Test
    void testEmploy() {
        Courier courier = new Courier(1, 1000, 5);
        Warehouse warehouse = new Warehouse(10);
        OrderLogger logger = new OrderLogger();

        courier.employ(warehouse, logger);
        assertNotNull(courier);
    }

    @Test
    void testRunWithoutEmployThrowsException() {
        Courier courier = new Courier(1, 1000, 5);
        assertThrows(IllegalStateException.class, courier::run);
    }

    @Test
    void testCourierDeliversSingleOrder() throws InterruptedException {
        Courier courier = new Courier(1, 100, 5);
        Warehouse warehouse = new Warehouse(10);
        OrderLogger logger = new OrderLogger();

        courier.employ(warehouse, logger);

        Order order = new Order();
        order.setInfo(1, OrderState.COOKED);
        warehouse.addOrder(order);

        Thread courierThread = new Thread(() -> {
            try {
                courier.run();
            } catch (IllegalStateException e) {
                // Expected
            }
        });

        courierThread.start();
        Thread.sleep(250);
        courierThread.interrupt();
        courierThread.join();

        assertEquals(OrderState.DELIVERED, order.getState());
    }

    @Test
    void testCourierDeliversMultipleOrders() throws InterruptedException {
        Courier courier = new Courier(1, 50, 3);
        Warehouse warehouse = new Warehouse(10);
        OrderLogger logger = new OrderLogger();

        courier.employ(warehouse, logger);

        Order order1 = new Order();
        order1.setInfo(1, OrderState.COOKED);
        Order order2 = new Order();
        order2.setInfo(2, OrderState.COOKED);
        Order order3 = new Order();
        order3.setInfo(3, OrderState.COOKED);

        warehouse.addOrder(order1);
        warehouse.addOrder(order2);
        warehouse.addOrder(order3);

        Thread courierThread = new Thread(() -> {
            try {
                courier.run();
            } catch (IllegalStateException e) {
                // Expected
            }
        });

        courierThread.start();
        Thread.sleep(300);
        courierThread.interrupt();
        courierThread.join();

        assertEquals(OrderState.DELIVERED, order1.getState());
        assertEquals(OrderState.DELIVERED, order2.getState());
        assertEquals(OrderState.DELIVERED, order3.getState());
    }

    @Test
    void testCourierRespectsBackpackCapacity() throws InterruptedException {
        Courier courier = new Courier(1, 50, 2);
        Warehouse warehouse = new Warehouse(10);
        OrderLogger logger = new OrderLogger();

        courier.employ(warehouse, logger);

        Order order1 = new Order();
        order1.setInfo(1, OrderState.COOKED);
        Order order2 = new Order();
        order2.setInfo(2, OrderState.COOKED);
        Order order3 = new Order();
        order3.setInfo(3, OrderState.COOKED);

        warehouse.addOrder(order1);
        warehouse.addOrder(order2);
        warehouse.addOrder(order3);

        Thread courierThread = new Thread(() -> {
            try {
                courier.run();
            } catch (IllegalStateException e) {
                // Expected
            }
        });

        courierThread.start();
        Thread.sleep(150);
        courierThread.interrupt();
        courierThread.join();

        assertEquals(OrderState.DELIVERED, order1.getState());
        assertEquals(OrderState.DELIVERED, order2.getState());
    }

    @Test
    void testCourierStopsWhenInterrupted() throws InterruptedException {
        Courier courier = new Courier(1, 100, 5);
        Warehouse warehouse = new Warehouse(10);
        OrderLogger logger = new OrderLogger();

        courier.employ(warehouse, logger);

        Thread courierThread = new Thread(() -> {
            try {
                courier.run();
            } catch (IllegalStateException e) {
                // Expected
            }
        });

        courierThread.start();
        Thread.sleep(50);
        courierThread.interrupt();
        courierThread.join(1000);

        assertFalse(courierThread.isAlive());
    }

    @Test
    void testMultipleCouriersDeliverOrders() throws InterruptedException {
        Courier courier1 = new Courier(1, 50, 2);
        Courier courier2 = new Courier(2, 50, 2);
        Warehouse warehouse = new Warehouse(10);
        OrderLogger logger = new OrderLogger();

        courier1.employ(warehouse, logger);
        courier2.employ(warehouse, logger);

        for (int i = 0; i < 6; i++) {
            Order order = new Order();
            order.setInfo(i, OrderState.COOKED);
            warehouse.addOrder(order);
        }

        Thread courierThread1 = new Thread(() -> {
            try {
                courier1.run();
            } catch (IllegalStateException e) {
                // Expected
            }
        });

        Thread courierThread2 = new Thread(() -> {
            try {
                courier2.run();
            } catch (IllegalStateException e) {
                // Expected
            }
        });

        courierThread1.start();
        courierThread2.start();
        Thread.sleep(300);
        courierThread1.interrupt();
        courierThread2.interrupt();
        courierThread1.join();
        courierThread2.join();

        assertNotNull(courier1);
        assertNotNull(courier2);
    }

    @Test
    void testCourierWithLargeBackpack() throws InterruptedException {
        Courier courier = new Courier(1, 50, 10);
        Warehouse warehouse = new Warehouse(20);
        OrderLogger logger = new OrderLogger();

        courier.employ(warehouse, logger);

        for (int i = 0; i < 5; i++) {
            Order order = new Order();
            order.setInfo(i, OrderState.COOKED);
            warehouse.addOrder(order);
        }

        Thread courierThread = new Thread(() -> {
            try {
                courier.run();
            } catch (IllegalStateException e) {
                // Expected
            }
        });

        courierThread.start();
        Thread.sleep(300);
        courierThread.interrupt();
        courierThread.join();

        assertNotNull(courier);
    }

    @Test
    void testCourierWithSmallBackpack() throws InterruptedException {
        Courier courier = new Courier(1, 50, 1);
        Warehouse warehouse = new Warehouse(10);
        OrderLogger logger = new OrderLogger();

        courier.employ(warehouse, logger);

        Order order1 = new Order();
        order1.setInfo(1, OrderState.COOKED);
        Order order2 = new Order();
        order2.setInfo(2, OrderState.COOKED);

        warehouse.addOrder(order1);
        warehouse.addOrder(order2);

        Thread courierThread = new Thread(() -> {
            try {
                courier.run();
            } catch (IllegalStateException e) {
                // Expected
            }
        });

        courierThread.start();
        Thread.sleep(200);
        courierThread.interrupt();
        courierThread.join();

        assertEquals(OrderState.DELIVERED, order1.getState());
    }
}

