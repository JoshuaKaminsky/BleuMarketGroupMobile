package com.mobile.bmg.model.api;

import com.google.gson.annotations.SerializedName;
import com.mobile.bmg.contract.IApiRequest;

/**
 * Created by Josh on 9/13/15.
 */
public class RequestLogin implements IApiRequest {

    @SerializedName("email_address")
    public String emailAddress;

    public String password;

    public RequestLogin(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
    }

    @Override
    public String getUrl() {
        return null;
    }
}
