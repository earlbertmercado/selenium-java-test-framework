package io.github.earlbertmercado.selenium.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.github.earlbertmercado.selenium.base.BaseTest;
import io.github.earlbertmercado.selenium.constants.AppConstants;
import io.github.earlbertmercado.selenium.dataprovider.TestDataLoader;
import io.github.earlbertmercado.selenium.dataprovider.TestDataUsers;
import io.github.earlbertmercado.selenium.pages.InventoryPage;
import io.github.earlbertmercado.selenium.pages.LoginPage;

public class LoginTest extends BaseTest {

    protected LoginPage loginPage;

    @BeforeMethod(dependsOnMethods = "setUp")
    public void setUpLoginTest() {
        loginPage = new LoginPage();
    }

    @Test(description = "Verify successful user login routes into inventory page.")
    public void testValidLogin() {
        TestDataUsers user = TestDataLoader.getUser("standard_user");
        InventoryPage inventoryPage = loginPage.login(user.getUsername(), user.getPassword());

        assertion.assertEquals(
                inventoryPage.getCurrentUrl(),
                AppConstants.INVENTORY_PAGE_URL,
                "User is not routed to the inventory page.");
    }

    @Test(description = "Verify invalid login credentials throws error message.")
    public void testInvalidLoginCredentials() {
        loginPage.login("invalid_user", "invalid_password");

        assertion.assertTrue(
                loginPage.isErrorMessageVisible(),
                "Error message element is not visible.");
    }

    @Test(description = "Verify login page elements are visible.")
    public void testLoginElementsVisibility() {
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(
                loginPage.isUsernameInputFieldVisible(),
                "Username input field is not visible.");

        softAssert.assertTrue(
                loginPage.isPasswordInputFieldVisible(),
                "Password input field is not visible.");

        softAssert.assertTrue(
                loginPage.isLoginButtonVisible(),
                "Login button is not visible.");

        softAssert.assertAll();
    }
}