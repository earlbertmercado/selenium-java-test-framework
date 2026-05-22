package io.github.earlbertmercado.selenium.driver;

import io.github.earlbertmercado.selenium.exceptions.FrameworkException;
import io.github.earlbertmercado.selenium.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;

public final class DriverFactory {

    private DriverFactory() {}

    public static WebDriver createDriverInstance() {
        String mode = ConfigReader.get("execution_mode").toLowerCase().trim();
        String browserName = ConfigReader.get("browser").toLowerCase().trim();

        // 1. Get the right browser strategy
        BrowserConfig browser = getBrowserConfig(browserName);

        // 2. Execute the strategy based on mode
        return switch (mode) {
            case "local" -> browser.createLocal();
            case "remote" -> createRemoteDriver(browser);
            default -> throw new FrameworkException("Unsupported execution mode: " + mode);
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

    private static WebDriver createRemoteDriver(BrowserConfig browser) {
        try {
            String gridUrl = ConfigReader.get("grid_url");
            return new RemoteWebDriver(URI.create(gridUrl).toURL(), browser.getRemoteCapabilities());
        } catch (MalformedURLException | IllegalArgumentException e) {
            throw new FrameworkException("Remote setup failed: invalid Selenium Hub URL configuration.", e);
        }
    }
}