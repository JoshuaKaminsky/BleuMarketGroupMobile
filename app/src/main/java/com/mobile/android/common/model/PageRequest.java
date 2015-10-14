package com.mobile.android.common.model;

import com.mobile.android.contract.IAsyncParamedAction;
import com.mobile.android.contract.IResultCallback;

/**
 * Created by Josh on 10/14/15.
 */
public class PageRequest<TResult> {

    private IAsyncParamedAction<TResult, Integer> request;

    public PageRequest(IAsyncParamedAction<TResult, Integer> request) {
        this.request = request;
    }

    public void Request(int page, IResultCallback<TResult> callback) {
        request.Execute(page, callback);
    }

}
