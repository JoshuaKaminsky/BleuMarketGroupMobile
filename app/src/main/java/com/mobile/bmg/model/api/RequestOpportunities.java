package com.mobile.bmg.model.api;

import com.mobile.bmg.contract.IApiRequest;

/**
 * Created by Josh on 9/13/15.
 */
public class RequestOpportunities implements IApiRequest {

    public String category;

    public int page;

    public RequestOpportunities(){ this("", 1); }

    public RequestOpportunities(int page) { this("", page); }

    public RequestOpportunities(String category, int page) { this.category = category; this.page = page; }

    @Override
    public String getUrl() {
        return Urls.getOpportunitiesByCategory(category, 20, page);
    }
}
