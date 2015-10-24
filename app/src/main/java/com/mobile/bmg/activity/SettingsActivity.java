package com.mobile.bmg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mobile.android.common.FontableTextView;
import com.mobile.android.common.model.ResultCallback;
import com.mobile.android.contract.IParamAction;
import com.mobile.bmg.R;
import com.mobile.bmg.common.BmgApiClient;
import com.mobile.bmg.common.BmgApp;
import com.mobile.bmg.model.User;
import com.mobile.bmg.model.api.RequestLogout;
import com.mobile.bmg.model.api.ResponseLogout;

/**
 * Created by Josh on 10/20/15.
 */
public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        //int coarsePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Refresh();
    }

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }

    private void Refresh() {
        String username = "";

        FontableTextView loginButton = (FontableTextView)findViewById(R.id.settings_login_text);
        if(BmgApp.IsLoggedIn()) {
            loginButton.setText("Logout");
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBusy(true);
                    BmgApiClient.logout(new RequestLogout(BmgApp.getToken()), new ResultCallback(logoutSuccess, responseError, ResponseLogout.class));
                }
            });

            User user = BmgApp.getUser();
            if(user != null) {
                username = String.format("%s %s", user.firstName, user.lastName);
            }
        } else {
            loginButton.setText("Login");
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                }
            });
        }

        FontableTextView usernameText = (FontableTextView)findViewById(R.id.settings_user_name);
        usernameText.setText(username);
    }

    private IParamAction logoutSuccess = new IParamAction<Void, ResponseLogout>() {
        @Override
        public Void Execute(ResponseLogout item) {
            setBusy(false);
            Toast.makeText(SettingsActivity.this, "you have been logged out.", Toast.LENGTH_LONG).show();
            BmgApp.logout();

            Refresh();
            return null;
        }
    };

    private IParamAction responseError = new IParamAction<Void,String>() {
        @Override
        public Void Execute(String item) {
            setBusy(false);
            BmgApp.logout();

            Refresh();
            return null;
        }
    };
}
