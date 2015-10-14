package com.mobile.bmg.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Josh on 9/13/15.
 */
public class ResponseOrganizationTotals {

    @SerializedName("donation_average")
    public double averageDonation;

    @SerializedName("donation_amount")
    public double donationAmount;

    @SerializedName("donation_count")
    public double count;
}
