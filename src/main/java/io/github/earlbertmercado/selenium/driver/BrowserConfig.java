package io.github.earlbertmercado.selenium.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

public interface BrowserConfig {

    WebDriver createLocal(boolean isHeadless);
    Capabilities getRemoteCapabilities();
}