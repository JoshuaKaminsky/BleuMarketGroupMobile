package com.mobile.bmg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mobile.android.common.FontableTextView;
import com.mobile.android.common.model.ResultCallback;
import com.mobile.android.contract.IParamAction;
import com.mobile.bmg.R;
import com.mobile.bmg.common.BmgApiClient;
import com.mobile.bmg.common.BmgApp;
import com.mobile.bmg.model.api.RequestLogin;
import com.mobile.bmg.model.api.RequestUser;
import com.mobile.bmg.model.api.RequestUserLikes;
import com.mobile.bmg.model.api.ResponseLogin;
import com.mobile.bmg.model.api.ResponseUser;
import com.mobile.bmg.model.api.ResponseUserLikes;

/**
 * Created by Josh on 10/19/15.
 */
public class LoginActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.White));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FontableTextView loginButton = (FontableTextView)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(((EditText)findViewById(R.id.usernameText)).getText().toString(), ((EditText)findViewById(R.id.passwordText)).getText().toString());
            }
        });

        TextView signUpText = (TextView)findViewById(R.id.signUpText);
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(v.getContext(), RegisterActivity.class), BmgApp.RegisterRequest);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case BmgApp.RegisterRequest:
                if(resultCode == RESULT_OK) {
                    setResult(RESULT_OK);
                    finish();
                }
        }
    }

    public void login(String username, String password) {
        RequestLogin request = new RequestLogin(username, password);

        setBusy(true);

        BmgApiClient.login(request, new ResultCallback(loginSuccess, responseError, ResponseLogin.class));
    }

    IParamAction loginSuccess = new IParamAction<Void, ResponseLogin>() {
        @Override
        public Void Execute(ResponseLogin result) {
            BmgApp.setToken(result.token);

            BmgApiClient.getUser(new RequestUser(result.token), new ResultCallback(getUserSuccess, responseError, ResponseUser.class));

            return null;
        }
    };

    IParamAction getUserSuccess = new IParamAction<Void, ResponseUser>() {
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
            setBusy(false);

            BmgApp.setUserLikes(response.organizations);

            setResult(RESULT_OK);
            finish();

            return null;
        }
    };
}
