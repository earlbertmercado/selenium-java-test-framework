package io.github.earlbertmercado.selenium.driver;

import org.openqa.selenium.WebDriver;

public final class DriverManager {
    private DriverManager() {}

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void setDriver(WebDriver driverInstance) {
        driverThreadLocal.set(driverInstance);
    }

    public static void unload() {
        driverThreadLocal.remove();
    }
}
