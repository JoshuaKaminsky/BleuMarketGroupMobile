package com.mobile.bmg.model.api;

/**
 * Created by Josh on 10/20/15.
 */
public class RequestDeleteCartItem extends BaseAuthorizedRequest {

    private int itemId;

    public RequestDeleteCartItem(int itemId, String token) {
        super(token);
        this.itemId = itemId;
    }

    @Override
    public String getUrl() {
        return Urls.removeFromUserCart(itemId);
    }
}
