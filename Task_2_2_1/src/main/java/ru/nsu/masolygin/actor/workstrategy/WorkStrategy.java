package ru.nsu.masolygin.actor.workstrategy;

import ru.nsu.masolygin.actor.employee.StaffProfile;
import ru.nsu.masolygin.dto.PizzeriaContext;

/**
 * Интерфейс стратегии работы.
 *
 * @param <T> тип профиля сотрудника
 */
public interface WorkStrategy<T extends StaffProfile> {

    /**
     * Выполняет работу сотрудника.
     *
     * @param profile профиль сотрудника
     * @param context контекст пиццерии
     * @throws InterruptedException если поток прерван
     */
    void work(T profile, PizzeriaContext context) throws InterruptedException;
}
