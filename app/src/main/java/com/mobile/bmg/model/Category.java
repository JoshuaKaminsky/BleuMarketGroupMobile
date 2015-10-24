package com.mobile.bmg.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Josh on 9/13/15.
 */
public class Category {

    public int id;

    public String name;

    @SerializedName("taggings_count")
    public int count;

    @Override
    public String toString() {
        return this.name;
    }
}
