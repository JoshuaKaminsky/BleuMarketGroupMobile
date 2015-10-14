package com.mobile.bmg.model;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

/**
 * Created by Josh on 9/13/15.
 */
public class Opportunity {

    public int id;

    @SerializedName("vm_opportunity_id")
    public int opportunityId;

    @SerializedName("vm_organization_id")
    public int organizationId;

    @SerializedName("organization_name")
    public String name;

    public String title;

    public String description;

    public String city;

    public String state;

    public String country;

    public double latitude;

    public double longitude;

    @SerializedName("start_date")
    public String startDate;

    @SerializedName("start_time")
    public String startTime;

    @SerializedName("end_date")
    public String endDate;

    @SerializedName("end_time")
    public String endTime;

    @SerializedName("single_day_opportunity")
    public boolean singleDay;

    public boolean ongoing;

    public String image;

    public boolean virtual;

    @SerializedName("volunteer_url")
    public String url;

    @SerializedName("created_at")
    public DateTime created;

    @SerializedName("updated_at")
    public DateTime updated;

    public Category[] categories;

    @SerializedName("event_date")
    public String eventDate;
}
