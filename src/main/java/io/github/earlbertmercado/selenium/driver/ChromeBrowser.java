package io.github.earlbertmercado.selenium.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class ChromeBrowser implements BrowserConfig {

    @Override
    public WebDriver createLocal() {
        return new ChromeDriver(new ChromeOptions().addArguments("--start-maximized"));
    }

    @Override
    public Capabilities getRemoteCapabilities() {
        return new ChromeOptions();
    }
}
