package com.mobile.bmg.model.api;

import com.google.gson.annotations.SerializedName;
import com.mobile.bmg.contract.IApiRequest;

/**
 * Created by Josh on 9/13/15.
 */
public class RequestLogout implements IApiRequest {

    @SerializedName("access_token")
    public String token;

    public RequestLogout(String token) {
        this.token = token;
    }

    @Override
    public String getUrl() {
        return Urls.getLogout();
    }
}
