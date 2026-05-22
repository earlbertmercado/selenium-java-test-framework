package io.github.earlbertmercado.selenium.constants;

import io.github.earlbertmercado.selenium.utils.ConfigReader;

public final class AppConstants {

    private AppConstants() {}

    public static final String BASE_URL = ConfigReader.get("base_url");
    public static final String INVENTORY_PAGE_URL = BASE_URL + "/inventory.html";
}
