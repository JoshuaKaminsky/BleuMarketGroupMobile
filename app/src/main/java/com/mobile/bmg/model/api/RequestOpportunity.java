package com.mobile.bmg.model.api;

import com.mobile.bmg.contract.IApiRequest;

/**
 * Created by Josh on 9/13/15.
 */
public class RequestOpportunity implements IApiRequest{

    public int id;

    public RequestOpportunity(int id) {
        this.id = id;
    }

    @Override
    public String getUrl() {
        return Urls.getOpportunity(id);
    }
}
