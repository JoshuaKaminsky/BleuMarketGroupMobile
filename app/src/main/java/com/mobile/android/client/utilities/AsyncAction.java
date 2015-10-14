package com.mobile.android.client.utilities;

import com.mobile.android.contract.IAction;
import com.mobile.android.contract.IResultCallback;

import android.os.AsyncTask;

public class AsyncAction<T> extends AsyncTask<Void, Void, T>{

	IAction<T> _asyncAction;
	IResultCallback<T> _callback;
	
	public AsyncAction(IAction<T> action, IResultCallback<T> callback){
		_asyncAction = action;
		_callback = callback;
	}
	
	@Override
	protected T doInBackground(Void... params) {
		if(_asyncAction == null)
			return null;
		
		return _asyncAction.Execute(); 
	}		
	
	protected void onPostExecute(T result) {
		if(_callback == null)
			return;
		
		_callback.call(result);
	};
}
