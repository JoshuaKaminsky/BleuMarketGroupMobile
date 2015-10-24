package com.mobile.bmg.model.api;

/**
 * Created by Josh on 9/13/15.
 */
public class RequestUser extends BaseAuthorizedRequest {

    public int id;

    public RequestUser(String token) {
        super(token);
    }

    public RequestUser(int id, String token) {
        super(token);

        this.id = id;
    }

    @Override
    public String getUrl() {
        if(id == 0) {
            return Urls.getLoggedInUser();
        }
        return Urls.getUser(this.id);
    }
}
