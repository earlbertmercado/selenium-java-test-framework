package io.github.earlbertmercado.selenium.pages;

import org.openqa.selenium.By;

import io.github.earlbertmercado.selenium.constants.AppConstants;

public class ItemDetailPage extends BasePage {

    private final By itemName               = By.cssSelector(".inventory_details_name");
    private final By itemDescription        = By.cssSelector(".inventory_details_desc");
    private final By itemPrice              = By.cssSelector(".inventory_details_price");
    private final By itemImage              = By.cssSelector(".inventory_details_img");
    private final By backToProductsButton   = By.cssSelector(".inventory_details_back_button");
    private final By addToCartButton        = By.cssSelector(".btn_inventory");
    private final By removeToCartButton     = By.cssSelector(".btn_inventory");
    private final By shoppingCartBadge      = By.className("shopping_cart_badge");

    public boolean isRedirectedToItemDetailPage() {
        String currentUrl = getCurrentUrl();
        return currentUrl.contains(AppConstants.ITEM_DETAIL_PAGE_URL);
    }

    // ------------------ Getter Methods ------------------
    public String getItemName() {
        log.info("Getting item name text");
        return getText(itemName);
    }

    public String getItemDescription() {
        log.info("Getting item description text");
        return getText(itemDescription);
    }

    public double getItemPriceAsDouble() {
        log.info("Getting item price as double");
        String priceText = getText(itemPrice);
        return Double.parseDouble(priceText.replace("$", ""));
    }

    // ------------------ Visibility Methods ------------------

    public boolean isItemNameVisibleAndNotBlank() {
        return isVisible(itemName) && !getText(itemName).isBlank();
    }

    public boolean isItemDescriptionVisibleAndNotBlank() {
        return isVisible(itemDescription) && !getText(itemDescription).isBlank();
    }

    public boolean isItemPriceVisibleAndNotBlank() {
        return isVisible(itemPrice) && !getText(itemPrice).isBlank();
    }

    public boolean isItemImageVisible() {
        return isVisible(itemImage);
    }

    public boolean isAddToCartButtonVisible() {
        if(getText(addToCartButton).equalsIgnoreCase("Add to cart")) {
            return isVisible(addToCartButton);

        }
        return false;
    }

    public boolean isRemoveButtonVisible() {
        if(getText(removeToCartButton).equalsIgnoreCase("Remove")) {
            return isVisible(removeToCartButton);

        }
        return false;
    }

    public boolean isBackToProductsButtonVisible() {
        return isVisible(backToProductsButton);
    }

    public boolean isShoppingCartBadgeVisible() {
        return isVisible(shoppingCartBadge);
    }

    // ------------------ Action Methods ------------------

    public void clickBackToProducts() {
        log.info("Clicking 'Back to Products' button to return to Inventory Page");
        click(backToProductsButton);
    }

    public void clickAddToCartButton() {
        log.info("Clicking 'Add to Cart' button");
        click(addToCartButton);
    }

    public void clickRemoveButton() {
        log.info("Clicking 'Remove' button");
        if(getText(removeToCartButton).equalsIgnoreCase("Remove")) {
            click(removeToCartButton);
        }else {
            log.warn("'Remove' button is not visible.");
        }
    }
}