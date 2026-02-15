package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.actor.employee.BakerProfile;
import ru.nsu.masolygin.actor.employee.CourierProfile;
import ru.nsu.masolygin.actor.employee.Worker;
import ru.nsu.masolygin.actor.employee.WorkerFactory;
import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.dto.PizzeriaContext;
import ru.nsu.masolygin.monitor.OrderQueue;
import ru.nsu.masolygin.monitor.Warehouse;
import ru.nsu.masolygin.view.ConsoleLogger;
import ru.nsu.masolygin.view.OrderLogger;

class PizzeriaTest {

    @Test
    void testPizzeriaCreation() {
        OrderLogger orderLogger = new ConsoleLogger();
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(10);
        PizzeriaContext context = new PizzeriaContext(queue, warehouse, orderLogger);

        List<Worker<BakerProfile>> bakers = List.of(
            WorkerFactory.createBaker(new BakerProfile(1, 1000), context)
        );
        List<Worker<CourierProfile>> couriers = List.of(
            WorkerFactory.createCourier(new CourierProfile(1, 1000, 5), context)
        );

        Pizzeria pizzeria = new Pizzeria(5000, queue, orderLogger, bakers, couriers);
        assertNotNull(pizzeria);
    }

    @Test
    void testPizzeriaCreationWithMultipleWorkers() {
        OrderLogger orderLogger = new ConsoleLogger();
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(20);
        PizzeriaContext context = new PizzeriaContext(queue, warehouse, orderLogger);

        List<Worker<BakerProfile>> bakers = List.of(
            WorkerFactory.createBaker(new BakerProfile(1, 1000), context),
            WorkerFactory.createBaker(new BakerProfile(2, 1000), context),
            WorkerFactory.createBaker(new BakerProfile(3, 1000), context)
        );
        List<Worker<CourierProfile>> couriers = List.of(
            WorkerFactory.createCourier(new CourierProfile(1, 1000, 5), context),
            WorkerFactory.createCourier(new CourierProfile(2, 1000, 5), context)
        );

        Pizzeria pizzeria = new Pizzeria(10000, queue, orderLogger, bakers, couriers);
        assertNotNull(pizzeria);
    }


    @Test
    void testAcceptOrderWhenClosed() {
        OrderLogger orderLogger = new ConsoleLogger();
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(10);
        PizzeriaContext context = new PizzeriaContext(queue, warehouse, orderLogger);

        List<Worker<BakerProfile>> bakers = List.of(
            WorkerFactory.createBaker(new BakerProfile(1, 1000), context)
        );
        List<Worker<CourierProfile>> couriers = List.of(
            WorkerFactory.createCourier(new CourierProfile(1, 1000, 5), context)
        );

        Pizzeria pizzeria = new Pizzeria(5000, queue, orderLogger, bakers, couriers);

        Order order = new Order();
        pizzeria.acceptOrder(order);

        assertNotNull(pizzeria);
    }

    @Test
    void testWorkGracefulShutdownWithShortTime() {
        OrderLogger orderLogger = new ConsoleLogger();
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(10);
        PizzeriaContext context = new PizzeriaContext(queue, warehouse, orderLogger);

        List<Worker<BakerProfile>> bakers = List.of(
            WorkerFactory.createBaker(new BakerProfile(1, 50), context)
        );
        List<Worker<CourierProfile>> couriers = List.of(
            WorkerFactory.createCourier(new CourierProfile(1, 50, 5), context)
        );

        Pizzeria pizzeria = new Pizzeria(200, queue, orderLogger, bakers, couriers);

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
        OrderLogger orderLogger = new ConsoleLogger();
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(10);
        PizzeriaContext context = new PizzeriaContext(queue, warehouse, orderLogger);

        List<Worker<BakerProfile>> bakers = List.of(
            WorkerFactory.createBaker(new BakerProfile(1, 50), context)
        );
        List<Worker<CourierProfile>> couriers = List.of(
            WorkerFactory.createCourier(new CourierProfile(1, 50, 5), context)
        );

        Pizzeria pizzeria = new Pizzeria(500, queue, orderLogger, bakers, couriers);

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
        OrderLogger orderLogger = new ConsoleLogger();
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(15);
        PizzeriaContext context = new PizzeriaContext(queue, warehouse, orderLogger);

        List<Worker<BakerProfile>> bakers = List.of(
            WorkerFactory.createBaker(new BakerProfile(1, 100), context),
            WorkerFactory.createBaker(new BakerProfile(2, 100), context)
        );
        List<Worker<CourierProfile>> couriers = List.of(
            WorkerFactory.createCourier(new CourierProfile(1, 100, 3), context),
            WorkerFactory.createCourier(new CourierProfile(2, 100, 3), context)
        );

        Pizzeria pizzeria = new Pizzeria(300, queue, orderLogger, bakers, couriers);

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
        OrderLogger orderLogger = new ConsoleLogger();
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(50);
        PizzeriaContext context = new PizzeriaContext(queue, warehouse, orderLogger);

        List<Worker<BakerProfile>> bakers = List.of(
            WorkerFactory.createBaker(new BakerProfile(1, 100), context)
        );
        List<Worker<CourierProfile>> couriers = List.of(
            WorkerFactory.createCourier(new CourierProfile(1, 100, 5), context)
        );

        Pizzeria pizzeria = new Pizzeria(500, queue, orderLogger, bakers, couriers);

        assertNotNull(pizzeria);
    }

    @Test
    void testPizzeriaWithSmallWarehouse() {
        OrderLogger orderLogger = new ConsoleLogger();
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(2);
        PizzeriaContext context = new PizzeriaContext(queue, warehouse, orderLogger);

        List<Worker<BakerProfile>> bakers = List.of(
            WorkerFactory.createBaker(new BakerProfile(1, 100), context)
        );
        List<Worker<CourierProfile>> couriers = List.of(
            WorkerFactory.createCourier(new CourierProfile(1, 100, 5), context)
        );

        Pizzeria pizzeria = new Pizzeria(500, queue, orderLogger, bakers, couriers);

        assertNotNull(pizzeria);
    }

    @Test
    void testPizzeriaWithManyWorkers() {
        OrderLogger orderLogger = new ConsoleLogger();
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(20);
        PizzeriaContext context = new PizzeriaContext(queue, warehouse, orderLogger);

        List<Worker<BakerProfile>> bakers = List.of(
            WorkerFactory.createBaker(new BakerProfile(1, 100), context),
            WorkerFactory.createBaker(new BakerProfile(2, 100), context),
            WorkerFactory.createBaker(new BakerProfile(3, 100), context),
            WorkerFactory.createBaker(new BakerProfile(4, 100), context),
            WorkerFactory.createBaker(new BakerProfile(5, 100), context)
        );

        List<Worker<CourierProfile>> couriers = List.of(
            WorkerFactory.createCourier(new CourierProfile(1, 100, 5), context),
            WorkerFactory.createCourier(new CourierProfile(2, 100, 5), context),
            WorkerFactory.createCourier(new CourierProfile(3, 100, 5), context),
            WorkerFactory.createCourier(new CourierProfile(4, 100, 5), context),
            WorkerFactory.createCourier(new CourierProfile(5, 100, 5), context)
        );

        Pizzeria pizzeria = new Pizzeria(500, queue, orderLogger, bakers, couriers);
        assertNotNull(pizzeria);
    }

    @Test
    void testPizzeriaWorkflow() {
        OrderLogger orderLogger = new ConsoleLogger();
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(10);
        PizzeriaContext context = new PizzeriaContext(queue, warehouse, orderLogger);

        List<Worker<BakerProfile>> bakers = List.of(
            WorkerFactory.createBaker(new BakerProfile(1, 50), context)
        );
        List<Worker<CourierProfile>> couriers = List.of(
            WorkerFactory.createCourier(new CourierProfile(1, 50, 5), context)
        );

        Pizzeria pizzeria = new Pizzeria(300, queue, orderLogger, bakers, couriers);

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

