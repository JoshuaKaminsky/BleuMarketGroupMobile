package com.mobile.bmg.model.api;

import com.google.gson.annotations.SerializedName;
import com.mobile.bmg.model.Organization;

/**
 * Created by Josh on 9/13/15.
 */
public class ResponseOrganizations {

    @SerializedName("total_count")
    public int totalCount;

    public Organization[] organizations;

}
