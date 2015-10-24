package com.mobile.bmg.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobile.android.client.utilities.Utilities;
import com.mobile.android.common.FontableTextView;
import com.mobile.android.common.model.ApiRequest;
import com.mobile.android.common.model.ResultCallback;
import com.mobile.android.contract.IAsyncParamedAction;
import com.mobile.android.contract.IParamAction;
import com.mobile.android.contract.IResultCallback;
import com.mobile.bmg.R;
import com.mobile.bmg.adapter.FilterAdapter;
import com.mobile.bmg.adapter.OpportunityAdapter;
import com.mobile.bmg.adapter.OrganizationAdapter;
import com.mobile.bmg.common.BmgApiClient;
import com.mobile.bmg.common.BmgApp;
import com.mobile.bmg.common.BmgPreferences;
import com.mobile.bmg.model.BrowsePreferences;
import com.mobile.bmg.model.Category;
import com.mobile.bmg.model.Tag;
import com.mobile.bmg.model.api.RequestOpportunities;
import com.mobile.bmg.model.api.RequestOrganizations;
import com.mobile.bmg.model.api.ResponseCategory;
import com.mobile.bmg.model.api.ResponseOpportunities;
import com.mobile.bmg.model.api.ResponseOrganizations;
import com.mobile.bmg.model.api.ResponseTags;

public class BrowseActivity extends BaseActivity {

    ViewPager viewPager;

    OpportunityAdapter opportunityAdapter;

    OrganizationAdapter organizationAdapter;

    Spinner filter;

    ArrayAdapter organizationFilterAdapter;

    ArrayAdapter opportunityFilterAdapter;

    Tag[] organizationTags;

    Category[] opportunityCategories;

    Tag selectedTag;

    Category selectedCategory;

    boolean donateSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browse);

        BrowsePreferences preferences = new BmgPreferences(this).getBrowsePreferences();
        if(preferences != null) {
            this.selectedCategory = preferences.selectedOpportunityCategory;

            this.selectedTag = preferences.selectedOrganizationTag;
        }

        this.organizationAdapter = new OrganizationAdapter(getSupportFragmentManager(), getOrganizationRequest());

        this.opportunityAdapter = new OpportunityAdapter(getSupportFragmentManager(), getOpportunitiesRequest());

        this.viewPager = (ViewPager) findViewById(R.id.pager);
        this.viewPager.setPageMargin(Utilities.dpToPx(this, 5));
        this.viewPager.setClipToPadding(true);

        organizationFilterAdapter = new FilterAdapter(this, R.layout.browse_spinner_style);
        organizationFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        opportunityFilterAdapter = new FilterAdapter(this, R.layout.browse_spinner_style);
        opportunityFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        filter = (Spinner) findViewById(R.id.browse_spinner);
        filter.setAdapter(organizationFilterAdapter);
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getAdapter() == organizationFilterAdapter) {
                    selectedTag = (Tag) parent.getItemAtPosition(position);

                    viewPager.removeAllViews();

                    organizationAdapter.update();

                    viewPager.setAdapter(organizationAdapter);

                    viewPager.invalidate();

                } else if(parent.getAdapter() == opportunityFilterAdapter) {
                    selectedCategory = (Category) parent.getItemAtPosition(position);

                    viewPager.removeAllViews();

                    opportunityAdapter.update();

                    viewPager.setAdapter(opportunityAdapter);

                    viewPager.invalidate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(parent.getAdapter() == organizationFilterAdapter) {
                    selectedTag = null;
                } else if(parent.getAdapter() == opportunityFilterAdapter) {
                    selectedCategory = null;
                }
            }
        });

        final FontableTextView donateButton = (FontableTextView)findViewById(R.id.donate_button);
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donateSelected = true;

                viewPager.removeAllViews();
                viewPager.setAdapter(organizationAdapter);
                viewPager.invalidate();

                filter.setAdapter(organizationFilterAdapter);

                if(selectedTag != null) {
                    filter.setSelection(organizationFilterAdapter.getPosition(selectedTag));
                }

                filter.invalidate();

                setBackgroundToGreen();
            }
        });

        final FontableTextView volunteerButton = (FontableTextView)findViewById(R.id.volunteer_button);
        volunteerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donateSelected = false;

                viewPager.removeAllViews();
                viewPager.setAdapter(opportunityAdapter);
                viewPager.invalidate();

                filter.setAdapter(opportunityFilterAdapter);

                if(selectedCategory != null) {
                    filter.setSelection(opportunityFilterAdapter.getPosition(selectedCategory));
                }

                filter.invalidate();

                setBackgroundToBlue();
            }
        });

        BmgApiClient.getAllOrganizationTags(new ResultCallback(getOrganizationTagSuccess, responseError, ResponseTags.class));

        BmgApiClient.getOpportunityCategories(new IResultCallback<ResponseCategory>() {
            @Override
            public Class<ResponseCategory> getType() {
                return ResponseCategory.class;
            }

            @Override
            public void call(ResponseCategory result) {
                opportunityCategories = result.categories;
                if(selectedCategory == null) {
                    selectedCategory = opportunityCategories[0];
                }
                for(Category category : opportunityCategories) {
                    opportunityFilterAdapter.add(category);
                }
                opportunityFilterAdapter.notifyDataSetChanged();
            }
        });

        setBackgroundToGreen();

        setBusy(true);
    }

    @Override
    protected void onStop() {
        super.onStop();

        BrowsePreferences preferences = new BrowsePreferences();
        preferences.selectedOpportunityCategory = this.selectedCategory;
        preferences.selectedOrganizationTag = this.selectedTag;

        new BmgPreferences(this).setBrowsePreferences(preferences);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(donateSelected) {
            setBackgroundToGreen();
        } else {
            setBackgroundToBlue();
        }
    }

    private IParamAction getOrganizationTagSuccess = new IParamAction<Void, ResponseTags>() {
        @Override
        public Void Execute(ResponseTags result) {
            setBusy(false);
            organizationTags = result.tags;
            if(selectedTag == null) {
                selectedTag = organizationTags[0];
            }
            for (Tag tag: organizationTags) {
                organizationFilterAdapter.add(tag);
            }

            filter.setSelection(organizationFilterAdapter.getPosition(selectedTag));

            organizationFilterAdapter.notifyDataSetChanged();
            return null;
        }
    };

    private IParamAction responseError = new IParamAction<Void,String>() {
        @Override
        public Void Execute(String item) {
            setBusy(false);
            Toast.makeText(BrowseActivity.this, item, Toast.LENGTH_LONG).show();
            return null;
        }
    };

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

        setStatusBarColor(R.color.bmg_dark_blue);
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

        setStatusBarColor(R.color.bmg_dark_green);
    }

    private void setStatusBarColor(int color){
        BmgApp.setStatusBarColor(getResources().getColor(color));
    }

    private ApiRequest getOrganizationRequest() {
        return new ApiRequest(new IAsyncParamedAction<ResponseOrganizations, Integer>() {
            @Override
            public void Execute(Integer page, final IResultCallback<ResponseOrganizations> callback) {
                RequestOrganizations requestOrganizations = new RequestOrganizations();
                requestOrganizations.page = page;
                requestOrganizations.tag = selectedTag;

                BmgApiClient.getOrganizations(requestOrganizations, new IResultCallback<ResponseOrganizations>() {
                    @Override
                    public Class<ResponseOrganizations> getType() {
                        return ResponseOrganizations.class;
                    }

                    @Override
                    public void call(ResponseOrganizations result) {
                        callback.call(result);
                    }
                });
            }
        });
    }

    private ApiRequest getOpportunitiesRequest(){
        return new ApiRequest(new IAsyncParamedAction<ResponseOpportunities, Integer>() {
            @Override
            public void Execute(Integer page, final IResultCallback<ResponseOpportunities> callback) {
                RequestOpportunities requestOpportunities = new RequestOpportunities(page);
                requestOpportunities.category = selectedCategory != null ? selectedCategory.name : "";

                BmgApiClient.getOpportunities(requestOpportunities, new IResultCallback<ResponseOpportunities>() {
                    @Override
                    public Class<ResponseOpportunities> getType() {
                        return ResponseOpportunities.class;
                    }

                    @Override
                    public void call(ResponseOpportunities result) {
                        callback.call(result);
                    }
                });
            }
        });
    }

}
