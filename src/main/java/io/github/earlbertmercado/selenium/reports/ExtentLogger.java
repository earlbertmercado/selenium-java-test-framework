package io.github.earlbertmercado.selenium.reports;

import com.aventstack.extentreports.MediaEntityBuilder;
import io.github.earlbertmercado.selenium.utils.ScreenshotUtils;

public final class ExtentLogger {
    private ExtentLogger() {}

    public static void pass(String message) {
        ExtentReportManager.getExtentTest().pass(message);
    }

    public static void fail(String message) {
        ExtentReportManager.getExtentTest().fail(message);
    }
    public static void info(String message)  {
        ExtentReportManager.getExtentTest().info(message);
    }

    public static void failWithScreenshot(String message) {
        ExtentReportManager.getExtentTest().fail(message,
                MediaEntityBuilder.createScreenCaptureFromBase64String(ScreenshotUtils.getBase64Image()).build());
    }
}
