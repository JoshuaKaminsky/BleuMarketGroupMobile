package com.mobile.android.client.utilities;

import java.io.InputStream;
import java.util.concurrent.Callable;

import com.mobile.android.contract.IResultCallback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.os.AsyncTask;
import android.util.Log;

public class HttpAsyncTask<TRequest, TResponse> extends AsyncTask<String, Void, TResponse> {
	private final String _authorization;	
	private final boolean _useAuthorization;
	
	protected IResultCallback<TResponse> _callback;
	
	protected Callable<Response> _httpAction;
	
	public HttpAsyncTask(IResultCallback<TResponse> callback)
	{
		_authorization = "";		
		_callback = callback;
		
		_useAuthorization = false;
	}	
	
	public HttpAsyncTask(String authorization, IResultCallback<TResponse> callback)
	{
		_authorization = authorization;		
		_callback = callback;
		
		_useAuthorization = true;
	}	

	public void Execute(final Request request) {
		_httpAction = new Callable<Response>() {

			public Response call() throws Exception {
				return HttpClientUtilities.httpRequest(request);
			}			
		};
		
		execute("");
	}
	
	public void Get(final String url) {
		_httpAction = new Callable<Response>() {

			public Response call() throws Exception {
				if (_useAuthorization) {
					return HttpClientUtilities.getAuthorizedJson(url, _authorization);
				}
				else {			
					return HttpClientUtilities.getJson(url);
				}	
			}			
		};
		
		execute("");
	}
	
	public void Post(final TRequest postItem, final String url) {
		_httpAction = new Callable<Response>() {

			public Response call() throws Exception {
				String jsonObject = JsonUtilities.getJson(postItem);
				
				if (_useAuthorization) {
					return HttpClientUtilities.postAuthorizedJson(url, _authorization, jsonObject);
				}
				else {
					return HttpClientUtilities.postJson(url, jsonObject);		
				}	
			}			
		};
		
		execute("");
	}
	
	public void Delete(final String url) {
		_httpAction = new Callable<Response>() {

			public Response call() throws Exception {
				if (_useAuthorization) {
					return HttpClientUtilities.deleteAuthorizedJson(url, _authorization);		
				}
				else {
					return HttpClientUtilities.deleteJson(url);	
				}
						
			}			
		};

		execute("");
	}
	
	public void Put(final TRequest postItem, final String url) {
		_httpAction = new Callable<Response>() {

			public Response call() throws Exception {
				String jsonObject = JsonUtilities.getJson(postItem);
				if (_useAuthorization) {
					return HttpClientUtilities.putAuthorizedJson(url, _authorization, jsonObject);					
				}
				else {
					return HttpClientUtilities.putJson(url, jsonObject);	
				}
					
			}			
		};

		execute("");
	}
	
	@Override
	protected TResponse doInBackground(String... urls) {
		String response = getDataString();
		
		TResponse result = null;
		
		if (response != null) {
			Log.i(this.toString(), response);
			
			if(_callback != null) {
				result = JsonUtilities.parseJson(response, _callback.GetType());
			}
		}
		
		return result;
	}
	
	@Override
	protected void onPostExecute(TResponse result) {
		super.onPostExecute(result);
		
		if(_callback != null)
		{	
			_callback.call(result);
		}		
	}
	
	public InputStream getDataStream() {
		try {			
			return _httpAction.call().body().byteStream();
		} catch (Exception e) {
			Log.e("Http Request", e.getMessage());
		}
		
		return null;
	}
	
	public String getDataString() {
		try {			
			return _httpAction.call().body().string();
		} catch (Exception e) {
			Log.e("Http Request", e.getLocalizedMessage());
		}
		
		return null;
	}
}
