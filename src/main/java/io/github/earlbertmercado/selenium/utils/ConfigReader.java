package io.github.earlbertmercado.selenium.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import io.github.earlbertmercado.selenium.exceptions.FrameworkException;

public final class ConfigReader {

    private static final String CONFIG_PATH = "/config/config.properties";
    private static final Properties properties = new Properties();

    static {
        try (InputStream is = ConfigReader.class.getResourceAsStream(CONFIG_PATH)) {
            if (is == null) {
                throw new FrameworkException("Configuration resource not found at path: " + CONFIG_PATH);
            }
            properties.load(is);
        } catch (IOException e) {
            throw new FrameworkException("Failed to load configuration file from: " + CONFIG_PATH, e);
        }
    }

    private ConfigReader() {}

    public static String get(String key) {
        String normalizedKey = key.toLowerCase().trim();

        // Priority 1: Check system properties (Maven -D args take precedence)
        String systemValue = System.getProperty(normalizedKey);
        if (systemValue != null && !systemValue.isEmpty()) {
            return systemValue;
        }

        // Priority 2: Fall back to properties file
        String fileValue = properties.getProperty(normalizedKey);
        if (fileValue == null) {
            throw new FrameworkException("Config key returned null: " + key);
        }

        return fileValue;
    }
}