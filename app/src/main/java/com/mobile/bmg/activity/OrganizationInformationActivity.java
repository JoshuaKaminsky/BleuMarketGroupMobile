package com.mobile.bmg.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobile.android.client.utilities.ImageDownloader;
import com.mobile.android.client.utilities.JsonUtilities;
import com.mobile.android.client.utilities.StringUtilities;
import com.mobile.android.common.FontableTextView;
import com.mobile.android.common.model.ResultCallback;
import com.mobile.android.contract.IParamAction;
import com.mobile.bmg.R;
import com.mobile.bmg.common.BmgApiClient;
import com.mobile.bmg.common.BmgApp;
import com.mobile.bmg.model.Organization;
import com.mobile.bmg.model.api.RequestOrganizationTotals;
import com.mobile.bmg.model.api.RequestToggleUserLike;
import com.mobile.bmg.model.api.ResponseOrganizationTotals;

import java.text.NumberFormat;
import java.util.List;

public class OrganizationInformationActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;

    public static final String ItemKey = "ITEM_KEY";

    private Organization organization;

    private boolean userLikesThisOrganization = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_organization_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.organization_information_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.organization_information_map);
        mapFragment.getMapAsync(this);

        if(savedInstanceState != null) {
            this.organization = JsonUtilities.parseJson(savedInstanceState.getString(ItemKey), Organization.class);
        } else {
            Intent intent = getIntent();
            String data = intent.getStringExtra(ItemKey);
            this.organization = JsonUtilities.parseJson(data, Organization.class);
        }

        if(this.organization == null) {
            finish();
            return;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        if(this.organization == null) {
            finish();
            return;
        }

        Refresh();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng location = getLocationFromAddress();

        this.googleMap.addMarker(new MarkerOptions().position(location).title(organization.name).icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_image))).showInfoWindow();

        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10f));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case BmgApp.LoginRequest:
                if(resultCode == RESULT_OK) {
                    Intent intent = new Intent(this, DonateActivity.class);
                    intent.putExtra(DonateActivity.OrganizationKey, JsonUtilities.getJson(organization));
                    startActivity(intent);
                }
            case BmgApp.DonateRequest:
                if(resultCode == RESULT_OK) {
                    Refresh();
                }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putString(ItemKey, JsonUtilities.getJson(this.organization));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public LatLng getLocationFromAddress() {

        if (this.organization.latitude != 0 && this.organization.longitude != 0) {
            return new LatLng(this.organization.latitude, this.organization.longitude);
        }

        String strAddress = getAddress();

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public String getAddress() {
        return String.format("%s %s %s, %s %s", organization.streetAddress, organization.streetAddressExtended, organization.city, organization.state, organization.postalCode);
    }

    public void call(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone));
        startActivity(callIntent);
    }

    public void email(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));

        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});

        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void Refresh(){
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bmg_green)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!BmgApp.IsLoggedIn()) {
                    Intent intent = new Intent(OrganizationInformationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                userLikesThisOrganization = !userLikesThisOrganization;

                if (!userLikesThisOrganization) {
                    fab.setImageResource(R.drawable.icon_favorite_default);
                } else {
                    fab.setImageResource(R.drawable.icon_favorite_hit);
                }

                BmgApp.setLike(organization, userLikesThisOrganization);

                BmgApiClient.setUserLike(new RequestToggleUserLike(userLikesThisOrganization, organization.id, BmgApp.getToken()));
            }
        });

        userLikesThisOrganization = BmgApp.doesUserLike(organization.id);
        if (!userLikesThisOrganization) {
            fab.setImageResource(R.drawable.icon_favorite_default);
        } else {
            fab.setImageResource(R.drawable.icon_favorite_hit);
        }

        ImageView organizationImage = (ImageView)findViewById(R.id.organization_information_image);
        organizationImage.setColorFilter(Color.rgb(175, 175, 175), android.graphics.PorterDuff.Mode.MULTIPLY);
        new ImageDownloader().download(this.organization.primaryLogo, organizationImage);

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(organization.name);

        String missionText = StringUtilities.IsNullOrEmpty(organization.missionStatement) ? organization.description : organization.missionStatement;

        ((TextView) findViewById(R.id.organization_information_mission_text)).setText(Html.fromHtml(missionText));

        ((TextView) findViewById(R.id.organization_information_address_line1)).setText(String.format("%s %s", organization.streetAddress, organization.streetAddressExtended));

        ((TextView) findViewById(R.id.organization_information_address_line2)).setText(String.format("%s, %s %s", organization.city, organization.state, organization.postalCode));

        ((TextView) findViewById(R.id.organization_information_contact_text)).setText(String.format("%s %s", organization.contactFirstName, organization.contactLastName));

        ((TextView) findViewById(R.id.organization_information_contact_email)).setText(organization.contactEmailAddress);

        ((TextView) findViewById(R.id.organization_information_contact_phone)).setText(organization.contactPhoneNumber);

//        ((TextView) findViewById(R.id.organization_information_contributions_text)).setText(format.format(organization.contributions));
//
//        ((TextView) findViewById(R.id.organization_information_total_revenue_text)).setText(format.format(organization.totalRevenue));
//
//        ((TextView) findViewById(R.id.organization_information_program_expenses_text)).setText(format.format(organization.programExpenses));
//
//        ((TextView) findViewById(R.id.organization_information_administrative_expenses_text)).setText(format.format(organization.administrativeExpenses));
//
//        ((TextView) findViewById(R.id.organization_information_fundraising_expenses_text)).setText(format.format(organization.fundraisingExpenses));

        findViewById(R.id.organization_information_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtilities.IsNullOrEmpty(organization.contactPhoneNumber))
                    return;

                call(organization.contactPhoneNumber);
            }
        });

        findViewById(R.id.organization_information_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtilities.IsNullOrEmpty(organization.contactEmailAddress))
                    return;

                email(organization.contactEmailAddress);
            }
        });

        BmgApiClient.getOrganizationTotals(new RequestOrganizationTotals(organization.id), new ResultCallback(getOrganizationTotalsSuccess, responseError, ResponseOrganizationTotals.class));

        FontableTextView donateButton = (FontableTextView) findViewById(R.id.organization_information_donate_button);
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BmgApp.IsLoggedIn()) {
                    Intent intent = new Intent(v.getContext(), DonateActivity.class);
                    intent.putExtra(DonateActivity.OrganizationKey, JsonUtilities.getJson(organization));
                    startActivityForResult(intent, BmgApp.DonateRequest);
                } else {
                    startActivityForResult(new Intent(v.getContext(), LoginActivity.class), BmgApp.LoginRequest);
                }
            }
        });
    }

    IParamAction getOrganizationTotalsSuccess = new IParamAction<Void, ResponseOrganizationTotals>() {
        @Override
        public Void Execute(ResponseOrganizationTotals response) {
            setBusy(false);

            final NumberFormat format = NumberFormat.getCurrencyInstance();

            ((TextView) findViewById(R.id.organization_information_donation_total_text)).setText(format.format(response.donationAmount));

            ((TextView) findViewById(R.id.organization_information_total_donations_text)).setText(Integer.toString(response.count));

            ((TextView) findViewById(R.id.organization_information_donation_average_text)).setText(format.format(response.averageDonation));

            return null;
        }
    };
}
