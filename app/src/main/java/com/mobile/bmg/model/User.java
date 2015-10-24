package com.mobile.bmg.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Josh on 9/12/15.
 */
public class User {

    public int id;

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

    public boolean isValid() {
        if(emailAddress == null || emailAddress.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            return false;
        }

        if(password == null || password.isEmpty() || password.length() < 8 || passwordConfirmation == null || passwordConfirmation.isEmpty() || !password.equalsIgnoreCase(passwordConfirmation)) {
            return false;
        }

        if(firstName == null || firstName.isEmpty()) {
            return false;
        }

        if(lastName == null || lastName.isEmpty()) {
            return false;
        }

        return true;
    }

}
