package io.github.earlbertmercado.selenium.tests;

import io.github.earlbertmercado.selenium.base.BaseTest;
import io.github.earlbertmercado.selenium.constants.AppConstants;
import io.github.earlbertmercado.selenium.pages.InventoryPage;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Verify successful user authentication routes into inventory page.")
    public void testValidLogin() {
        InventoryPage inventoryPage = loginPage
                .login("standard_user", "secret_sauce");

        assertion.assertEquals(
                inventoryPage.getCurrentUrl(),
                AppConstants.INVENTORY_PAGE_URL,
                "Successfully redirected to inventory page"
        );
    }

    @Test(description = "Verify invalid login credentials throws error message")
    public void testInvalidLoginCredentials() {
        loginPage.login("invalid_user", "invalid_password");

        assertion.assertEquals(
                loginPage.isErrorMessageVisible(),
                true,
                "Error message element is visible");
    }
}