package io.github.earlbertmercado.selenium.base;

import io.github.earlbertmercado.selenium.driver.DriverFactory;
import io.github.earlbertmercado.selenium.driver.DriverManager;
import io.github.earlbertmercado.selenium.pages.LoginPage;
import io.github.earlbertmercado.selenium.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.Assertion;
import org.testng.asserts.SoftAssert;

public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);

    protected Assertion assertion;
    protected SoftAssert softAssert;
    protected LoginPage loginPage;

    protected BaseTest() {}

    @BeforeMethod
    protected void setUp() {
        assertion = new Assertion();
        softAssert = new SoftAssert();

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
        log.info("Starting teardown process...");

        if (DriverManager.getDriver() != null) {
            DriverManager.getDriver().quit();
            DriverManager.unload();
            log.info("Driver closed and thread context cleared.");
        }
    }
}