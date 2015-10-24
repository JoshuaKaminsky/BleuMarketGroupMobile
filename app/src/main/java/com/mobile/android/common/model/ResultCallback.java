package com.mobile.android.common.model;

import com.mobile.android.contract.IParamAction;
import com.mobile.android.contract.IResultCallback;

/**
 * Created by Josh on 10/20/15.
 */
public class ResultCallback<TResult extends Object> implements IResultCallback<TResult> {

    private IParamAction<Void, TResult> success;
    private IParamAction<Void, String> error;
    private Class<TResult> type;

    public ResultCallback(IParamAction<Void, TResult> success, IParamAction<Void, String> error, Class<TResult> type) {
        this.success = success;

        this.error = error;

        this.type = type;
    }

    @Override
    public Class<TResult> getType() {
        return this.type;
    }

    @Override
    public void call(TResult result) {
        this.success.Execute(result);
    }

    public void error(String message) {
        this.error.Execute(message);
    }
}
