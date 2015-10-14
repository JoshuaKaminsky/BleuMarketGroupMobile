package com.mobile.bmg.model;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

/**
 * Created by Josh on 9/12/15.
 */
public class Organization {

    public int id;

    @SerializedName("company_name")
    public String name;

    @SerializedName("doing_business_as")
    public String operatingAs;

    @SerializedName("website_status")
    public int websiteStatus;

    @SerializedName("website_url")
    public String websiteUrl;

    @SerializedName("federal_tax_id_number")
    public String federalTaxId;

    @SerializedName("organization_type")
    public String type;

    @SerializedName("organization_description")
    public String description;

    @SerializedName("address")
    public String streetAddress;

    @SerializedName("address2")
    public String streetAddressExtended;

    public String city;

    public String state;

    public String country;

    @SerializedName("postal_code")
    public String postalCode;

    @SerializedName("mailing_address")
    public String mailingStreetAddress;

    @SerializedName("mailing_address2")
    public String mailingStreetAddressExtended;

    @SerializedName("mailing_city")
    public String mailingCity;

    @SerializedName("mailing_state")
    public String mailingState;

    @SerializedName("mailing_country")
    public String mailingCountry;

    @SerializedName("mailing_postal_code")
    public String mailingPostalCode;

    @SerializedName("mailing_timezone")
    public String mailingTimeZone;

    @SerializedName("contact_first_name")
    public String contactFirstName;

    @SerializedName("contact_last_name")
    public String contactLastName;

    @SerializedName("contact_phone_number")
    public String contactPhoneNumber;

    @SerializedName("contact_email_address")
    public String contactEmailAddress;

    @SerializedName("year_founded")
    public int yearFounded;

    @SerializedName("incorporated_year")
    public int yearIncorporated;

    public String keywords;

    @SerializedName("mission_statement")
    public String missionStatement;

    @SerializedName("impact_statement")
    public String impactStatement;

    @SerializedName("intended_user")
    public String intendedUser;

    @SerializedName("referred_by")
    public String referredBy;

    @SerializedName("created_at")
    public DateTime created;

    @SerializedName("updated_at")
    public DateTime updated;

    public double latitude;

    public double longitude;

    @SerializedName("use_guidestar_data")
    public boolean useGuidestarData;

    @SerializedName("primary_logo")
    public String primaryLogo;

    @SerializedName("tags_list")
    public String[] tags;

    public int status;

    public boolean featured;
}
