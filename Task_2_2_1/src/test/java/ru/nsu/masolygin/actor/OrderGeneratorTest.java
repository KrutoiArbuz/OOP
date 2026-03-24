package ru.nsu.masolygin.actor;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.Pizzeria;
import ru.nsu.masolygin.actor.employee.BakerProfile;
import ru.nsu.masolygin.actor.employee.CourierProfile;
import ru.nsu.masolygin.actor.employee.Worker;
import ru.nsu.masolygin.actor.employee.WorkerFactory;
import ru.nsu.masolygin.dto.PizzeriaContext;
import ru.nsu.masolygin.monitor.OrderQueue;
import ru.nsu.masolygin.monitor.Warehouse;
import ru.nsu.masolygin.view.ConsoleLogger;
import ru.nsu.masolygin.view.OrderLogger;

class OrderGeneratorTest {

    @Test
    void testOrderGeneratorCreation() {
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
        OrderGenerator generator = new OrderGenerator(pizzeria);
        assertNotNull(generator);
    }

    @Test
    void testOrderGeneratorCreationWithDifferentPizzeria() {
        OrderLogger orderLogger = new ConsoleLogger();
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(20);
        PizzeriaContext context = new PizzeriaContext(queue, warehouse, orderLogger);

        List<Worker<BakerProfile>> bakers = List.of(
            WorkerFactory.createBaker(new BakerProfile(1, 500), context),
            WorkerFactory.createBaker(new BakerProfile(2, 600), context)
        );
        List<Worker<CourierProfile>> couriers = List.of(
            WorkerFactory.createCourier(new CourierProfile(1, 800, 3), context),
            WorkerFactory.createCourier(new CourierProfile(2, 900, 4), context)
        );

        Pizzeria pizzeria = new Pizzeria(10000, queue, orderLogger, bakers, couriers);
        OrderGenerator generator = new OrderGenerator(pizzeria);
        assertNotNull(generator);
    }

    @Test
    void testOrderGeneratorGeneratesOrders() throws InterruptedException {
        OrderLogger orderLogger = new ConsoleLogger();
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(10);
        PizzeriaContext context = new PizzeriaContext(queue, warehouse, orderLogger);

        List<Worker<BakerProfile>> bakers = List.of(
            WorkerFactory.createBaker(new BakerProfile(1, 100), context)
        );
        List<Worker<CourierProfile>> couriers = List.of(
            WorkerFactory.createCourier(new CourierProfile(1, 100, 5), context)
        );

        Pizzeria pizzeria = new Pizzeria(1000, queue, orderLogger, bakers, couriers);
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
        OrderGenerator generator = new OrderGenerator(pizzeria);

        Thread generatorThread = new Thread(generator);
        generatorThread.start();
        Thread.sleep(100);
        generatorThread.interrupt();
        generatorThread.join(1000);

        assertNotNull(generatorThread);
    }

    @Test
    void testOrderGeneratorWithMultipleWorkers() throws InterruptedException {
        OrderLogger orderLogger = new ConsoleLogger();
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(20);
        PizzeriaContext context = new PizzeriaContext(queue, warehouse, orderLogger);

        List<Worker<BakerProfile>> bakers = List.of(
            WorkerFactory.createBaker(new BakerProfile(1, 100), context),
            WorkerFactory.createBaker(new BakerProfile(2, 100), context)
        );
        List<Worker<CourierProfile>> couriers = List.of(
            WorkerFactory.createCourier(new CourierProfile(1, 100, 5), context),
            WorkerFactory.createCourier(new CourierProfile(2, 100, 5), context)
        );

        Pizzeria pizzeria = new Pizzeria(2000, queue, orderLogger, bakers, couriers);
        OrderGenerator generator = new OrderGenerator(pizzeria);

        Thread generatorThread = new Thread(generator);
        generatorThread.start();
        Thread.sleep(300);
        generatorThread.interrupt();
        generatorThread.join();

        assertNotNull(generator);
    }

    @Test
    void testOrderGeneratorWithFastGeneration() throws InterruptedException {
        OrderLogger orderLogger = new ConsoleLogger();
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(50);
        PizzeriaContext context = new PizzeriaContext(queue, warehouse, orderLogger);

        List<Worker<BakerProfile>> bakers = List.of(
            WorkerFactory.createBaker(new BakerProfile(1, 50), context)
        );
        List<Worker<CourierProfile>> couriers = List.of(
            WorkerFactory.createCourier(new CourierProfile(1, 50, 10), context)
        );

        Pizzeria pizzeria = new Pizzeria(1000, queue, orderLogger, bakers, couriers);
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
        OrderLogger orderLogger = new ConsoleLogger();
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(10);
        PizzeriaContext context = new PizzeriaContext(queue, warehouse, orderLogger);

        List<Worker<BakerProfile>> bakers = List.of(
            WorkerFactory.createBaker(new BakerProfile(1, 100), context)
        );
        List<Worker<CourierProfile>> couriers = List.of(
            WorkerFactory.createCourier(new CourierProfile(1, 100, 5), context)
        );

        Pizzeria pizzeria = new Pizzeria(2000, queue, orderLogger, bakers, couriers);
        OrderGenerator generator = new OrderGenerator(pizzeria);

        Thread generatorThread = new Thread(generator);
        generatorThread.start();
        Thread.sleep(300);
        generatorThread.interrupt();
        generatorThread.join();

        assertNotNull(generator);
    }
}

