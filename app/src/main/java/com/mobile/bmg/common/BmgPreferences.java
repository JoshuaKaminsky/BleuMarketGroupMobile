package com.mobile.bmg.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mobile.android.client.utilities.JsonUtilities;
import com.mobile.bmg.model.BrowsePreferences;
import com.mobile.bmg.model.User;

/**
 * Created by Josh on 10/17/15.
 */
public class BmgPreferences {

    private static final String preferenceName = " BmgPreferences";

    private static final String browsePreferencesKey = "browsePreferences";

    private static final String tokenPreferencesKey = "tokenPreference";

    private static final String userPreferenceKey = "userPreference";

    private final SharedPreferences sharedPreferences;

    public BmgPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(preferenceName, 0);
    }

    public void setBrowsePreferences(BrowsePreferences preferences) {
        setPreference(browsePreferencesKey, preferences);
    }

    public BrowsePreferences getBrowsePreferences() {
        return getPreference(browsePreferencesKey, BrowsePreferences.class);
    }

    public void setUserToken(String token) {
        setPreference(tokenPreferencesKey, token);
    }

    public String getUserToken() {
        return getPreference(tokenPreferencesKey);
    }

    public void setUser(User user) {
        setPreference(userPreferenceKey, user);
    }

    public User getUser() {
        return getPreference(userPreferenceKey, User.class);
    }

    private <T> void setPreference(String key, T item) {
        setPreference(key, JsonUtilities.getJson(item));
    }

    private void setPreference(String key, String value) {
        try {
            SharedPreferences.Editor editor = this.sharedPreferences.edit();

            editor.putString(key, value);

            editor.apply();
        } catch (Exception exception) {
            Log.e("BmgPreferences", "error saving preference " + key, exception);
        }
    }

    private String getPreference(String key) {
        try {
            return this.sharedPreferences.getString(key, "");
        } catch (Exception exception) {
            Log.e("BmgPreferences", "error retrieving preference " + key, exception);
        }

        return "";
    }

    private <T> T getPreference(String key, Class<T> clazz) {
        try {
            return JsonUtilities.parseJson(getPreference(key), clazz);
        } catch (Exception exception) {
            Log.e("BmgPreferences", "error retrieving preference " + key, exception);
        }

        return null;
    }
}
