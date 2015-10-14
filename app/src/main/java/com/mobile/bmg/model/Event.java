package com.mobile.bmg.model;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

/**
 * Created by Josh on 9/13/15.
 */
public class Event {

    public String title;

    public String description;

    public String link;

    @SerializedName("published_date")
    public DateTime published;

}
