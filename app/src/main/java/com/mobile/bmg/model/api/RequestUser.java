package com.mobile.bmg.model.api;

import com.mobile.bmg.contract.IApiRequest;

/**
 * Created by Josh on 9/13/15.
 */
public class RequestUser implements IApiRequest {

    public int id;

    public RequestUser(int id) {
        this.id = id;
    }

    @Override
    public String getUrl() {
        return Urls.getUser(this.id);
    }
}
