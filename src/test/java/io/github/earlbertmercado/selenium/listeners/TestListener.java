package io.github.earlbertmercado.selenium.listeners;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.github.earlbertmercado.selenium.reports.ExtentLogger;
import io.github.earlbertmercado.selenium.reports.ExtentReportManager;

/**
 * TestNG listener for integrating test execution lifecycle events with logs and reports.
 *
 * Creates trace IDs for suite/test/fixture operations and populates ExtentReports entries
 * for pass/fail/skip outcomes.
 */
public final class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);
    private static final String TRACE_ID = "traceId";

    // Prefixes for different operation types (all 4 letters for consistent spacing)
    // SUIT = Suite, TEST = Test, FIXT = Fixture (setup/teardown)
    private static final String SUIT_PREFIX = "SUIT";
    private static final String TEST_PREFIX = "TEST";
    private static final String FIXT_PREFIX = "FIXT";

    // --- Global Suite Lifecycle Hooks ---

    @Override
    public void onStart(ITestContext context) {
        newTraceId(SUIT_PREFIX);
        log.info("Suite execution started.");
        ExtentReportManager.initReports();
    }

    @Override
    public void onFinish(ITestContext context) {
        newTraceId(SUIT_PREFIX);
        log.info("Suite execution completed.");
        ExtentReportManager.flushReports();

        // Clean up ThreadContext to prevent memory leaks in thread pools
        ThreadContext.clearAll();
    }

    // --- Individual Test Execution Lifecycle Hooks ---

    @Override
    public void onTestStart(ITestResult result) {
        String traceId = newTraceId(TEST_PREFIX);
        log.info("Starting test execution for: {}", result.getName());

        String testDescription = result.getMethod().getDescription();
        ExtentReportManager.createTest(getMethodName(result), getClassName(result), testDescription);

        ExtentTest currentTest = ExtentReportManager.getExtentTest();
        if (currentTest != null) {
            currentTest.log(Status.INFO, "Trace ID: " + traceId);
        }

        log.info("Test started: {}", getTestIdentifier(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("Test passed: {}", getTestIdentifier(result));

        ExtentTest currentTest = ExtentReportManager.getExtentTest();
        if (currentTest == null) {
            log.warn("No ExtentTest found for {}; skipping report logging.", getTestIdentifier(result));
        } else {
            currentTest.log(Status.PASS, "Test Passed: " + getMethodName(result));
            safely(() -> ExtentLogger.pass("All assertions passed successfully."));
        }

        finalizeTestContext();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Test failed: {}", getTestIdentifier(result));

        ExtentTest currentTest = ExtentReportManager.getExtentTest();
        if (currentTest == null) {
            log.warn("No ExtentTest found for {}; skipping report logging.", getTestIdentifier(result));
        } else {
            currentTest.log(Status.FAIL, "Test failed: " + getMethodName(result));

            Throwable throwable = result.getThrowable();
            if (throwable != null) {
                currentTest.log(Status.FAIL, throwable.getMessage());
                currentTest.log(Status.FAIL, throwable);
            }

            safely(() -> ExtentLogger.failWithScreenshot("Failure screenshot captured."));
        }

        finalizeTestContext();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("Test skipped: {}", getTestIdentifier(result));

        ExtentTest currentTest = ExtentReportManager.getExtentTest();
        if (currentTest == null) {
            log.warn("No ExtentTest found for {}; skipping report logging.", getTestIdentifier(result));
        } else {
            currentTest.log(Status.SKIP, "Test skipped: " + getMethodName(result));

            Throwable throwable = result.getThrowable();
            if (throwable != null) {
                currentTest.log(Status.INFO, throwable);
            }

            safely(() -> ExtentLogger.skip("Test execution skipped."));
        }

        finalizeTestContext();
    }

    // --- Internal String and Context Helpers ---

    private String getClassName(ITestResult result) {
        return result.getMethod().getTestClass().getRealClass().getSimpleName();
    }

    private String getMethodName(ITestResult result) {
        return result.getMethod().getMethodName();
    }

    private String getTestIdentifier(ITestResult result) {
        return getClassName(result) + "/" + getMethodName(result);
    }

    private String newTraceId(String prefix) {
        String traceId = prefix + "-" + UUID.randomUUID();
        ThreadContext.put(TRACE_ID, traceId);
        return traceId;
    }

    private void finalizeTestContext() {
        ExtentReportManager.unload();
        newTraceId(FIXT_PREFIX);
    }

    private void safely(Runnable reportingAction) {
        try {
            reportingAction.run();
        } catch (Exception exception) {
            log.error("Reporting action failed; continuing test execution.", exception);
        }
    }
}