package io.github.earlbertmercado.selenium.pages;

import org.openqa.selenium.By;

public class InventoryPage extends BasePage {
    private final By inventoryTitle             = By.xpath("//span[@class='title']");
    private final By addToCartBackpackButton    = By.id("add-to-cart-sauce-labs-backpack");
    private final By shoppingCartLink           = By.className("shopping_cart_link");

    public String getInventoryTitleText() {
        return getText(inventoryTitle);
    }

    public InventoryPage addBackpackToCart() {
        click(addToCartBackpackButton);
        return this;
    }

//    public CartPage clickCart() {
//        click(shoppingCartLink);
//        return new CartPage();
//    }
}