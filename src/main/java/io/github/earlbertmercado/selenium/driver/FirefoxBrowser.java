package io.github.earlbertmercado.selenium.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class FirefoxBrowser implements BrowserConfig {

    @Override
    public WebDriver createLocal(boolean isHeadless) {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--width=1920", "--height=1080");

        if (isHeadless) {
            options.addArguments("--headless");
        }

        return new FirefoxDriver(options);
    }

    @Override
    public Capabilities getRemoteCapabilities() {
        return new FirefoxOptions();
    }
}