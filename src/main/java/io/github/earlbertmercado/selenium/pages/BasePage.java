package io.github.earlbertmercado.selenium.pages;

import io.github.earlbertmercado.selenium.driver.DriverManager;
import io.github.earlbertmercado.selenium.exceptions.FrameworkException;
import io.github.earlbertmercado.selenium.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BasePage {

    protected final WebDriverWait explicitWait;
    protected final Logger log;

    protected BasePage() {
        long timeout = Long.parseLong(ConfigReader.get("timeout"));
        this.explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        this.log = LogManager.getLogger(this.getClass());
    }

    // --- Page Utilities ---

    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    // --- State Actions & Verifications ---

    protected boolean isVisible(By locator) {
        try {
            explicitWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            log.warn("Element not visible within timeout: {}", locator.toString());
            return false;
        }
    }

    // --- Single Element Actions ---

    protected void click(By locator) {
        log.info("Clicking element: {}", locator);
        explicitWait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void sendKeys(By locator, String value) {
        log.info("Entering text into element: {}", locator);
        WebElement element = explicitWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(value);
    }

    protected String getText(By locator) {
        return explicitWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    protected void selectByValue(By locator, String value) {
        log.info("Selecting value '{}' from dropdown: {}", value, locator);
        WebElement element = explicitWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        new Select(element).selectByValue(value);
    }

    // --- Multi-Element Actions ---

    protected int getElementCount(By locator) {
        return getDriver().findElements(locator).size();
    }

    protected WebElement getElementByIndex(By locator, int index) {
        List<WebElement> elements = getDriver().findElements(locator);
        if (index < 0 || index >= elements.size())
            throw new FrameworkException("Index " + index + " out of bounds for locator: " + locator);
        return elements.get(index);
    }

    protected List<String> getTexts(By locator) {
        return getDriver().findElements(locator).stream()
                .map(e -> e.getText().trim())
                .toList();
    }

    protected List<Double> getDoubleValues(By locator) {
        return getDriver().findElements(locator).stream()
                .map(e -> e.getText().replaceAll("[^0-9.]", "").trim())
                .filter(text -> !text.isEmpty())
                .map(Double::parseDouble)
                .toList();
    }

    // --- Internal Helpers ---

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}