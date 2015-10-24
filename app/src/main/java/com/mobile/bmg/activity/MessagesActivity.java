package com.mobile.bmg.activity;

import android.os.Bundle;

import com.mobile.bmg.R;

/**
 * Created by Josh on 10/20/15.
 */
public class MessagesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_messages);


    }

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }


}
