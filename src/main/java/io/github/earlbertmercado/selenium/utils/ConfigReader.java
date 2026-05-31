package io.github.earlbertmercado.selenium.utils;

import io.github.earlbertmercado.selenium.exceptions.FrameworkException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream is = ConfigReader.class.getResourceAsStream("/config/config.properties")) {
            properties.load(is);
        } catch (IOException e) {
            throw new FrameworkException("Initial configuration file setup failed at destination route.", e);
        }
    }

    private ConfigReader() {}

    public static String get(String key) {
        String value = properties.getProperty(key.toLowerCase().trim());
        if (value == null) {
            throw new FrameworkException("Config key returned null: " + key);
        }
        return value;
    }
}
