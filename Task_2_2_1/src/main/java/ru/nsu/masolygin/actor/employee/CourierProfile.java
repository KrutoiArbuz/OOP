package ru.nsu.masolygin.actor.employee;

/**
 * Профиль курьера.
 *
 * @param id               идентификатор курьера
 * @param deliverySpeed    время доставки заказа в миллисекундах
 * @param backpackCapacity максимальное количество заказов, которое может нести курьер
 */
public record CourierProfile(int id, int deliverySpeed, int backpackCapacity) implements
StaffProfile {

}
