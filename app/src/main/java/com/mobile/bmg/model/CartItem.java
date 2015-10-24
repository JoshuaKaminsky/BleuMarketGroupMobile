package com.mobile.bmg.model;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

/**
 * Created by Josh on 10/20/15.
 */
public class CartItem {

    public int id;

    @SerializedName("user_id")
    public int userId;

    @SerializedName("organization_id")
    public int organizationId;

    public String title;

    public double price;

    @SerializedName("format_price")
    public String formattedPrice;

    public String memo;

    @SerializedName("memorial_gift")
    public String gift;

    @SerializedName("monthly_recurring")
    public String recurring;

    @SerializedName("monthly_recurring_day")
    public String recurringDay;

    @SerializedName("created_at")
    public DateTime createdAt;

    @SerializedName("updated_at")
    public DateTime updatedAt;

}
