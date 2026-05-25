package io.github.earlbertmercado.selenium.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.github.earlbertmercado.selenium.reports.ExtentLogger;
import io.github.earlbertmercado.selenium.reports.ExtentReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.UUID;

public class TestListener implements ITestListener {
    private static final String TRACE_ID = "traceId";

    // Prefixes for different operation types (all 4 letters for consistent spacing)
    private static final String SUIT_PREFIX = "SUIT";
    private static final String TEST_PREFIX = "TEST";
    private static final String FIXT_PREFIX = "FIXT";

    private static final Logger log = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        String traceId = SUIT_PREFIX + "-" + UUID.randomUUID();
        ThreadContext.put(TRACE_ID, traceId);
        log.info("Suite execution started.");
        ExtentReportManager.initReports();
    }

    @Override
    public void onFinish(ITestContext context) {
        String traceId = SUIT_PREFIX + "-" + UUID.randomUUID();
        ThreadContext.put(TRACE_ID, traceId);
        log.info("Suite execution completed.");
        ExtentReportManager.flushReports();

        // Clean up ThreadContext to prevent memory leaks in thread pools
        ThreadContext.clearAll();
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Generate a unique ID with TEST prefix for the current test thread
        String traceId = TEST_PREFIX + "-" + UUID.randomUUID();
        ThreadContext.put(TRACE_ID, traceId);

        log.info("Starting test execution for: {}", result.getName());

        // Extract description from @Test annotation
        String testDescription = result.getMethod().getDescription();

        ExtentReportManager.createTest(getMethodName(result), getClassName(result), testDescription);
        ExtentReportManager.getExtentTest().log(Status.INFO, "Trace ID: " + traceId);

        log.info("Test started: {}", getTestIdentifier(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getExtentTest().log(Status.PASS, "Test Passed: " + getMethodName(result));
        log.info("Test passed: {}", getTestIdentifier(result));
        ExtentLogger.pass("All assertions passed successfully.");

        finalizeTestContext();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Test failed: {}", getTestIdentifier(result));

        ExtentTest currentTest = ExtentReportManager.getExtentTest();
        currentTest.log(Status.FAIL, "Test failed: " + getMethodName(result));
        currentTest.log(Status.FAIL, result.getThrowable().getMessage());
        currentTest.log(Status.FAIL, result.getThrowable());

        try {
            ExtentLogger.failWithScreenshot("Failure screenshot captured.");
        } catch (Exception exception) {
            log.error("Failed to attach screenshot.", exception);
        }

        finalizeTestContext();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getExtentTest().log(Status.SKIP, "Test skipped: " + getMethodName(result));
        ExtentReportManager.getExtentTest().log(Status.INFO, result.getThrowable());
        log.warn("Test skipped: {}", getTestIdentifier(result));
        ExtentLogger.fail("Test execution skipped.");

        finalizeTestContext();
    }

    // --- Helper Methods ---

    private String getClassName(ITestResult result) {
        return result.getMethod().getTestClass().getRealClass().getSimpleName();
    }

    private String getMethodName(ITestResult result) {
        return result.getMethod().getMethodName();
    }

    private String getTestIdentifier(ITestResult result) {
        return getClassName(result) + "/" + getMethodName(result);
    }

    private void finalizeTestContext() {
        ExtentReportManager.unload();
        String traceId = FIXT_PREFIX + "-" + UUID.randomUUID();
        ThreadContext.put(TRACE_ID, traceId);
    }
}