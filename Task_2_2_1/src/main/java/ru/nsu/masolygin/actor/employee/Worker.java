package ru.nsu.masolygin.actor.employee;

import ru.nsu.masolygin.actor.workstrategy.BakerJob;
import ru.nsu.masolygin.actor.workstrategy.CourierJob;
import ru.nsu.masolygin.actor.workstrategy.WorkStrategy;
import ru.nsu.masolygin.dto.PizzeriaContext;

/**
 * Универсальный класс работника пиццерии.
 *
 * @param <T> тип профиля сотрудника, наследующий StaffProfile
 */
public class Worker<T extends StaffProfile> implements Runnable {

    /**
     * Профиль сотрудника.
     */
    private final T profile;

    /**
     * Стратегия работы сотрудника.
     */
    private final WorkStrategy<T> strategy;

    /**
     * Контекст пиццерии с доступом к очередям и логгеру.
     */
    private final PizzeriaContext context;

    /**
     * Конструктор работника.
     *
     * @param profile  профиль сотрудника
     * @param strategy стратегия работы
     * @param context  контекст пиццерии
     */
    public Worker(T profile, WorkStrategy<T> strategy, PizzeriaContext context) {
        this.profile = profile;
        this.strategy = strategy;
        this.context = context;
    }

    /**
     * Фабричный метод для создания пекаря.
     *
     * @param profile профиль пекаря
     * @param context контекст пиццерии
     * @return новый экземпляр работника-пекаря
     */
    public static Worker<BakerProfile> createBaker(BakerProfile profile, PizzeriaContext context) {
        return new Worker<>(profile, new BakerJob(), context);
    }

    /**
     * Фабричный метод для создания курьера.
     *
     * @param profile профиль курьера
     * @param context контекст пиццерии
     * @return новый экземпляр работника-курьера
     */
    public static Worker<CourierProfile> createCourier(CourierProfile profile,
    PizzeriaContext context) {
        return new Worker<>(profile, new CourierJob(), context);
    }

    /**
     * Основной цикл работы сотрудника.
     */
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                strategy.work(profile, context);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
