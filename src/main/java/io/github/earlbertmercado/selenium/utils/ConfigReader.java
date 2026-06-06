package io.github.earlbertmercado.selenium.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import io.github.earlbertmercado.selenium.exceptions.FrameworkException;

public final class ConfigReader {

    private static final String ENV_KEY = "env";
    private static final String DEFAULT_ENV = "test";
    private static final String ENV_CONFIG_CLASSPATH_PATTERN = "/config/%s.properties";
    private static final Properties properties = new Properties();

    static {
        loadEnvironmentConfig();
    }

    private ConfigReader() {}

    public static String get(String key) {
        String normalizedKey = key.trim().toLowerCase();

        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = System.getProperty(normalizedKey);
        }
        if (value != null && !value.isBlank()) {
            return value;
        }

        value = properties.getProperty(normalizedKey);
        if (value != null && !value.isBlank()) {
            return value;
        }

        String secretRef = properties.getProperty(normalizedKey + "_ref");
        if (secretRef != null && !secretRef.isBlank()) {
            return resolveSecretReference(secretRef.trim(), key);
        }

        throw new FrameworkException("Config key returned null/blank: " + key);
    }

    public static String getCurrentEnvironment() {
        String env = System.getProperty(ENV_KEY, DEFAULT_ENV);
        return env.trim().toLowerCase();
    }

    private static void loadEnvironmentConfig() {
        String env = getCurrentEnvironment();
        String envConfigClasspath = String.format(ENV_CONFIG_CLASSPATH_PATTERN, env);

        try (InputStream envStream = ConfigReader.class.getResourceAsStream(envConfigClasspath)) {
            if (envStream == null) {
                throw new FrameworkException("No configuration found for env='" + env + "' at: " + envConfigClasspath);
            }
            properties.load(envStream);
        } catch (IOException e) {
            throw new FrameworkException("Failed to load classpath env config from: " + envConfigClasspath, e);
        }
    }

    private static String resolveSecretReference(String referenceName, String key) {
        String fromSystem = System.getProperty(referenceName);
        if (fromSystem != null && !fromSystem.isBlank()) {
            return fromSystem;
        }

        String fromEnv = System.getenv(referenceName);
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv;
        }

        throw new FrameworkException(
                "Missing secret value for config key: " + key + " via reference: " + referenceName
                        + ". Provide as -D" + referenceName + "=<value> or environment variable.");
    }
}