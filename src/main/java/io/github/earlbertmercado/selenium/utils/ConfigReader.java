package io.github.earlbertmercado.selenium.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import io.github.earlbertmercado.selenium.exceptions.FrameworkException;

public final class ConfigReader {

    private static final String ENV_KEY = "env";
    private static final String DEFAULT_ENV = "test";
    private static final Properties properties = new Properties();

    static {
        String env = getCurrentEnvironment();
        String path = "/config/config-%s.properties".formatted(env);
        try (InputStream stream = ConfigReader.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new FrameworkException("No configuration found for env='" + env + "' at: " + path);
            }
            properties.load(stream);
        } catch (IOException e) {
            throw new FrameworkException("Failed to load classpath env config from: " + path, e);
        }
    }

    private ConfigReader() {}

    public static String get(String key) {
        String normalizedKey = key.trim().toLowerCase();
        String value = coalesce(
            System.getProperty(key), 
            System.getProperty(normalizedKey), 
            properties.getProperty(normalizedKey));
        if (value != null && !value.isBlank()) {
            return value;
        }
        throw new FrameworkException("Config key returned null/blank: " + key);
    }

    public static String getCurrentEnvironment() {
        return System.getProperty(ENV_KEY, DEFAULT_ENV).trim().toLowerCase();
    }


    /**
     * Returns the first non-null, non-blank value from the given candidates,
     * mirroring SQL COALESCE semantics. Used to simplify config key resolution
     * by collapsing the multi-source lookup chain (system property → normalized
     * system property → properties file) into a single readable call.
     */
    private static String coalesce(String... values) {
        for (String v : values) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }
}