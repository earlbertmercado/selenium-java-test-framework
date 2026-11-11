package io.github.earlbertmercado.selenium.driver;

import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;

import io.github.earlbertmercado.selenium.exceptions.FrameworkException;

/**
 * Thread-local storage for the current WebDriver instance.
 *
 * This class is used by test setup and page objects to retrieve the browser
 * instance bound to the currently executing thread.
 */
@NoArgsConstructor 
public final class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();


    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new FrameworkException("WebDriver is not initialized for the current thread.");
        }
        return driver;
    }

    public static boolean hasDriver() {
        return driverThreadLocal.get() != null;
    }

    public static void setDriver(WebDriver driverInstance) {
        driverThreadLocal.set(driverInstance);
    }

    public static void unload() {
        driverThreadLocal.remove();
    }
}