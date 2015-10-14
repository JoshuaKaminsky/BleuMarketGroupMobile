package com.mobile.bmg.contract;

import com.squareup.okhttp.Request;

/**
 * Created by Josh on 9/13/15.
 */
public interface IApiTokenRequest extends IApiRequest {

    void addTokenHeader(Request.Builder request, String token);

}
