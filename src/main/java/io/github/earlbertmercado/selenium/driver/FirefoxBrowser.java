package io.github.earlbertmercado.selenium.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class FirefoxBrowser implements BrowserConfig {

    @Override
    public WebDriver createLocal(boolean isHeadless, String width, String height) {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments(
                String.format("--width=%s", width),
                String.format("--height=%s", height)
        );
        if (isHeadless) {
            options.addArguments("--headless=new");
        }
        return new FirefoxDriver(options);
    }

    @Override
    public Capabilities getRemoteCapabilities(String width, String height) {
        return new FirefoxOptions().addArguments(
                String.format("--width=%s", width),
                String.format("--height=%s", height)
        );
    }
}