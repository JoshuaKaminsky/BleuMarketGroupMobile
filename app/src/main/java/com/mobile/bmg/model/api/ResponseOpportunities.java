package com.mobile.bmg.model.api;

import com.google.gson.annotations.SerializedName;
import com.mobile.bmg.model.Opportunity;

/**
 * Created by Josh on 9/13/15.
 */
public class ResponseOpportunities {

    @SerializedName("total_count")
    public int totalCount;

    public Opportunity[] opportunities;

}
