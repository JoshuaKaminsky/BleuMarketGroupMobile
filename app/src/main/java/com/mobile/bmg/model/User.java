package com.mobile.bmg.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Josh on 9/12/15.
 */
public class User {

    @SerializedName("email_address")
    public String emailAddress;

    public String password;

    @SerializedName("password_confirmation")
    public String passwordConfirmation;

    @SerializedName("first_name")
    public String firstName;

    @SerializedName("last_name")
    public String lastName;

    @SerializedName("address")
    public String streetAddress;

    @SerializedName("address2")
    transient public String streetAddressExtended;

    public String city;

    public String state;

    transient public String country;

    @SerializedName("postal_code")
    public String postalCode;

}
