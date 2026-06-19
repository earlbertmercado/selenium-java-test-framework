package io.github.earlbertmercado.selenium.utils;

import io.github.earlbertmercado.selenium.exceptions.FrameworkException;

/*
 * Converts raw string values (e.g. from config or properties files) into
 * typed Java values. Currently supports boolean conversion only, accepting
 * common truthy/falsy tokens case-insensitively: "true"/"yes"/"1" and
 * "false"/"no"/"0".
 */
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