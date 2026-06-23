package io.github.earlbertmercado.selenium.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

/**
 * Contract for browser-specific WebDriver configuration.
 *
 * Implementations provide both local browser creation and remote capability
 * generation for grid or remote execution.
 */
public interface BrowserConfig {

    WebDriver createLocal(boolean isHeadless, String width, String height);
    
    Capabilities getRemoteCapabilities(String width, String height);
}