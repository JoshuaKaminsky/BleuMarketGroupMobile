package com.mobile.bmg.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mobile.android.common.model.ResultCallback;
import com.mobile.android.contract.IParamAction;
import com.mobile.bmg.R;
import com.mobile.bmg.common.BmgApiClient;
import com.mobile.bmg.common.BmgApp;
import com.mobile.bmg.model.api.RequestUser;
import com.mobile.bmg.model.api.RequestUserLikes;
import com.mobile.bmg.model.api.ResponseUser;
import com.mobile.bmg.model.api.ResponseUserLikes;

/**
 * Created by Josh on 9/13/15.
 */
public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(BmgApp.IsLoggedIn()) {
                    BmgApiClient.getUser(new RequestUser(BmgApp.getToken()), new ResultCallback(getUserSuccess, responseError, ResponseUser.class));
                } else {
                    goToBrowseIntent();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void goToBrowseIntent() {
        Intent i = new Intent(SplashActivity.this, MainActivity.class);

        startActivity(i);

        finish();
    }

    private IParamAction getUserSuccess = new IParamAction<Void, ResponseUser>() {
        @Override
        public Void Execute(ResponseUser response) {
            BmgApp.setUser(response.user);

            BmgApiClient.getUserLikes(new RequestUserLikes(response.user.id, BmgApp.getToken()), new ResultCallback(getUserLikesSuccess, responseError, ResponseUserLikes.class));


            return null;
        }
    };

    IParamAction getUserLikesSuccess = new IParamAction<Void, ResponseUserLikes>() {
        @Override
        public Void Execute(ResponseUserLikes response) {
            BmgApp.setUserLikes(response.organizations);

            goToBrowseIntent();

            return null;
        }
    };

    IParamAction responseError = new IParamAction<Void,String>() {
        @Override
        public Void Execute(String item) {
            BmgApp.logout();

            goToBrowseIntent();

            return null;
        }
    };



}
