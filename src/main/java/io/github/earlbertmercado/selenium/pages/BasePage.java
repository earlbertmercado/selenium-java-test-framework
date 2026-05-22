package io.github.earlbertmercado.selenium.pages;

import io.github.earlbertmercado.selenium.driver.DriverManager;
import io.github.earlbertmercado.selenium.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected final WebDriverWait explicitWait;

    protected BasePage() {
        long timeout = Long.parseLong(ConfigReader.get("timeout"));
        this.explicitWait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
    }

    public String getCurrentUrl() {
        return DriverManager.getDriver().getCurrentUrl();
    }

    protected void click(By locator) {
        WebElement element = explicitWait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    protected void sendKeys(By locator, String value) {
        WebElement element = explicitWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(value);
    }

    protected String getText(By locator) {
        return explicitWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    protected boolean isVisible(By locator) {
        try {
            explicitWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
