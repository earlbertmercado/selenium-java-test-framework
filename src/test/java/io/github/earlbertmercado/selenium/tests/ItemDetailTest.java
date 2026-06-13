package io.github.earlbertmercado.selenium.tests;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.github.earlbertmercado.selenium.base.BaseTest;
import io.github.earlbertmercado.selenium.constants.AppConstants;
import io.github.earlbertmercado.selenium.dataprovider.TestDataLoader;
import io.github.earlbertmercado.selenium.dataprovider.TestDataUsers;
import io.github.earlbertmercado.selenium.pages.InventoryPage;
import io.github.earlbertmercado.selenium.pages.ItemDetailPage;
import io.github.earlbertmercado.selenium.pages.LoginPage;

public class ItemDetailTest extends BaseTest {

    private InventoryPage inventoryPage;
    private ItemDetailPage itemDetailPage;
    protected LoginPage loginPage;

    @BeforeMethod(dependsOnMethods = "setUp")
    public void navigateToInventoryPage() {
        TestDataUsers user = TestDataLoader.getUser("standard_user");
        loginPage = new LoginPage();
        inventoryPage = loginPage.login(user.getUsername(), user.getPassword());
        itemDetailPage = new ItemDetailPage();
    }

    @Test(description = "Verify that the item detail page loads correctly with all necessary elements visible")
    public void testItemDetailPageLoadsCorrectly() {
        SoftAssert softAssert = new SoftAssert();
        final int FIRST_ITEM = 0;

        inventoryPage.clickItemNameByIndex(FIRST_ITEM);

        log.info("Verifying item detail page loads with all UI components.");

        softAssert.assertTrue(
                itemDetailPage.isRedirectedToItemDetailPage(),
                "User is not redirected to the item detail page.");

        softAssert.assertTrue(
                itemDetailPage.isItemNameVisibleAndNotBlank(),
                "Item name is not visible or is blank.");

        softAssert.assertTrue(
                itemDetailPage.isItemDescriptionVisibleAndNotBlank(),
                "Item description is not visible or is blank.");

        softAssert.assertTrue(
                itemDetailPage.isItemPriceVisibleAndNotBlank(),
                "Item price is not visible or is blank.");

        softAssert.assertTrue(
                itemDetailPage.isItemImageVisible(),
                "Item image is not visible.");

        softAssert.assertTrue(
                itemDetailPage.isAddToCartButtonVisible(),
                "Add to cart button is not visible.");

        softAssert.assertTrue(
                itemDetailPage.isBackToProductsButtonVisible(),
                "Back to products button is not visible.");

        softAssert.assertAll();
    }

    @Test(description = "Add item from detail page updates cart badge and allows removal")
    public void testAddThenRemoveItemToCart() {
        SoftAssert softAssert = new SoftAssert();
        final int FIRST_ITEM = 0;
        final int EXPECTED_CART_BADGE_DISPLAYED_NUMBER = 1;

        inventoryPage.clickItemNameByIndex(FIRST_ITEM);

        log.info("Adding item to cart from detail page.");
        itemDetailPage.clickAddToCartButton();

        int badgeCount = inventoryPage.getCartItemCount();
        log.info("Verified cart badge updated to: {}", badgeCount);

        softAssert.assertEquals(
                badgeCount,
                EXPECTED_CART_BADGE_DISPLAYED_NUMBER,
                "Cart badge does not display the expected number after adding item.");

        softAssert.assertTrue(
                itemDetailPage.isRemoveButtonVisible(),
                "Remove button is not visible after adding item to cart.");

        log.info("Removing item from cart using remove button on detail page.");
        itemDetailPage.clickRemoveButton();

        boolean isCartBadgeVisible = itemDetailPage.isShoppingCartBadgeVisible();
        log.info("Verified cart badge visibility after removing item: {}", isCartBadgeVisible);

        softAssert.assertFalse(
                isCartBadgeVisible,
                "Cart badge is still visible after removing item from cart.");

        softAssert.assertTrue(
                itemDetailPage.isAddToCartButtonVisible(),
                "Add to cart button is not visible after removing item from cart.");

        softAssert.assertAll();
    }

    @Test(description = "Verify that clicking 'Back to Products' button returns user to inventory page")
    public void testBackToProductsButton() {
        final int FIRST_ITEM = 0;

        inventoryPage.clickItemNameByIndex(FIRST_ITEM);

        log.info("Clicking 'Back to Products' button on item detail page.");
        itemDetailPage.clickBackToProducts();

        assertion.assertTrue(
                inventoryPage.getCurrentUrl().endsWith("inventory.html"),
                "Clicking 'Back to Products' fails to return to inventory page");
    }

    @Test(description = "Verify that specific item detail page displays correct information")
    public void testSpecificItemDetailPage() {
        SoftAssert softAssert = new SoftAssert();
        final int FLEE_JACKET_ID = 5;
        final double EXPECTED_PRICE = 49.99;
        final By fleeJacket = By.id("item_5_title_link");

        log.info("Testing specific item detail page for 'Fleece Jacket'.");
        inventoryPage.clickSpecificItem(fleeJacket);

        String actualName = itemDetailPage.getItemName();
        double actualPrice = itemDetailPage.getItemPriceAsDouble();

        log.info("Verifying specific product details: " +
                "[Expected Name: Sauce Labs Fleece Jacket, Actual: {}]", actualName);
        log.info("Verifying product price: [Expected: ${}, Actual: ${}]", EXPECTED_PRICE, actualPrice);

        assertion.assertTrue(
                itemDetailPage.getCurrentUrl().contains(AppConstants.ITEM_DETAIL_PAGE_URL + FLEE_JACKET_ID),
                "URL does not contain expected item ID for Fleece Jacket.");

        softAssert.assertEquals(
                actualName,
                "Sauce Labs Fleece Jacket",
                "Item name does not match expected value for Fleece Jacket.");

        softAssert.assertEquals(
                actualPrice,
                EXPECTED_PRICE,
                "Item price does not match expected value for Fleece Jacket.");

        softAssert.assertEquals(
                itemDetailPage.getItemDescription(),
                "It's not every day that you come across a midweight quarter-zip " +
                "fleece jacket capable of handling everything from a relaxing day " +
                "outdoors to a busy day at the office.",
                "Item description does not match expected value for Fleece Jacket.");

        softAssert.assertAll();
    }

    @Test(description = "Verify that item price is not zero")
    public void testItemPriceNotZero() {
        final int FIRST_ITEM = 0;

        inventoryPage.clickItemNameByIndex(FIRST_ITEM);

        double itemPrice = itemDetailPage.getItemPriceAsDouble();
        log.info("Verifying item price is not zero. Actual price: ${}", itemPrice);

        assertion.assertTrue(
                itemPrice > 0,
                "Item price should be greater than zero, but was: $" + itemPrice);
    }
}