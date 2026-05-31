package io.github.earlbertmercado.selenium.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public final class EdgeBrowser implements BrowserConfig {

    @Override
    public WebDriver createLocal(boolean isHeadless) {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");

        if (isHeadless) {
            options.addArguments("--headless=new");
        }

        return new EdgeDriver(options);
    }

    @Override
    public Capabilities getRemoteCapabilities() {
        return new EdgeOptions();
    }
}