package io.github.earlbertmercado.selenium.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.github.earlbertmercado.selenium.utils.LocatorRepository;

public class InventoryPage extends BasePage {

    // private final By inventoryTitle         = By.className("title");
    // private final By burgerMenu             = By.id("react-burger-menu-btn");
    // private final By shoppingCart           = By.className("shopping_cart_link");
    // private final By sortDropDown           = By.className("product_sort_container");
    // private final By items                  = By.cssSelector(".inventory_item");
    // private final By itemNames              = By.cssSelector(".inventory_item_name");
    // private final By itemDescriptions       = By.cssSelector(".inventory_item_desc");
    // private final By itemPrices             = By.cssSelector(".inventory_item_price");
    // private final By itemImages             = By.cssSelector(".inventory_item_img img");
    // private final By addToCartButtons       = By.cssSelector(".btn_inventory");
    // private final By removeToCartButtons    = By.cssSelector(".btn_inventory");
    // private final By shoppingCartBadge      = By.className("shopping_cart_badge");

    private final By inventoryTitle         = LocatorRepository.get("inventory.title");
    private final By burgerMenu             = LocatorRepository.get("inventory.burger_menu");
    private final By shoppingCart           = LocatorRepository.get("inventory.shopping_cart");
    private final By sortDropDown           = LocatorRepository.get("inventory.sort_dropdown");
    private final By items                  = LocatorRepository.get("inventory.all_items");
    private final By itemNames              = LocatorRepository.get("inventory.all_item_names");
    private final By itemDescriptions       = LocatorRepository.get("inventory.all_item_description");
    private final By itemPrices             = LocatorRepository.get("inventory.all_item_price");
    private final By itemImages             = LocatorRepository.get("inventory.all_item_image");
    private final By addToCartButtons       = LocatorRepository.get("inventory.all_add_to_cart_buttons");
    private final By removeToCartButtons    = LocatorRepository.get("inventory.all_remove_to_cart_buttons");
    private final By shoppingCartBadge      = LocatorRepository.get("inventory.cart_badge");

    // ------------------ Getter Methods ------------------

    public String getInventoryTitleText() {
        return getText(inventoryTitle);
    }

    public int getItemCount() {
        return getElementCount(items);
    }

    public String getItemName(int index) {
        return getElementByIndex(itemNames, index).getText().trim();
    }

    public String getItemDescription(int index) {
        return getElementByIndex(itemDescriptions, index).getText().trim();
    }

    public String getItemPrice(int index) {
        String priceText = getElementByIndex(itemPrices, index).getText().trim();
        return priceText.replace("$", "");
    }

    public String getItemImageSrc(int index) {
        return getElementByIndex(itemImages, index).getAttribute("src");
    }

    public List<String> getItemNames() {
        return getTexts(itemNames);
    }

    public List<Double> getItemPrices() {
        return getDoubleValues(itemPrices);
    }

    public int getCartItemCount() {
        if (isVisible(shoppingCartBadge)) {
            String badgeText = getText(shoppingCartBadge);
            try {
                return Integer.parseInt(badgeText);
            } catch (NumberFormatException e) {
                log.error("Failed to parse cart item count from badge text: '{}'", badgeText, e);
                return 0;
            }
        } else {
            return 0;
        }
    }

    public List<ItemInfo> getAllItemInfo() {
        List<ItemInfo> products = new ArrayList<>();
        List<WebElement> productItems = waitForElements(items);

        if (productItems.isEmpty()) {
            log.warn("No inventory items were visible when collecting item info.");
            return products;
        }

        for (WebElement product : productItems) {
            String name = product.findElement(itemNames).getText().trim();
            String description = product.findElement(itemDescriptions).getText().trim();
            String price = product.findElement(itemPrices).getText().replace("$", "").trim();
            String imageSrc = product.findElement(itemImages).getAttribute("src");

            products.add(new ItemInfo(name, description, price, imageSrc));
        }

        return products;
    }

    // ------------------ Visibility Methods ------------------

    public boolean isBurgerMenuVisible() {
        return isVisible(burgerMenu);
    }

    public boolean isShoppingCartLinkVisible() {
        return isVisible(shoppingCart);
    }

    public boolean isSortDropDownVisible() {
        return isVisible(sortDropDown);
    }

    public boolean areItemsVisible() {
        return getItemCount() > 0;
    }

    public boolean areAddToCartButtonsVisible() {
        return getElementCount(addToCartButtons) > 0
                && getElementCount(addToCartButtons) == getItemCount();
    }

    // ------------------ Action Methods ------------------

    public void addItemToCartByIndex(int index) {
        String itemName = getItemName(index);
        log.info("Adding item to cart: '{}' (Index: {})", itemName, index);

        if ("Add to cart".equals(getElementByIndex(addToCartButtons, index).getText().trim())) {
            getElementByIndex(addToCartButtons, index).click();
            log.info("Item '{}' added to cart successfully.", itemName);
        } else {
            log.warn("Item '{}' is already in the cart or button state is incorrect.", itemName);
        }
    }

    public void removeItemFromCartByIndex(int index) {
        log.info("Removing item from cart at index: {}", index);
        if ("Remove".equals(getElementByIndex(removeToCartButtons, index).getText().trim())) {
            getElementByIndex(removeToCartButtons, index).click();
        }
    }

    public void addItemsToCart(int... indices) {
        log.info("Adding multiple items to cart.");
        for (int index : indices) {
            log.debug("Adding item at index {}", index);
            addItemToCartByIndex(index);
        }
    }

    public void removeItemsFromCart(int... indices) {
        log.info("Removing multiple items from cart.");
        for (int index : indices) {
            log.debug("Removing item at index {}", index);
            removeItemFromCartByIndex(index);
        }
    }

    public void addThenRemoveItems(int... indices) {
        addItemsToCart(indices);
        removeItemsFromCart(indices);
    }

    public void sortByNameAsc() {
        log.info("Sorting products by Name (A to Z)");
        selectByValue(sortDropDown, "az");
    }

    public void sortByNameDesc() {
        log.info("Sorting products by Name (Z to A)");
        selectByValue(sortDropDown, "za");
    }

    public void sortByPriceAsc() {
        log.info("Sorting products by Price (Low to High)");
        selectByValue(sortDropDown, "lohi");
    }

    public void sortByPriceDesc() {
        log.info("Sorting products by Price (High to Low)");
        selectByValue(sortDropDown, "hilo");
    }

    // ------------------ Action Methods ------------------

    public ItemDetailPage clickSpecificItem(By itemLocator) {
        click(itemLocator);
        return new ItemDetailPage();
    }

    public ItemDetailPage clickItemNameByIndex(int index) {
        getElementByIndex(itemNames, index).click();
        return new ItemDetailPage();
    }

    public ItemDetailPage clickItemImageByIndex(int index) {
        getElementByIndex(itemImages, index).click();
        return new ItemDetailPage();
    }

    public CartPage clickShoppingCart() {
        click(shoppingCart);
        return new CartPage();
    }

    // ------------------ Validation / Sorting Verification Methods ------------------

    public boolean isSortedAlphabeticallyAsc() {
        List<String> names = getItemNames();
        List<String> sortedNames = names.stream().sorted().toList();
        return names.equals(sortedNames);
    }

    public boolean isSortedAlphabeticallyDesc() {
        List<String> names = getItemNames();
        List<String> sortedNames = names.stream().sorted((a, b) -> b.compareTo(a)).toList();
        return names.equals(sortedNames);
    }

    public boolean isSortedByPriceAsc() {
        List<Double> prices = getItemPrices();
        List<Double> sortedPrices = prices.stream().sorted().toList();
        return prices.equals(sortedPrices);
    }

    public boolean isSortedByPriceDesc() {
        List<Double> prices = getItemPrices();
        List<Double> sortedPrices = prices.stream().sorted((a, b) -> Double.compare(b, a)).toList();
        return prices.equals(sortedPrices);
    }

    public boolean areAllItemsValid() {
        log.info("Performing bulk validation of all displayed items.");
        List<ItemInfo> products = getAllItemInfo();
        return !products.isEmpty() && products.stream().allMatch(info -> {
            boolean valid = info.isValid();
            if (!valid) log.error("Item validation failed for: {}", info.name());
            return valid;
        });
    }

    // ------------------ DTO / Record ------------------

    public record ItemInfo(String name, String description, String price, String imageSrc) {

        public boolean isValid() {
            return isNotBlank(name)
                    && isNotBlank(description)
                    && isValidPrice(price)
                    && isNotBlank(imageSrc);
        }

        private static boolean isNotBlank(String v) {
            return v != null && !v.isBlank();
        }

        private static boolean isValidPrice(String v) {
            try {
                Double.valueOf(v.trim());
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }
}