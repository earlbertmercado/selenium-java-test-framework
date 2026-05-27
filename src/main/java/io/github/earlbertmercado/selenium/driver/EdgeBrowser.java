package io.github.earlbertmercado.selenium.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class EdgeBrowser implements BrowserConfig {

    @Override
    public WebDriver createLocal() {
        return new EdgeDriver(new EdgeOptions().addArguments("--start-maximized"));
    }

    @Override
    public Capabilities getRemoteCapabilities() {
        return new EdgeOptions();
    }
}