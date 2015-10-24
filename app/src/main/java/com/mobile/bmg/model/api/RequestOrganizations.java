package com.mobile.bmg.model.api;

import com.mobile.bmg.contract.IApiRequest;
import com.mobile.bmg.model.Tag;

/**
 * Created by Josh on 9/13/15.
 */
public class RequestOrganizations implements IApiRequest {

    public Tag tag;

    public String search;

    public int distance;

    public int page;

    public RequestOrganizations(){ this("", -1, 1); }

    public RequestOrganizations(int page) { this("", -1, page); }

    public RequestOrganizations(String search, int distance, int page) { this.search = search; this.distance = distance; this.page = page; }

    @Override
    public String getUrl() {
        if(tag != null) {
            return Urls.getOrganizationByTag(tag.name, page);
        }

        return Urls.getOrganizationsByLocationDistanceAndPage(search, distance, page);
    }
}
