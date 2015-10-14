package com.mobile.bmg.model.api;

import com.mobile.bmg.model.User;

/**
 * Created by Josh on 9/13/15.
 */
public class RequestUserCreate extends BaseAuthorizedRequest {

    public User user;

    public RequestUserCreate(String token) {
        super(token);
    }

    @Override
    public String getUrl() {
        return Urls.createUser();
    }
}
