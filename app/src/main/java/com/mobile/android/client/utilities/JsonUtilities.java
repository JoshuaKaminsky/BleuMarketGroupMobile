package com.mobile.android.client.utilities;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtilities{

	private static Gson getGson() {
		return Converters.registerDateTime(new GsonBuilder()).create();
	}

	public static <T> T parseJson(InputStream inputStream, Class<T> type) {
		if(inputStream == null)
			return null;		
		
		return getGson().fromJson(new InputStreamReader(inputStream), type);
	}
	
	public static <T> T parseJson(String input, Class<T> type)
	{
		if(input == null)
			return null;
		
		Reader reader = new StringReader(input);
		
		return getGson().fromJson(reader, type);
	}
	
	public static <T extends Object> String getJson(T input)  {
		if(input == null)
			return "";
		
		return getGson().toJson(input);
	}
}
