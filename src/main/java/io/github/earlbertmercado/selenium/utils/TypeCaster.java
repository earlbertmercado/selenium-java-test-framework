package io.github.earlbertmercado.selenium.utils;

import io.github.earlbertmercado.selenium.exceptions.FrameworkException;

public final class TypeCaster {

    private TypeCaster() {}

    public static boolean toBoolean(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new FrameworkException("Boolean value cannot be null or empty");
        }

        String normalizedValue = value.toLowerCase().trim();

        return switch (normalizedValue) {
            case "true", "yes", "1" -> true;
            case "false", "no", "0" -> false;
            default -> throw new FrameworkException("Invalid boolean value: " + value);
        };
    }
}