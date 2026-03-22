package ru.nsu.masolygin;

import ru.nsu.masolygin.actor.OrderGenerator;
import ru.nsu.masolygin.exception.ConfigLoadException;
import ru.nsu.masolygin.exception.PizzeriaInitializationException;
import ru.nsu.masolygin.fileloader.ConfigLoader;
import ru.nsu.masolygin.fileloader.PizzeriaConfig;

/**
 * Main класс.
 */
public class Main {

    /**
     * Main.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        try {
            ConfigLoader loader = new ConfigLoader();
            PizzeriaConfig config = loader.load("src/main/resources/config.json");

            Pizzeria pizzeria = new PizzeriaBuilder(config).build();

            OrderGenerator ordersGenerator = new OrderGenerator(pizzeria);
            Thread generatorThread = new Thread(ordersGenerator);

            generatorThread.start();

            pizzeria.workGracefulShutdown();

            generatorThread.join();

        } catch (ConfigLoadException e) {
            System.err.println("Error start: incorrect configuration");
            System.err.println("Details: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Reason: " + e.getCause().getMessage());
            }
        } catch (PizzeriaInitializationException e) {
            System.err.println("Error start: incorrect data initialization");
            System.err.println("Details: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Reason: " + e.getCause().getMessage());
            }
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR: we're don't understand what is going");
            e.printStackTrace();
        }
    }
}
