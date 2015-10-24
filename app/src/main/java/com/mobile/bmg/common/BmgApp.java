package com.mobile.bmg.common;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.mobile.bmg.model.Organization;
import com.mobile.bmg.model.User;

import java.util.HashMap;

/**
 * Created by Josh on 10/18/15.
 */
public final class BmgApp extends Application {

    private static Context context = null;

    private static Window mainWindow = null;

    private static String token;

    private static User user = null;

    private static HashMap<Integer, Organization> likes = new HashMap<>();

    public void onCreate() {
        super.onCreate();

        BmgApp.context = getApplicationContext();

        BmgPreferences preferences = new BmgPreferences(BmgApp.context);

        BmgApp.token = preferences.getUserToken();

        BmgApp.user = preferences.getUser();
    }

    public static final int LoginRequest = 1;

    public static final int RegisterRequest = 2;

    public static final int DonateRequest = 3;

    public static final int CreditCardScanRequest = 4;


    public static boolean IsLoggedIn() {
        return BmgApp.token != null && !BmgApp.token.isEmpty() && BmgApp.user != null;
    }

    public static void setToken(String token) {
        BmgApp.token = token;

        new BmgPreferences(BmgApp.context).setUserToken(token);
    }

    public static String getToken() {
        return BmgApp.token;
    }

    public static User getUser() {
        return BmgApp.user;
    }

    public static void setUser(User user) {
        BmgApp.user = user;

        new BmgPreferences(BmgApp.context).setUser(user);
    }

    public static void setUserLikes(Organization[] organizations) {
        likes.clear();

        for(Organization organization:organizations) {
            if(!likes.containsKey(organization.id)) {
                likes.put(organization.id, organization);
            }
        }
    }

    public static void setLike(Organization organization, boolean doesLike) {
        if(doesLike) {
            likes.put(organization.id, organization);
        } else if(likes.containsKey(organization.id)) {
            likes.remove(organization.id);
        }
    }

    public static Organization[] getUserLikes() {
        return likes.values().toArray(new Organization[likes.size()]);
    }

    public static boolean doesUserLike(int organizationId) {
        return likes.containsKey(organizationId);
    }

    public static void setWindow(Window window) {
        BmgApp.mainWindow = window;
    }

    public static void setStatusBarColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BmgApp.mainWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            BmgApp.mainWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            BmgApp.mainWindow.setStatusBarColor(color);
        }
    }

    public static void logout() {
        BmgApp.token = "";
        BmgApp.user = null;
        BmgApp.likes.clear();
    }
}
