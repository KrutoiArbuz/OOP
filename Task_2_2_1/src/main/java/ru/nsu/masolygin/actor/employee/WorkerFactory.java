package ru.nsu.masolygin.actor.employee;

import ru.nsu.masolygin.actor.workstrategy.BakerJob;
import ru.nsu.masolygin.actor.workstrategy.CourierJob;
import ru.nsu.masolygin.dto.PizzeriaContext;

/**
 * Фабрика для создания рабочих
 */
public class WorkerFactory {

    /**
     * Приватный конструктор
     */
    private WorkerFactory() {
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
}
