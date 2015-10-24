package com.mobile.bmg.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Josh on 10/20/15.
 */
public class RequestCheckout extends BaseAuthorizedRequest {

    @SerializedName("card_number")
    private String cardNumber;

    @SerializedName("expiration_month")
    private String expirationMonth;

    @SerializedName("expiration_year")
    private String expirationYear;

    public RequestCheckout(String cardNumber, int expirationMonth, int expirationYear, String token) {
        super(token);
        this.cardNumber = cardNumber;
        this.expirationMonth = Integer.toString(expirationMonth);
        this.expirationYear = Integer.toString(expirationYear);
    }

    @Override
    public String getUrl() {
        return Urls.chechout();
    }
}
