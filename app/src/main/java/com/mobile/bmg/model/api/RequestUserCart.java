package com.mobile.bmg.model.api;

/**
 * Created by Josh on 10/20/15.
 */
public class RequestUserCart extends BaseAuthorizedRequest{
    public RequestUserCart(String token) {
        super(token);
    }

    @Override
    public String getUrl() {
        return Urls.getUserCart();
    }
}
