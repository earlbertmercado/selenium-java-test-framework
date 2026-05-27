package io.github.earlbertmercado.selenium.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class FirefoxBrowser implements BrowserConfig {

    @Override
    public WebDriver createLocal() {
        return new FirefoxDriver(new FirefoxOptions().addArguments("--width=1920", "--height=1080"));
    }

    @Override
    public Capabilities getRemoteCapabilities() {
        return new FirefoxOptions();
    }
}
