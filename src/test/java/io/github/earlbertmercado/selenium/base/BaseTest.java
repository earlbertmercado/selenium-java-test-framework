package io.github.earlbertmercado.selenium.base;

import io.github.earlbertmercado.selenium.driver.DriverFactory;
import io.github.earlbertmercado.selenium.driver.DriverManager;
import io.github.earlbertmercado.selenium.pages.LoginPage;
import io.github.earlbertmercado.selenium.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.asserts.Assertion;

import java.util.UUID;

@Listeners(io.github.earlbertmercado.selenium.listeners.TestListener.class)
public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);
    private static final String TRACE_ID = "traceId";
    private static final String FIXT_PREFIX = "FIXT";

    protected Assertion assertion;
    protected LoginPage loginPage;

    protected BaseTest() {}

    @BeforeMethod
    protected void setUp() {
        // Set FIXT prefix for setup operations
        String traceId = FIXT_PREFIX + "-" + UUID.randomUUID();
        ThreadContext.put(TRACE_ID, traceId);

        assertion = new Assertion();

        log.info("Initializing driver instance...");
        WebDriver driver = DriverFactory.createDriverInstance();
        DriverManager.setDriver(driver);

        log.info("Navigating to base URL...");
        DriverManager.getDriver().get(ConfigReader.get("base_url"));

        loginPage = new LoginPage();

        log.info("Setup completed successfully.");
    }

    @AfterMethod
    protected void tearDown() {
        try {
            String traceId = FIXT_PREFIX + "-" + UUID.randomUUID();
            ThreadContext.put(TRACE_ID, traceId);

            log.info("Starting teardown process...");

            if (DriverManager.getDriver() != null) {
                DriverManager.getDriver().quit();
                DriverManager.unload();
                log.info("Driver closed and thread context cleared.");
            }
        } finally {
            ThreadContext.clearAll();
        }
    }
}