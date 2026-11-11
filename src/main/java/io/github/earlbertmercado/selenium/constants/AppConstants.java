package io.github.earlbertmercado.selenium.constants;

import io.github.earlbertmercado.selenium.utils.ConfigReader;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class AppConstants {

    public static final String BASE_URL = ConfigReader.get("base_url");
    public static final String INVENTORY_PAGE_URL = BASE_URL + "/inventory.html";
    public static final String ITEM_DETAIL_PAGE_URL = BASE_URL + "/inventory-item.html?id=";
}
