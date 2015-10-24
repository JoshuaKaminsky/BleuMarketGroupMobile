package com.mobile.bmg;

import android.test.InstrumentationTestCase;

import com.mobile.android.contract.IResultCallback;
import com.mobile.bmg.common.BmgApiClient;
import com.mobile.bmg.model.api.RequestOrganizations;
import com.mobile.bmg.model.api.ResponseOrganizations;


import java.util.concurrent.CountDownLatch;

/**
 * Created by Josh on 9/13/15.
 */
public class BmgApiClientTest extends InstrumentationTestCase {
    public void test() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        RequestOrganizations request = new RequestOrganizations();

        final ResponseOrganizations response = new ResponseOrganizations();

        IResultCallback<ResponseOrganizations> callback = new IResultCallback<ResponseOrganizations>() {
            @Override
            public Class<ResponseOrganizations> getType() {
                return ResponseOrganizations.class;
            }

            @Override
            public void call(ResponseOrganizations result) {
                response.organizations = result.organizations;
                response.totalCount = result.totalCount;

                signal.countDown();
            }
        };

        BmgApiClient.getOrganizations(request, callback);

        signal.await();

        assertTrue(response != null);
        assertTrue(response.organizations != null);
        assertTrue(response.totalCount > 0);
    }
}
