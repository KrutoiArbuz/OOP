package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.fileloader.PizzeriaConfig;
import ru.nsu.masolygin.fileloader.PizzeriaConfig.BakerConfig;
import ru.nsu.masolygin.fileloader.PizzeriaConfig.CourierConfig;

/**
 * Тест для PizzeriaBuilder.
 */
class PizzeriaBuilderTest {

    @Test
    void testBuildPizzeria() {
        PizzeriaConfig config = new PizzeriaConfig();
        config.setWorkTime(10000);
        config.setWarehouseCapacity(5);

        BakerConfig baker1 = new BakerConfig();
        baker1.setId(1);
        baker1.setCookingTime(1000);

        BakerConfig baker2 = new BakerConfig();
        baker2.setId(2);
        baker2.setCookingTime(1500);

        config.setBakers(List.of(baker1, baker2));

        CourierConfig courier1 = new CourierConfig();
        courier1.setId(1);
        courier1.setDeliveryTime(2000);
        courier1.setBackpackCapacity(3);

        CourierConfig courier2 = new CourierConfig();
        courier2.setId(2);
        courier2.setDeliveryTime(2500);
        courier2.setBackpackCapacity(2);

        config.setCouriers(List.of(courier1, courier2));

        PizzeriaBuilder builder = new PizzeriaBuilder(config);
        Pizzeria pizzeria = builder.build();

        assertNotNull(pizzeria, "Pizzeria should not be null");
    }

    @Test
    void testBuildPizzeriaWithEmptyWorkers() {
        PizzeriaConfig config = new PizzeriaConfig();
        config.setWorkTime(5000);
        config.setWarehouseCapacity(10);
        config.setBakers(List.of());
        config.setCouriers(List.of());

        PizzeriaBuilder builder = new PizzeriaBuilder(config);
        Pizzeria pizzeria = builder.build();

        assertNotNull(pizzeria, "Pizzeria should not be null even with no workers");
    }

    @Test
    void testBuildPizzeriaWithOnlyBakers() {
        PizzeriaConfig config = new PizzeriaConfig();
        config.setWorkTime(8000);
        config.setWarehouseCapacity(7);

        BakerConfig baker = new BakerConfig();
        baker.setId(1);
        baker.setCookingTime(1200);

        config.setBakers(List.of(baker));
        config.setCouriers(List.of());

        PizzeriaBuilder builder = new PizzeriaBuilder(config);
        Pizzeria pizzeria = builder.build();

        assertNotNull(pizzeria, "Pizzeria should not be null with only bakers");
    }

    @Test
    void testBuildPizzeriaWithOnlyCouriers() {
        PizzeriaConfig config = new PizzeriaConfig();
        config.setWorkTime(6000);
        config.setWarehouseCapacity(4);

        CourierConfig courier = new CourierConfig();
        courier.setId(1);
        courier.setDeliveryTime(1800);
        courier.setBackpackCapacity(5);

        config.setBakers(List.of());
        config.setCouriers(List.of(courier));

        PizzeriaBuilder builder = new PizzeriaBuilder(config);
        Pizzeria pizzeria = builder.build();

        assertNotNull(pizzeria, "Pizzeria should not be null with only couriers");
    }
}

