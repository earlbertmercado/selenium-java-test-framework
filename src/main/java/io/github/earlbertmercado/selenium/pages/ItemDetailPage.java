package io.github.earlbertmercado.selenium.pages;

import org.openqa.selenium.By;

import io.github.earlbertmercado.selenium.constants.AppConstants;
import io.github.earlbertmercado.selenium.utils.LocatorRepository;

public class ItemDetailPage extends BasePage {
    
    private final By itemName               = LocatorRepository.get("item_detail.name");
    private final By itemDescription        = LocatorRepository.get("item_detail.description");
    private final By itemPrice              = LocatorRepository.get("item_detail.price");
    private final By itemImage              = LocatorRepository.get("item_detail.image");
    private final By backToProductsButton   = LocatorRepository.get("item_detail.back");
    private final By addToCartButton        = LocatorRepository.get("item_detail.add_to_cart");
    private final By removeToCartButton     = LocatorRepository.get("item_detail.remove");
    private final By shoppingCartBadge      = LocatorRepository.get("item_detail.cart_badge");

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
        return isVisible(addToCartButton)
                && getText(addToCartButton).equalsIgnoreCase("Add to cart");
    }

    public boolean isRemoveButtonVisible() {
        return isVisible(removeToCartButton)
                && getText(removeToCartButton).equalsIgnoreCase("Remove");
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
        if (isVisible(removeToCartButton)
                && getText(removeToCartButton).equalsIgnoreCase("Remove")) {
            click(removeToCartButton);
        } else {
            log.warn("'Remove' button is not visible.");
        }
    }
}