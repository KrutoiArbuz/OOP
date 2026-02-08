package ru.nsu.masolygin.fileLoader;

import java.util.List;

public class PizzeriaConfig {
    private int workTime;
    private int warehouseCapacity;
    private List<BakerConfig> bakers;
    private List<CourierConfig> couriers;

    public int getWorkTime() { return workTime; }
    public void setWorkTime(int workTime) { this.workTime = workTime; }

    public int getWarehouseCapacity() { return warehouseCapacity; }
    public void setWarehouseCapacity(int warehouseCapacity) { this.warehouseCapacity = warehouseCapacity; }

    public List<BakerConfig> getBakers() { return bakers; }
    public void setBakers(List<BakerConfig> bakers) { this.bakers = bakers; }

    public List<CourierConfig> getCouriers() { return couriers; }
    public void setCouriers(List<CourierConfig> couriers) { this.couriers = couriers; }

    public static class BakerConfig {
        private int id;
        private int cookingTime;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getCookingTime() { return cookingTime; }
        public void setCookingTime(int cookingTime) { this.cookingTime = cookingTime; }
    }

    public static class CourierConfig {
        private int id;
        private int deliveryTime;
        private int backpackCapacity;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getDeliveryTime() { return deliveryTime; }
        public void setDeliveryTime(int deliveryTime) { this.deliveryTime = deliveryTime; }
        public int getBackpackCapacity() { return backpackCapacity; }
        public void setBackpackCapacity(int backpackCapacity) { this.backpackCapacity = backpackCapacity; }
    }
}