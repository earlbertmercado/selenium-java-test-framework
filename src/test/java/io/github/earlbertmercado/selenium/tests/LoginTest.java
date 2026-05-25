package io.github.earlbertmercado.selenium.tests;

import io.github.earlbertmercado.selenium.base.BaseTest;
import io.github.earlbertmercado.selenium.constants.AppConstants;
import io.github.earlbertmercado.selenium.pages.InventoryPage;
import io.github.earlbertmercado.selenium.dataprovider.TestDataLoader;
import io.github.earlbertmercado.selenium.dataprovider.TestDataUsers;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Verify successful user login routes into inventory page.")
    public void testValidLogin() {
        TestDataUsers user = TestDataLoader.getUser("standard_user");
        InventoryPage inventoryPage = loginPage
                .login(user.getUsername(), user.getPassword());

        assertion.assertEquals(
                inventoryPage.getCurrentUrl(),
                AppConstants.INVENTORY_PAGE_URL,
                "User is not routed to the inventory page."
        );
    }

    @Test(description = "Verify invalid login credentials throws error message.")
    public void testInvalidLoginCredentials() {
        loginPage.login("invalid_user", "invalid_password");

        assertion.assertEquals(
                loginPage.isErrorMessageVisible(),
                true,
                "Error message element is not visible");
    }

    @Test(description = "Verify login page elements are visible.")
    public void testLoginElementsVisibility() {
        softAssert.assertEquals(
                loginPage.isUsernameInputFieldVisible(),
                true,
                "Username input field is not visible"
        );
        softAssert.assertEquals(
                loginPage.isPasswordInputFieldVisible(),
                true,
                "Password input field is not visible"
        );
        softAssert.assertEquals(
                loginPage.isLoginButtonVisible(),
                true,
                "Login button is not visible");

        softAssert.assertAll();
    }
}