package io.github.earlbertmercado.selenium.utils;

import lombok.NoArgsConstructor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import io.github.earlbertmercado.selenium.driver.DriverManager;

@NoArgsConstructor
public final class ScreenshotUtils {

    // Captures a screenshot and returns it as a Base64 encoded string.
    public static String getBase64Image() {
        return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
    }
}
