package io.github.earlbertmercado.selenium.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;

import io.github.earlbertmercado.selenium.exceptions.FrameworkException;

/**
 * Loads locator definitions for the current environment from
 * /locators/locator-<env>.properties and resolves them into
 * Selenium By instances.
 *
 * Each property value must be in the form "strategy:selector",
 * e.g. login.button=id:loginBtn.
 */
public final class LocatorRepository {

    private static Map<String, String> locators;

    private LocatorRepository() {
    }

    public static By get(String key) {
        String value = locators().get(key.trim().toLowerCase());
        if (value == null || value.isBlank()) {
            throw new FrameworkException("Locator not found for key: " + key);
        }
        return parseLocator(value);
    }

    private static synchronized Map<String, String> locators() {
        if (locators == null) {
            locators = loadLocators();
        }
        return locators;
    }

    private static Map<String, String> loadLocators() {
        String env = ConfigReader.getCurrentEnvironment();
        String path = "/locators/locator-%s.properties".formatted(env);

        Properties raw = new Properties();
        try (InputStream stream = LocatorRepository.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new FrameworkException("No locator file found for env='" + env + "' at: " + path);
            }
            raw.load(stream);
        } catch (IOException e) {
            throw new FrameworkException("Failed to load locators from: " + path, e);
        }

        Map<String, String> normalized = new HashMap<>();
        raw.forEach((k, v) -> normalized.put(((String) k).trim().toLowerCase(), ((String) v).trim()));
        return normalized;
    }

    private static By parseLocator(String value) {
        String[] parts = value.split(":", 2);
        if (parts.length != 2) {
            throw new FrameworkException("Invalid locator format (expected 'strategy:selector'): " + value);
        }
        String strategy = parts[0].trim().toLowerCase();
        String selector = parts[1].trim();
        return switch (strategy) {
            case "id"              -> By.id(selector);
            case "name"            -> By.name(selector);
            case "css"             -> By.cssSelector(selector);
            case "xpath"           -> By.xpath(selector);
            case "linktext"        -> By.linkText(selector);
            case "partiallinktext" -> By.partialLinkText(selector);
            case "class"           -> By.className(selector);
            case "tag"             -> By.tagName(selector);
            default -> throw new FrameworkException("Unknown locator strategy: '" + strategy + "'");
        };
    }
}