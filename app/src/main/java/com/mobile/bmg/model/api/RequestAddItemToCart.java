package com.mobile.bmg.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Josh on 10/20/15.
 */
public class RequestAddItemToCart extends BaseAuthorizedRequest{

    @SerializedName("organization_id")
    private int organizationId;

    private double price;

    private String title;

    public RequestAddItemToCart(int organizationId, double price, String token) {
        super(token);
        this.organizationId = organizationId;
        this.price = price;
        this.title = "Donation";
    }

    @Override
    public String getUrl() {
        return Urls.addToUserCart();
    }
}
