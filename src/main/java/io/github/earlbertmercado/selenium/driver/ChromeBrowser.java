package io.github.earlbertmercado.selenium.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class ChromeBrowser implements BrowserConfig {

    @Override
    public WebDriver createLocal(boolean isHeadless, String width, String height) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(String.format("--window-size=%s,%s", width, height));
        if (isHeadless) {
            options.addArguments("--headless=new");
        }
        return new ChromeDriver(options);
    }

    @Override
    public Capabilities getRemoteCapabilities(String width, String height) {
        return new ChromeOptions().addArguments(String.format("--window-size=%s,%s", width, height));
    }
}