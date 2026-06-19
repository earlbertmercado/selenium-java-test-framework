package io.github.earlbertmercado.selenium.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import io.github.earlbertmercado.selenium.exceptions.FrameworkException;

/**
 * Resolves configuration values for the currently running environment.
 *
 * Values come from /config/config-<env>.properties on the
 * classpath, where <env> is read from the env system
 * property (defaulting to "test"). Any key can be overridden at
 * runtime via a JVM system property (e.g. -Dsome.key=value), which
 * takes priority over the properties file.
 */
public final class ConfigReader {

    private static final String ENV_KEY = "env";
    private static final String DEFAULT_ENV = "test";
    private static Properties properties;

    private ConfigReader() {}

    public static String get(String key) {
        String normalizedKey = key.trim().toLowerCase();

        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = System.getProperty(normalizedKey);
        }
        if (value == null || value.isBlank()) {
            value = properties().getProperty(normalizedKey);
        }

        if (value != null && !value.isBlank()) {
            return value;
        }
        throw new FrameworkException("Config key returned null/blank: " + key);
    }

    public static String getCurrentEnvironment() {
        return System.getProperty(ENV_KEY, DEFAULT_ENV).trim().toLowerCase();
    }

    private static synchronized Properties properties() {
        if (properties == null) {
            properties = loadProperties();
        }
        return properties;
    }

    private static Properties loadProperties() {
        String env = getCurrentEnvironment();
        String path = "/config/config-%s.properties".formatted(env);

        Properties props = new Properties();
        try (InputStream stream = ConfigReader.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new FrameworkException("No configuration found for env='" + env + "' at: " + path);
            }
            props.load(stream);
        } catch (IOException e) {
            throw new FrameworkException("Failed to load classpath env config from: " + path, e);
        }
        return props;
    }
}