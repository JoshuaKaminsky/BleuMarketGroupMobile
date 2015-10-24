package com.mobile.android.client.utilities;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.mobile.android.common.model.ResultCallback;
import com.mobile.android.contract.IResultCallback;
import com.mobile.bmg.model.ApiError;
import com.squareup.okhttp.Response;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class HttpAsyncTask<TRequest, TResponse> extends AsyncTask<String, Void, TResponse> {
	private final Map<String, String> _headers;

	protected IResultCallback<TResponse> _callback;
	
	protected Callable<Response> _httpAction;

	protected Response response;

	protected ApiError apiError;

	public HttpAsyncTask(IResultCallback<TResponse> callback)
	{
		_callback = callback;
		_headers = new HashMap<>();
	}
	
	public HttpAsyncTask(String authorization, IResultCallback<TResponse> callback)
	{
		_callback = callback;
		_headers = new HashMap<>();

		addAuthHeader(authorization);
	}

	public void Get(final String url) {
		_httpAction = new Callable<Response>() {

			public Response call() throws Exception {
				return HttpClientUtilities.getJson(url, _headers);
			}
		};
		
		execute("");
	}
	
	public void Post(final TRequest postItem, final String url) {
		_httpAction = new Callable<Response>() {

			public Response call() throws Exception {
				String data = "";
				if(postItem != null) {
					data = JsonUtilities.getJson(postItem);
				}

				return HttpClientUtilities.postJson(url, data, _headers);
			}
		};
		
		execute("");
	}
	
	public void Delete(final String url) {
		_httpAction = new Callable<Response>() {

			public Response call() throws Exception {
				return HttpClientUtilities.deleteJson(url, _headers);
			}
		};

		execute("");
	}
	
	public void Put(final TRequest putItem, final String url) {
		_httpAction = new Callable<Response>() {

			public Response call() throws Exception {
				String data = "";
				if(putItem != null) {
					data = JsonUtilities.getJson(putItem);
				}
				return HttpClientUtilities.putJson(url, data, _headers);
			}
		};

		execute("");
	}
	
	@Override
	protected TResponse doInBackground(String... urls) {
		try {
			response = _httpAction.call();
		} catch (Exception e) {
			Log.e("Http Request", e.getMessage(), e);
		}



		String data = "";

		try {
			data = response.body().string();
		} catch (Exception e) {
			Log.e("Http Request", e.getMessage(), e);
		}

		if(response.code() != HttpURLConnection.HTTP_OK) {
			Log.e("Http Error:", data);

			try{
				apiError = JsonUtilities.parseJson(data, ApiError.class);
			} catch(Exception ex) {
				apiError = new ApiError();
				apiError.errors = new String[]{data};
			}
		}

		TResponse result = null;
		if (!StringUtilities.IsNullOrEmpty(data)) {
			Log.i(this.toString(), data);

			if(_callback != null) {
				result = JsonUtilities.parseJson(data, _callback.getType());
			}
		}

		return result;
	}
	
	@Override
	protected void onPostExecute(TResponse result) {
		super.onPostExecute(result);

		if(response == null) {
			return;
		}

		if(response.code() != HttpURLConnection.HTTP_OK) {

			if(_callback != null && _callback instanceof ResultCallback) {
				String error = "";

				if(apiError != null && apiError.errors != null && apiError.errors.length > 0) {
					error = apiError.errors[0];
				}

				((ResultCallback)_callback).error(error);
			}

			return;
		}

		if(_callback != null)
		{	
			_callback.call(result);
		}		
	}

	public void addAuthHeader(String authorization) {
		String base64EncodedCredentials = Base64.encodeToString(authorization.getBytes(), Base64.NO_WRAP);

		_headers.put("Authorization", base64EncodedCredentials);
	}

	public void addHeader(String key, String value) {
		_headers.put(key, value);
	}
}
