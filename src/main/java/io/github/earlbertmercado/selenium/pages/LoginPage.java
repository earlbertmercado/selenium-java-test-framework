package io.github.earlbertmercado.selenium.pages;

import org.openqa.selenium.By;

public class LoginPage extends BasePage {
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton   = By.id("login-button");
    private final By errorMessage = By.cssSelector(".error-message-container");

    public LoginPage enterUsername(String username) {
        sendKeys(usernameField, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        sendKeys(passwordField, password);
        return this;
    }

    public InventoryPage clickLogin() {
        click(loginButton);
        return new InventoryPage();
    }

    public InventoryPage login(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }

    public boolean isUsernameInputFieldVisible() {
        return isVisible(usernameField);
    }

    public boolean isPasswordInputFieldVisible() {
        return isVisible(passwordField);
    }

    public boolean isLoginButtonVisible() {
        return isVisible(loginButton);
    }

    public boolean isErrorMessageVisible() {
        return isVisible(errorMessage);
    }
}