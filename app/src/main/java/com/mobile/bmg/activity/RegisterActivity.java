package com.mobile.bmg.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.android.client.utilities.StringUtilities;
import com.mobile.android.common.FontableTextView;
import com.mobile.android.common.TextValidator;
import com.mobile.android.common.model.ResultCallback;
import com.mobile.android.contract.IParamAction;
import com.mobile.bmg.R;
import com.mobile.bmg.common.BmgApiClient;
import com.mobile.bmg.common.BmgApp;
import com.mobile.bmg.model.User;
import com.mobile.bmg.model.api.RequestLogin;
import com.mobile.bmg.model.api.RequestRegister;
import com.mobile.bmg.model.api.ResponseLogin;
import com.mobile.bmg.model.api.ResponseUserCreate;

/**
 * Created by Josh on 10/19/15.
 */
public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.register_toolbar);
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

        final EditText username = ((EditText)findViewById(R.id.register_email_text));
        username.addTextChangedListener(new TextValidator(username) {
            @Override
            public void validate(TextView textView, String text) {
                if (text == null) {
                    username.setError("email is required");
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    username.setError("email is invalid");
                    return;
                }
            }
        });

        final EditText password = ((EditText)findViewById(R.id.register_password_text));
        password.addTextChangedListener(new TextValidator(password) {
            @Override public void validate(TextView textView, String text) {
                if (text == null) {
                    password.setError("password is required");
                    return;
                }

                if(text.length() < 8) {
                    password.setError("password should be at least 8 characters");
                    return;
                }
            }
        });

        final EditText confirmation = ((EditText)findViewById(R.id.register_password_confirmation_text));
        confirmation.addTextChangedListener(new TextValidator(confirmation) {
            @Override public void validate(TextView textView, String text) {
                if(!text.equalsIgnoreCase(password.getText().toString())) {
                    confirmation.setError("passwords do not match");
                    return;
                }
            }
        });

        final EditText firstName = ((EditText)findViewById(R.id.register_first_name_text));
        firstName.addTextChangedListener(new TextValidator(firstName) {
            @Override public void validate(TextView textView, String text) {
                if(StringUtilities.IsNullOrEmpty(text)) {
                    firstName.setError("first name is required");
                    return;
                }
            }
        });

        final EditText lastName = ((EditText)findViewById(R.id.register_last_name_text));
        lastName.addTextChangedListener(new TextValidator(lastName) {
            @Override public void validate(TextView textView, String text) {
                if(StringUtilities.IsNullOrEmpty(text)) {
                    lastName.setError("last name is required");
                    return;
                }
            }
        });

        FontableTextView registerButton = (FontableTextView)findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();

                user.emailAddress = username.getText().toString();
                user.password = password.getText().toString();
                user.passwordConfirmation = confirmation.getText().toString();
                user.firstName = firstName.getText().toString();
                user.lastName = lastName.getText().toString();

                if(user.isValid()) {
                    register(user);
                } else {
                    Toast.makeText(v.getContext(), "please fix errors", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void register(final User user) {
        setBusy(true);

        RequestRegister request = new RequestRegister(user);

        BmgApiClient.createUser(request, new ResultCallback(createUserSuccess, responseError, ResponseUserCreate.class));
    }

    IParamAction createUserSuccess = new IParamAction<Void, ResponseUserCreate>() {
        @Override
        public Void Execute(ResponseUserCreate response) {
            BmgApp.setUser(response.user);

            BmgApiClient.login(new RequestLogin(response.user.emailAddress, response.user.password), new ResultCallback(loginSuccess, responseError, ResponseLogin.class));

            return null;
        }
    };

    IParamAction loginSuccess = new IParamAction<Void, ResponseLogin>() {
        @Override
        public Void Execute(ResponseLogin result) {
            setBusy(false);

            BmgApp.setToken(result.token);

            setResult(RESULT_OK);
            finish();

            return null;
        }
    };
}
