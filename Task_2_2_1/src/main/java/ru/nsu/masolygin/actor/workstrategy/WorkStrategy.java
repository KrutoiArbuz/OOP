package ru.nsu.masolygin.actor.workstrategy;

import ru.nsu.masolygin.actor.employee.StaffProfile;
import ru.nsu.masolygin.dto.PizzeriaContext;

/**
 * Интерфейс стратегии работы.
 */
public interface WorkStrategy<T extends StaffProfile> {

    void work(T profile, PizzeriaContext context) throws InterruptedException;
}
