package com.mobile.bmg.model.api;

/**
 * Created by Josh on 10/20/15.
 */
public class RequestUserLikes extends BaseAuthorizedRequest {

    public int userId;

    public RequestUserLikes(int userId, String token) {
        super(token);

        this.userId = userId;
    }

    @Override
    public String getUrl() {
        return Urls.getUserLikes(userId);
    }
}
