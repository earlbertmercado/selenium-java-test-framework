package io.github.earlbertmercado.selenium.pages;

import io.github.earlbertmercado.selenium.constants.AppConstants;

public class ItemDetailPage extends BasePage {

    public boolean isRedirectedToItemDetailPage() {
        String currentUrl = getCurrentUrl();
        return currentUrl.contains(AppConstants.ITEM_DETAIL_PAGE_URL);
    }

}
