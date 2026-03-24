package ru.nsu.masolygin.actor.employee;

/**
 * Профиль пекаря.
 *
 * @param id          идентификатор пекаря
 * @param bakingSpeed время выпечки миллисекундах
 */
public record BakerProfile(int id, int bakingSpeed) implements StaffProfile {

}
