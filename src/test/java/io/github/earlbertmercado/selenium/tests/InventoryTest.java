package io.github.earlbertmercado.selenium.tests;

import io.github.earlbertmercado.selenium.base.BaseTest;
import io.github.earlbertmercado.selenium.constants.AppConstants;
import io.github.earlbertmercado.selenium.dataprovider.TestDataLoader;
import io.github.earlbertmercado.selenium.dataprovider.TestDataUsers;
import io.github.earlbertmercado.selenium.pages.InventoryPage;
import io.github.earlbertmercado.selenium.pages.ItemDetailPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class InventoryTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod
    public void navigateToInventoryPage() {
        TestDataUsers user = TestDataLoader.getUser("standard_user");
        inventoryPage = loginPage.login(user.getUsername(), user.getPassword());
    }

    @Test(description = "Verify that the inventory page elements are visible")
    public void testInventoryPageElementsVisibility() {
        SoftAssert softAssert = new SoftAssert();

        log.info("Verifying inventory page elements visibility...");

        assertion.assertEquals(
                inventoryPage.getCurrentUrl(),
                AppConstants.INVENTORY_PAGE_URL,
                "User is not on the inventory page.");

        softAssert.assertTrue(
                inventoryPage.isBurgerMenuVisible(),
                "Burger menu element is not visible.");

        softAssert.assertTrue(
                inventoryPage.isShoppingCartLinkVisible(),
                "Shopping cart link element is not visible.");

        softAssert.assertTrue(
                inventoryPage.isSortDropDownVisible(),
                "Sort drop down element is not visible.");

        softAssert.assertTrue(
                inventoryPage.areItemsVisible(),
                "Inventory items are not visible.");

        softAssert.assertTrue(
                inventoryPage.areAddToCartButtonsVisible(),
                "Add to cart buttons are not visible for each item."
        );

        softAssert.assertAll();
    }

    @Test(description = "Verify that sorting functionality works")
    public void testSortingFunctionality() {
        log.info("Verifying sorting functionality...");

        inventoryPage.sortByNameDesc();
        assertion.assertTrue(
                inventoryPage.isSortedAlphabeticallyDesc(),
                "Products are not sorted alphabetically in descending order.");

        inventoryPage.sortByNameAsc();
        assertion.assertTrue(
                inventoryPage.isSortedAlphabeticallyAsc(),
                "Products are not sorted alphabetically in ascending order.");

        inventoryPage.sortByPriceAsc();
        assertion.assertTrue(
                inventoryPage.isSortedByPriceAsc(),
                "Products are not sorted by price in ascending order.");

        inventoryPage.sortByPriceDesc();
        assertion.assertTrue(
                inventoryPage.isSortedByPriceDesc(),
                "Products are not sorted by price in descending order.");
    }

    @Test(description = "Verify that each item detail is valid.")
    public void testEachItemDetails() {
        log.info("Verifying item details validity");
        assertion.assertTrue(
                inventoryPage.areAllItemsValid(),
                "One or more inventory items contain invalid details.");
    }

    @Test(description = "Verify cart badge after adding items ")
    public void testCartBadgeAfterAddingItems(){
        final int FIRST_ITEM = 0;
        final int SECOND_ITEM = 1;
        final int THIRD_ITEM = 2;
        final int EXPECTED_CART_BADGE_COUNT = 3;

        inventoryPage.addItemsToCart(FIRST_ITEM, SECOND_ITEM, THIRD_ITEM);

        int actualCount = inventoryPage.getCartItemCount();

        log.info("Verification: Cart badge count is {}", actualCount);

        assertion.assertEquals(actualCount,
                EXPECTED_CART_BADGE_COUNT,
                "Cart badge count does not match expected count after adding items.");
    }

    @Test(description = "Verify cart badge after removing items ")
    public void testCartBadgeAfterRemovingItems() {
        final int FIRST_ITEM = 0;
        final int SECOND_ITEM = 1;
        final int THIRD_ITEM = 2;
        final int EXPECTED_CART_BADGE_COUNT = 0;

        inventoryPage.addThenRemoveItems(FIRST_ITEM, SECOND_ITEM, THIRD_ITEM);

        int cartItemCount = inventoryPage.getCartItemCount();

        assertion.assertEquals(cartItemCount,
                EXPECTED_CART_BADGE_COUNT,
                "Cart badge count does not match expected count after removing items.");
    }

    @Test(description = "Verify if clicking item navigates to item detail page")
    public void testClickingItemNavigatesToItemDetailPage() {
        final int FIRST_ITEM = 0;
        ItemDetailPage itemDetailPage = inventoryPage.clickItemNameByIndex(FIRST_ITEM);

        log.info("Verifying navigation to Detail Page for item index {}", FIRST_ITEM);

        assertion.assertTrue(
                itemDetailPage.isRedirectedToItemDetailPage(),
                "Failed to navigate to item detail page."
        );
    }

}
