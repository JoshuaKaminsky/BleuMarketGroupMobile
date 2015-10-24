package com.mobile.bmg.model.api;

/**
 * Created by Josh on 10/20/15.
 */
public class RequestToggleUserLike extends BaseAuthorizedRequest{

    private boolean like;

    private int organizationId;

    public RequestToggleUserLike(boolean like, int organizationId, String token) {
        super(token);
        this.like = like;

        this.organizationId = organizationId;
    }

    public boolean getLike() { return like;}

    @Override
    public String getUrl() {
        if(like) {
            return Urls.addOrganizationLike(organizationId);
        } else {
            return Urls.removeOrganizationLike(organizationId);
        }
    }
}
