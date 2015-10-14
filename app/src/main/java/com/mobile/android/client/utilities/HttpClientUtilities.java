package com.mobile.android.client.utilities;

import java.io.IOException;
import java.net.HttpURLConnection;

import android.util.Base64;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

public class HttpClientUtilities {

	public static final MediaType JsonMediaType = MediaType.parse("application/json; charset=utf-8");

	public static Response get(String url) {
		Log.i("HTTPClientUtilities :: Get", url);

		Request request = new Request.Builder().url(url).build();

		return httpRequest(request);
	}	
	
	public static Response getJson(String url) {
		Log.i("HTTPClientUtilities :: Get", url);

		Request.Builder getRequest = new Request.Builder()
				.url(url)
				.addHeader("Accept", "application/json");

		return httpRequest(getRequest.build());
	}	

	public static Response getAuthorizedJson(String url, String authorization) {
		Log.i("HTTPClientUtilities :: GET", url);

		Request.Builder request = new Request.Builder()
				.url(url)
				.addHeader("Accept", "application/json");

		setBasicAuthorizationHeader(request, authorization);
		
		return httpRequest(request.build());
	}
	
	public static Response postJson(String url, String jsonObject) {
		Log.i("HTTPClientUtilities :: POST", url);
		
		if(jsonObject != null) {
			Log.i("HTTPClientUtilities :: POST", jsonObject);
		}

		Request.Builder request = getPostRequest(url, jsonObject);

		if (request == null) return null;
		
		return httpRequest(request.build());
	}
	
	public static Response postAuthorizedJson(String url, String authorization, String jsonObject) {
		Log.i("HTTPClientUtilities :: POST", url);

		if(jsonObject != null) {
			Log.i("HTTPClientUtilities :: POST", jsonObject);
		}
		
		Request.Builder request = getPostRequest(url, jsonObject);

		if (request == null) return null;

		setBasicAuthorizationHeader(request, authorization);
		
		return httpRequest(request.build());
	}
	
	public static Response putJson(String url, String jsonObject) {
		Log.i("HTTPClientUtilities :: PUT", url);

		if(jsonObject != null) {
			Log.i("HTTPClientUtilities :: PUT", jsonObject);
		}
		
		Request.Builder request = getPutRequest(url, jsonObject);

		if (request == null) return null;
		
		return httpRequest(request.build());
	}
	
	public static Response putAuthorizedJson(String url, String authorization, String jsonObject) {
		Log.i("HTTPClientUtilities :: PUT", url);

		if(jsonObject != null) {
			Log.i("HTTPClientUtilities :: PUT", jsonObject);
		}
		
		Request.Builder request = getPutRequest(url, jsonObject);

		if (request == null) return null;

		setBasicAuthorizationHeader(request, authorization);
		
		return httpRequest(request.build());
	}
	
	public static Response deleteJson(String url) {
		Log.i("HTTPClientUtilities :: Delete", url);
		
		return httpRequest(new Request.Builder().url(url).delete().build());
	}

	public static Response deleteAuthorizedJson(String url, String authorization) {
		Log.i("HTTPClientUtilities :: Delete", url);
		
		Request.Builder deleteRequest = new Request.Builder().url(url);

		setBasicAuthorizationHeader(deleteRequest, authorization);
		
		return httpRequest(deleteRequest.build());
	}
	
	public static void addHeader(Request.Builder request, String header, String value) {
		request.addHeader(header, value);
	}
	
	public static Response httpRequest(Request request) {
		OkHttpClient client = new OkHttpClient();
		
		try
		{
			Response response = client.newCall(request).execute();

			final int statusCode = response.code();
			
			if(statusCode != HttpURLConnection.HTTP_OK)
			{
				String content = "";
				if(response != null) {
					ResponseBody entity = response.body();
					if(entity != null) {
						Log.w("HttpClientUtilities.RetrieveStream(String url)",	"Error " + statusCode + " for URL " + request.urlString() + " : " + entity.string());
						return null;
					}
				}
				
				Log.w("HttpClientUtilities.RetrieveStream(String url)", "Error " + statusCode + " for URL " + request.urlString());
				return null;					
			}
			
			return response;
		}
		catch (IOException e) 
		{
			client.cancel(request.tag());
			Log.w("HttpClientUtilities.RetrieveStream(String url)",	"Error for URL " + request.urlString(), e);
		}
		
		return null;
	}
	
	private static Request.Builder getPostRequest(String url, String jsonObject) {
		Request.Builder postRequest = new Request.Builder().url(url);

		try {
			postRequest.addHeader("Accept", "application/json");
            postRequest.addHeader("Content-Type", "application/json");
			postRequest.post(RequestBody.create(JsonMediaType, jsonObject));
		}
		catch(Exception ex) {
			Log.e("Could not set post body " + jsonObject, ex.getMessage());
			return null;
		}
		
		return postRequest;
	}
	
	private static Request.Builder getPutRequest(String url, String jsonObject) {
		Request.Builder putRequest = new Request.Builder().url(url);
		
		try {
			putRequest.addHeader("Accept", "application/json");
            putRequest.addHeader("Content-Type", "application/json");
            putRequest.put(RequestBody.create(JsonMediaType, jsonObject));
		}
		catch(Exception ex) {
			Log.e("Could not set put body " + jsonObject, ex.getMessage());
			return null;
		}
		
		return putRequest;
	}
	
	private static void setBasicAuthorizationHeader(Request.Builder request, String credentials) {
		String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

		request.addHeader("Authorization", "Basic " + base64EncodedCredentials);
	}
}
