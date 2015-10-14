package com.mobile.android.contract;

public interface IAsyncParamedAction<TResult, TParam> {
	
	void Execute(TParam parameter, IResultCallback<TResult> callback);

}

