package io.github.earlbertmercado.selenium.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public final class EdgeBrowser implements BrowserConfig {

    @Override
    public WebDriver createLocal(boolean isHeadless, String width, String height) {
        EdgeOptions options = new EdgeOptions();
        options.addArguments(String.format("--window-size=%s,%s", width, height));
        if (isHeadless) {
            options.addArguments("--headless=new");
        }
        return new EdgeDriver(options);
    }

    @Override
    public Capabilities getRemoteCapabilities(String width, String height) {
        return new EdgeOptions().addArguments(String.format("--window-size=%s,%s", width, height));
    }
}