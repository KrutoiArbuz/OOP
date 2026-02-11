package ru.nsu.masolygin.fileloader;

import java.util.List;

/**
 * Конфигурация пиццерии.
 */
public class PizzeriaConfig {

    private int workTime;
    private int warehouseCapacity;
    private List<BakerConfig> bakers;
    private List<CourierConfig> couriers;

    /**
     * Возвращает время работы.
     *
     * @return время работы
     */
    public int getWorkTime() {
        return workTime;
    }

    /**
     * Устанавливает время работы.
     *
     * @param workTime время работы
     */
    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    /**
     * Возвращает вместимость склада.
     *
     * @return вместимость склада
     */
    public int getWarehouseCapacity() {
        return warehouseCapacity;
    }

    /**
     * Устанавливает вместимость склада.
     *
     * @param warehouseCapacity вместимость склада
     */
    public void setWarehouseCapacity(int warehouseCapacity) {
        this.warehouseCapacity = warehouseCapacity;
    }

    /**
     * Возвращает список конфигураций пекарей.
     *
     * @return список конфигураций пекарей
     */
    public List<BakerConfig> getBakers() {
        return bakers;
    }

    /**
     * Устанавливает список конфигураций пекарей.
     *
     * @param bakers список конфигураций пекарей
     */
    public void setBakers(List<BakerConfig> bakers) {
        this.bakers = bakers;
    }

    /**
     * Возвращает список конфигураций курьеров.
     *
     * @return список конфигураций курьеров
     */
    public List<CourierConfig> getCouriers() {
        return couriers;
    }

    /**
     * Устанавливает список конфигураций курьеров.
     *
     * @param couriers список конфигураций курьеров
     */
    public void setCouriers(List<CourierConfig> couriers) {
        this.couriers = couriers;
    }

    /**
     * Класс конфигурации пекаря.
     */
    public static class BakerConfig {

        private int id;
        private int cookingTime;

        /**
         * Возвращает идентификатор.
         *
         * @return идентификатор
         */
        public int getId() {
            return id;
        }

        /**
         * Устанавливает идентификатор.
         *
         * @param id идентификатор
         */
        public void setId(int id) {
            this.id = id;
        }

        /**
         * Возвращает время приготовления.
         *
         * @return время приготовления
         */
        public int getCookingTime() {
            return cookingTime;
        }

        /**
         * Устанавливает время приготовления.
         *
         * @param cookingTime время приготовления
         */
        public void setCookingTime(int cookingTime) {
            this.cookingTime = cookingTime;
        }
    }

    /**
     * Класс конфигурации курьера.
     */
    public static class CourierConfig {

        private int id;
        private int deliveryTime;
        private int backpackCapacity;

        /**
         * Возвращает идентификатор.
         *
         * @return идентификатор
         */
        public int getId() {
            return id;
        }

        /**
         * Устанавливает идентификатор.
         *
         * @param id идентификатор
         */
        public void setId(int id) {
            this.id = id;
        }

        /**
         * Возвращает время доставки.
         *
         * @return время доставки
         */
        public int getDeliveryTime() {
            return deliveryTime;
        }

        /**
         * Устанавливает время доставки.
         *
         * @param deliveryTime время доставки
         */
        public void setDeliveryTime(int deliveryTime) {
            this.deliveryTime = deliveryTime;
        }

        /**
         * Возвращает вместимость рюкзака.
         *
         * @return вместимость рюкзака
         */
        public int getBackpackCapacity() {
            return backpackCapacity;
        }

        /**
         * Устанавливает вместимость рюкзака.
         *
         * @param backpackCapacity вместимость рюкзака
         */
        public void setBackpackCapacity(int backpackCapacity) {
            this.backpackCapacity = backpackCapacity;
        }
    }
}
