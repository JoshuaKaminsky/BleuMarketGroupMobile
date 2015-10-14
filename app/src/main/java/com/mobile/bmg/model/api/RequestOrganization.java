package com.mobile.bmg.model.api;

import com.mobile.bmg.contract.IApiRequest;

/**
 * Created by Josh on 9/13/15.
 */
public class RequestOrganization implements IApiRequest{

    public int id;

    public RequestOrganization(int id) {
        this.id = id;
    }

    @Override
    public String getUrl() {
        return Urls.getOrganization(id);
    }
}
