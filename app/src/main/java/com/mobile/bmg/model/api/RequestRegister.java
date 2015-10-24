package com.mobile.bmg.model.api;

import com.mobile.bmg.contract.IApiRequest;
import com.mobile.bmg.model.User;

/**
 * Created by Josh on 10/19/15.
 */
public class RequestRegister implements IApiRequest {

    public User user;

    public RequestRegister(User user) {

        this.user = user;
    }

    @Override
    public String getUrl() {
        return Urls.createUser();
    }
}
