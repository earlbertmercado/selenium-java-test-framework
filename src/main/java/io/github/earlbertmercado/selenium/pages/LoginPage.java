package io.github.earlbertmercado.selenium.pages;

import org.openqa.selenium.By;

import io.github.earlbertmercado.selenium.utils.LocatorRepository;

public class LoginPage extends BasePage {

    private final By usernameField  = LocatorRepository.get("login.username");
    private final By passwordField  = LocatorRepository.get("login.password");
    private final By loginButton    = LocatorRepository.get("login.button");
    private final By errorMessage   = LocatorRepository.get("login.error_message");

    // --- Action Methods ---

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

    // --- Verification Methods ---

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