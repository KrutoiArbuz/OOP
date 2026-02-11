package ru.nsu.masolygin;

import java.util.List;
import ru.nsu.masolygin.actor.employee.BakerProfile;
import ru.nsu.masolygin.actor.employee.CourierProfile;
import ru.nsu.masolygin.actor.employee.Worker;
import ru.nsu.masolygin.dto.PizzeriaContext;
import ru.nsu.masolygin.exception.PizzeriaInitializationException;
import ru.nsu.masolygin.fileloader.PizzeriaConfig;
import ru.nsu.masolygin.monitor.OrderQueue;
import ru.nsu.masolygin.monitor.Warehouse;
import ru.nsu.masolygin.view.ConsoleLogger;
import ru.nsu.masolygin.view.OrderLogger;

/**
 * Строитель пиццерии.
 */
public class PizzeriaBuilder {

    private final PizzeriaConfig config;

    /**
     * Конструктор.
     *
     * @param config конфигурация пиццерии
     */
    public PizzeriaBuilder(PizzeriaConfig config) {
        this.config = config;
    }

    /**
     * Создает пиццерию.
     *
     * @return готовая пиццерия
     * @throws PizzeriaInitializationException если конфигурация некорректна
     */
    public Pizzeria build() throws PizzeriaInitializationException {
        if (config.getWarehouseCapacity() <= 0) {
            throw new PizzeriaInitializationException("Warehouse capacity must more than 0");
        }
        if (config.getBakers().isEmpty()) {
            throw new PizzeriaInitializationException("Need at least one baker");
        }
        if (config.getCouriers().isEmpty()) {
            throw new PizzeriaInitializationException("Need at least one courier");
        }
        if (config.getWorkTime() <= 0) {
            throw new PizzeriaInitializationException(
                "We are live in boring universe, need a positive time value");

        }

        OrderLogger logger = new ConsoleLogger();
        OrderQueue queue = new OrderQueue();
        Warehouse warehouse = new Warehouse(config.getWarehouseCapacity());

        PizzeriaContext context = new PizzeriaContext(queue, warehouse, logger);

        List<Worker<BakerProfile>> bakers;
        List<Worker<CourierProfile>> couriers;

        try {
            bakers = config.getBakers().stream()
                .map(cfg -> {
                    try {
                        validateBakerConfig(cfg);
                        BakerProfile profile = new BakerProfile(cfg.getId(), cfg.getCookingTime());
                        return Worker.createBaker(profile, context);
                    } catch (PizzeriaInitializationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

            couriers = config.getCouriers().stream()
                .map(cfg -> {
                    try {
                        validateCourierConfig(cfg);
                        CourierProfile profile = new CourierProfile(cfg.getId(),
                            cfg.getDeliveryTime(),
                            cfg.getBackpackCapacity());
                        return Worker.createCourier(profile, context);
                    } catch (PizzeriaInitializationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
        } catch (RuntimeException e) {
            if (e.getCause() instanceof PizzeriaInitializationException) {
                throw (PizzeriaInitializationException) e.getCause();
            }
            throw new PizzeriaInitializationException(
                "Unexpected error during worker creation: " + e.getMessage(), e);
        }

        return new Pizzeria(config.getWorkTime(), queue, logger, bakers, couriers);
    }

    /**
     * Проверяет конфигурацию пекаря.
     *
     * @param cfg конфигурация пекаря
     * @throws PizzeriaInitializationException если параметры некорректны
     */
    private void validateBakerConfig(PizzeriaConfig.BakerConfig cfg)
        throws PizzeriaInitializationException {
        if (cfg.getId() <= 0) {
            throw new PizzeriaInitializationException(
                "Baker ID must be positive, got: " + cfg.getId());
        }
        if (cfg.getCookingTime() <= 0) {
            throw new PizzeriaInitializationException(
                "Baker cooking time must be positive, got: " + cfg.getCookingTime()
                    + " for baker ID: " + cfg.getId());
        }
    }

    /**
     * Проверяет конфигурацию курьера.
     *
     * @param cfg конфигурация курьера
     * @throws PizzeriaInitializationException если параметры некорректны
     */
    private void validateCourierConfig(PizzeriaConfig.CourierConfig cfg)
        throws PizzeriaInitializationException {
        if (cfg.getId() <= 0) {
            throw new PizzeriaInitializationException(
                "Courier ID must be positive, got: " + cfg.getId());
        }
        if (cfg.getDeliveryTime() <= 0) {
            throw new PizzeriaInitializationException(
                "Courier delivery time must be positive, got: " + cfg.getDeliveryTime()
                    + " for courier ID: " + cfg.getId());
        }
        if (cfg.getBackpackCapacity() <= 0) {
            throw new PizzeriaInitializationException(
                "Courier backpack capacity must be positive, got: " + cfg.getBackpackCapacity()
                    + " for courier ID: " + cfg.getId());
        }
    }
}
