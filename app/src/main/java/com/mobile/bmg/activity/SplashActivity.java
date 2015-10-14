package com.mobile.bmg.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.mobile.bmg.R;

/**
 * Created by Josh on 9/13/15.
 */
public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 2500;

    private View busySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onStart() {
        super.onStart();

        busySpinner = findViewById(R.id.splash_loading_spinner);

        final Context context = this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goToBrowseIntent();
            }
        }, SPLASH_TIME_OUT);
    }


    private void goToBrowseIntent() {
        Intent i = new Intent(SplashActivity.this, BrowseActivity.class);
        startActivity(i);

        finish();
    }
}
