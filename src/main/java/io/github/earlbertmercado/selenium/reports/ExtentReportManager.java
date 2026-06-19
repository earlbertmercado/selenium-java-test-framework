package io.github.earlbertmercado.selenium.reports;

import java.util.Objects;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.earlbertmercado.selenium.constants.FrameworkConstants;
import io.github.earlbertmercado.selenium.exceptions.FrameworkException;
import io.github.earlbertmercado.selenium.utils.ConfigReader;

/*
 * Owns the lifecycle of the shared ExtentReports instance and the per-thread
 * "current test" handle used by listeners/hooks to log steps. initReports()
 * must run once before any test nodes are created via createTest(); if setup
 * fails partway (e.g. a missing config key), nothing is left half-initialized,
 * so a later retry of initReports() can succeed cleanly.
 */
public final class ExtentReportManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    private ExtentReportManager() {}

    // --- Report Lifecycle ---

    public static synchronized void initReports() {
        if (Objects.nonNull(extent)) {
            return;
        }

        ExtentReports newExtent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter(FrameworkConstants.getReportOutputPath());
        newExtent.attachReporter(spark);

        newExtent.setSystemInfo("OS", System.getProperty("os.name"));
        newExtent.setSystemInfo("Browser", ConfigReader.get("browser_name"));
        newExtent.setSystemInfo("Execution Mode", ConfigReader.get("execution_mode"));

        spark.config().setReportName("SauceDemo Test Suite Execution Report");
        spark.config().setDocumentTitle("Test Execution Automation Audit Logs");
        spark.config().setTheme(Theme.DARK);
        spark.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

        // Only assign once setup has fully succeeded.
        extent = newExtent;
    }

    public static void flushReports() {
        if (Objects.nonNull(extent)) {
            extent.flush();
        }
    }

    // --- ThreadLocal State Actions ---

    public static ExtentTest getExtentTest() {
        return extentTestThreadLocal.get();
    }

    public static void setExtentTest(ExtentTest test) {
        extentTestThreadLocal.set(test);
    }

    public static void unload() {
        extentTestThreadLocal.remove();
    }

    // --- Test Node Factory ---

    public static void createTest(String testCaseName, String tagName, String description) {
        if (Objects.isNull(extent)) {
            throw new FrameworkException("ExtentReportManager.initReports() must be called first");
        }

        ExtentTest test = extent.createTest(testCaseName).assignCategory(tagName);
        if (description != null && !description.isBlank()) {
            test.getModel().setDescription(description);
        }
        setExtentTest(test);
    }
}