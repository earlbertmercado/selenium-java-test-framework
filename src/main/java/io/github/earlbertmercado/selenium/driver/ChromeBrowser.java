package io.github.earlbertmercado.selenium.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class ChromeBrowser implements BrowserConfig {

    @Override
    public WebDriver createLocal(boolean isHeadless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        if (isHeadless) {
            options.addArguments("--headless=new");
        }

        return new ChromeDriver(options);
    }

    @Override
    public Capabilities getRemoteCapabilities() {
        return new ChromeOptions();
    }
}