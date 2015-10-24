package com.mobile.bmg.common;

import com.mobile.bmg.model.api.BaseAuthorizedRequest;
import com.mobile.bmg.model.api.Urls;

/**
 * Created by Josh on 10/20/15.
 */
public class RequestDeleteCart extends BaseAuthorizedRequest{

    public RequestDeleteCart(String token) {
        super(token);
    }

    @Override
    public String getUrl() {
        return Urls.deleteUserCart();
    }
}
