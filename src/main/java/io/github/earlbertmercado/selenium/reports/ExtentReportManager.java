package io.github.earlbertmercado.selenium.reports;

import java.util.Objects;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.earlbertmercado.selenium.constants.FrameworkConstants;
import io.github.earlbertmercado.selenium.utils.ConfigReader;

public final class ExtentReportManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    private ExtentReportManager() {}

    // --- Report Lifecycle ---

    public static synchronized void initReports() {
        if (Objects.isNull(extent)) {
            extent = new ExtentReports();
            ExtentSparkReporter spark = new ExtentSparkReporter(FrameworkConstants.getReportOutputPath());
            extent.attachReporter(spark);

            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Browser", ConfigReader.get("browser_name"));
            extent.setSystemInfo("Execution Mode", ConfigReader.get("execution_mode"));

            spark.config().setReportName("SauceDemo Test Suite Execution Report");
            spark.config().setDocumentTitle("Test Execution Automation Audit Logs");
            spark.config().setTheme(Theme.DARK);
            spark.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        }
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
        ExtentTest test = extent.createTest(testCaseName).assignCategory(tagName);
        if (description != null && !description.isEmpty()) {
            test.getModel().setDescription(description);
        }
        setExtentTest(test);
    }
}