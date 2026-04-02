package ru.nsu.masolygin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

/**
 * Загрузчик конфигурации из ресурсов.
 */
public class ConfigLoader {

    /**
     * Загружает объект конфигурации из JSON.
     *
     * @param resourcePath путь к ресурсу
     * @return конфигурация игры
     * @throws ConfigLoadException ошибка чтения или парсинга
     */
    public SnakeConfig load(String resourcePath) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = ConfigLoader.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new ConfigLoadException("Config file not found in classpath: " + resourcePath,
                    null);
            }
            return mapper.readValue(is, SnakeConfig.class);
        } catch (ConfigLoadException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigLoadException("Failed to parse config: " + resourcePath, e);
        }
    }
}
