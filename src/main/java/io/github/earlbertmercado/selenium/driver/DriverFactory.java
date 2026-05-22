package io.github.earlbertmercado.selenium.driver;

import io.github.earlbertmercado.selenium.exceptions.FrameworkException;
import io.github.earlbertmercado.selenium.utils.ConfigReader;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public final class DriverFactory {
    private DriverFactory() {}

    public static WebDriver createDriverInstance() {
        WebDriver driver;
        String executionMode = ConfigReader.get("execution_mode");
        String browser = ConfigReader.get("browser");

        if (executionMode.equalsIgnoreCase("local")) {
            driver = createLocalDriver(browser);
        } else if (executionMode.equalsIgnoreCase("remote")) {
            driver = createRemoteDriver(browser);
        } else {
            throw new FrameworkException("Execution mode config mismatch: " + executionMode);
        }
        return driver;
    }

    private static WebDriver createLocalDriver(String browser) {
        return switch (browser.toLowerCase().trim()) {
            case "chrome" -> new ChromeDriver(new ChromeOptions().addArguments("--start-maximized"));
            case "firefox" -> new FirefoxDriver(new FirefoxOptions().addArguments("--width=1920", "--height=1080"));
            case "edge" -> new EdgeDriver(new EdgeOptions().addArguments("--start-maximized"));
            default -> throw new FrameworkException("Browser config mismatch: " + browser);
        };
    }

    private static WebDriver createRemoteDriver(String browser) {
        Capabilities capabilities = switch (browser.toLowerCase().trim()) {
            case "chrome" -> new ChromeOptions();
            case "firefox" -> new FirefoxOptions();
            case "edge" -> new EdgeOptions();
            default -> throw new FrameworkException("Remote browser designation profile mismatch: " + browser);
        };
        try {
            return new RemoteWebDriver(new URL(ConfigReader.get("grid_url")), capabilities);
        } catch (MalformedURLException e) {
            throw new FrameworkException("Remote setup failed: invalid Selenium Hub URL config.", e);
        }
    }
}
