package io.github.earlbertmercado.selenium.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.By;

import io.github.earlbertmercado.selenium.exceptions.FrameworkException;

public final class LocatorRepository {
    private static final Properties locators = new Properties();

    static {
        String env = ConfigReader.getCurrentEnvironment();
        String path = "/locators/locator-%s.properties".formatted(env);
        try (InputStream stream = LocatorRepository.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new FrameworkException("No locator file found for env='" + env + "' at: " + path);
            }
            locators.load(stream);
        } catch (IOException e) {
            throw new FrameworkException("Failed to load locators from: " + path, e);
        }
    }

    public static By get(String key) {
        String normalized = key.trim().toLowerCase();
        String value = locators.getProperty(normalized);
        if (value == null || value.isBlank()) {
            throw new FrameworkException("Locator not found for key: " + key);
        }
        return parseLocator(value);
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
