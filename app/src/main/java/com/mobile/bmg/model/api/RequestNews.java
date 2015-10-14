package com.mobile.bmg.model.api;

import com.mobile.bmg.contract.IApiRequest;

/**
 * Created by Josh on 9/13/15.
 */
public class RequestNews implements IApiRequest {

    @Override
    public String getUrl() {
        return Urls.getNews();
    }
}
