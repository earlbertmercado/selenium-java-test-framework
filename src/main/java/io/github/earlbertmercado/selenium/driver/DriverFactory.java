package io.github.earlbertmercado.selenium.driver;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.earlbertmercado.selenium.exceptions.FrameworkException;
import io.github.earlbertmercado.selenium.utils.ConfigReader;
import io.github.earlbertmercado.selenium.utils.TypeCaster;

public final class DriverFactory {

    private DriverFactory() {}

    public static WebDriver createDriverInstance() {
        String mode = ConfigReader.get("execution_mode").toLowerCase().trim();
        String browserName = ConfigReader.get("browser_name").toLowerCase().trim();
        boolean isHeadless = TypeCaster.toBoolean(ConfigReader.get("headless"));

        // Retrieve values, falling back safely to standard 1080p if keys are missing/empty
        String width = Objects.requireNonNullElse(ConfigReader.get("browser_width"), "1920").trim();
        String height = Objects.requireNonNullElse(ConfigReader.get("browser_height"), "1080").trim();

        // 1. Get the right browser strategy
        BrowserConfig browser = getBrowserConfig(browserName);

        // 2. Execute the strategy based on mode
        return switch (mode) {
            case "local" -> browser.createLocal(isHeadless, width, height);
            case "remote", "grid" -> createRemoteDriver(browser, width, height);
            default -> throw new FrameworkException("Unsupported execution mode: " + mode
                    + ". Supported values are local, remote, or grid.");
        };
    }

    private static BrowserConfig getBrowserConfig(String browserName) {
        return switch (browserName) {
            case "chrome" -> new ChromeBrowser();
            case "firefox" -> new FirefoxBrowser();
            case "edge" -> new EdgeBrowser();
            default -> throw new FrameworkException("Unsupported browser configuration: " + browserName);
        };
    }

    private static WebDriver createRemoteDriver(BrowserConfig browser, String width, String height) {
        try {
            String gridUrl = ConfigReader.get("grid_url");
            return new RemoteWebDriver(
                    URI.create(gridUrl).toURL(),
                    browser.getRemoteCapabilities(width, height)
            );
        } catch (MalformedURLException | IllegalArgumentException e) {
            throw new FrameworkException("Remote setup failed: invalid Selenium Hub URL configuration.", e);
        }
    }
}