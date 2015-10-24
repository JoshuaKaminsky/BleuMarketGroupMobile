package com.mobile.bmg.model.api;

import com.mobile.bmg.contract.IApiTokenRequest;
import com.squareup.okhttp.Request;

/**
 * Created by Josh on 9/12/15.
 */
public abstract class BaseAuthorizedRequest implements IApiTokenRequest {

    public transient String token;

    public BaseAuthorizedRequest(String token) {
        this.token = token;
    }

    @Override
    public void addTokenHeader(Request.Builder request, String token) {
        request.addHeader("Token", token);
    }

    @Override
    public abstract String getUrl();
}
