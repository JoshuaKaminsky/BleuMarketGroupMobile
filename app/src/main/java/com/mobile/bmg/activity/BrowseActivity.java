package com.mobile.bmg.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mobile.android.client.utilities.Utilities;
import com.mobile.android.common.FontableTextView;
import com.mobile.android.common.model.PageRequest;
import com.mobile.android.contract.IAsyncParamedAction;
import com.mobile.android.contract.IResultCallback;
import com.mobile.bmg.R;
import com.mobile.bmg.adapter.OpportunityAdapter;
import com.mobile.bmg.adapter.OrganizationAdapter;
import com.mobile.bmg.common.BmgApiClient;
import com.mobile.bmg.model.api.RequestOpportunities;
import com.mobile.bmg.model.api.RequestOrganizations;
import com.mobile.bmg.model.api.ResponseOpportunities;
import com.mobile.bmg.model.api.ResponseOrganizations;
import com.mobile.bmg.model.api.ResponseTags;

public class BrowseActivity extends AppCompatActivity {

    OpportunityAdapter _opportunityAdapter;
    OrganizationAdapter _organizationAdapter;

    ViewPager _viewPager;

    int _currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browse);

        _viewPager = (ViewPager) findViewById(R.id.pager);
        _viewPager.setPageMargin(Utilities.dpToPx(this, 5));
        _viewPager.setClipToPadding(true);

        _organizationAdapter = new OrganizationAdapter(getSupportFragmentManager(), new PageRequest<ResponseOrganizations>(new IAsyncParamedAction<ResponseOrganizations, Integer>() {
            @Override
            public void Execute(Integer page, final IResultCallback<ResponseOrganizations> callback) {
                RequestOrganizations requestOrganizations = new RequestOrganizations();
                requestOrganizations.page = page;

                BmgApiClient.getOrganizations(requestOrganizations, new IResultCallback<ResponseOrganizations>() {
                    @Override
                    public Class<ResponseOrganizations> GetType() {
                        return ResponseOrganizations.class;
                    }

                    @Override
                    public void call(ResponseOrganizations result) {
                        callback.call(result);
                    }
                });
            }
        }));

        _viewPager.setAdapter(_organizationAdapter);

        _opportunityAdapter = new OpportunityAdapter(getSupportFragmentManager(), new PageRequest<ResponseOpportunities>(new IAsyncParamedAction<ResponseOpportunities, Integer>() {
            @Override
            public void Execute(Integer page, final IResultCallback<ResponseOpportunities> callback) {
                RequestOpportunities requestOpportunities = new RequestOpportunities(page);

                BmgApiClient.getOpportunities(requestOpportunities, new IResultCallback<ResponseOpportunities>() {
                    @Override
                    public Class<ResponseOpportunities> GetType() {
                        return ResponseOpportunities.class;
                    }

                    @Override
                    public void call(ResponseOpportunities result) { callback.call(result); }
                });
            }
        }));

        BmgApiClient.getAllOrganizationTags(new IResultCallback<ResponseTags>() {
            @Override
            public Class<ResponseTags> GetType() {
                return ResponseTags.class;
            }

            @Override
            public void call(ResponseTags result) {

            }
        });

        final FontableTextView donateButton = (FontableTextView)findViewById(R.id.donate_button);

        final FontableTextView volunteerButton = (FontableTextView)findViewById(R.id.volunteer_button);

        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _viewPager.removeAllViews();
                _viewPager.setAdapter(_organizationAdapter);
                _viewPager.invalidate();

                setBackgroundToGreen();
            }
        });

        volunteerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _viewPager.removeAllViews();
                _viewPager.setAdapter(_opportunityAdapter);
                _viewPager.invalidate();

                setBackgroundToBlue();
            }
        });

        setBackgroundToGreen();
    }

    private void setBackgroundToBlue() {
        View mainLayout = findViewById(R.id.browse_main_layout);
        mainLayout.setBackgroundResource(R.color.bmg_blue);

        LinearLayout buttonGroup = (LinearLayout)findViewById(R.id.browse_button_group);
        buttonGroup.setBackgroundResource(R.drawable.linear_layout_blue_border);

        FontableTextView donateButton = (FontableTextView)findViewById(R.id.donate_button);
        donateButton.setBackgroundResource(0);
        donateButton.setTextColor(getResources().getColor(R.color.Black));
        donateButton.setOpacity(.28f);

        FontableTextView volunteerButton = (FontableTextView)findViewById(R.id.volunteer_button);
        volunteerButton.setBackgroundResource(R.color.bmg_dark_blue);
        volunteerButton.setTextColor(getResources().getColor(R.color.White));
        volunteerButton.setOpacity(1f);

        setStatusBarColor(R.color.bmg_blue);
    }

    private void setBackgroundToGreen() {
        View mainLayout = findViewById(R.id.browse_main_layout);
        mainLayout.setBackgroundResource(R.color.bmg_green);

        LinearLayout buttonGroup = (LinearLayout)findViewById(R.id.browse_button_group);
        buttonGroup.setBackgroundResource(R.drawable.linear_layout_green_border);

        FontableTextView donateButton = (FontableTextView)findViewById(R.id.donate_button);
        donateButton.setBackgroundResource(R.color.bmg_dark_green);
        donateButton.setTextColor(getResources().getColor(R.color.White));
        donateButton.setOpacity(1f);

        FontableTextView volunteerButton = (FontableTextView)findViewById(R.id.volunteer_button);
        volunteerButton.setBackgroundResource(0);
        volunteerButton.setTextColor(getResources().getColor(R.color.Black));
        volunteerButton.setOpacity(.28f);

        setStatusBarColor(R.color.bmg_green);
    }

    private void setStatusBarColor(int color){
        Window window = getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(getResources().getColor(color));
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
