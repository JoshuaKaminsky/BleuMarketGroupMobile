package com.mobile.bmg.activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CollapsingToolbarLayout;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobile.android.client.utilities.ImageDownloader;
import com.mobile.android.client.utilities.JsonUtilities;
import com.mobile.android.client.utilities.StringUtilities;
import com.mobile.bmg.R;
import com.mobile.bmg.model.Opportunity;

import java.util.List;

public class OpportunityInformationActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;

    public static final String ItemKey = "ITEM_KEY";

    private Opportunity opportunity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_opportunity_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.opportunity_information_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.opportunity_information_map);
        mapFragment.getMapAsync(this);

        if(savedInstanceState != null) {
            this.opportunity = JsonUtilities.parseJson(savedInstanceState.getString(ItemKey), Opportunity.class);
        } else {
            Intent intent = getIntent();
            String data = intent.getStringExtra(ItemKey);
            this.opportunity = JsonUtilities.parseJson(data, Opportunity.class);
        }

        if(this.opportunity == null) {
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(this.opportunity == null) {
            finish();
            return;
        }

        Refresh();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng location = getLocationFromAddress();

        this.googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                openInBrowser(opportunity.url);
            }
        });

        this.googleMap.addMarker(new MarkerOptions().position(location).title(opportunity.name).icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_image))).showInfoWindow();

        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10f));
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putString(ItemKey, JsonUtilities.getJson(this.opportunity));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null) {
            this.opportunity = JsonUtilities.parseJson(savedInstanceState.getString(ItemKey), Opportunity.class);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public LatLng getLocationFromAddress() {

        if (this.opportunity.latitude != 0 && this.opportunity.longitude != 0) {
            return new LatLng(this.opportunity.latitude, this.opportunity.longitude);
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
        return String.format("%s, %s %s", opportunity.city, opportunity.state, opportunity.country);
    }

    private void Refresh(){
        ImageView organizationImage = (ImageView)findViewById(R.id.opportunity_information_image);
        organizationImage.setColorFilter(Color.rgb(175, 175, 175), android.graphics.PorterDuff.Mode.MULTIPLY);
        new ImageDownloader().download(this.opportunity.image, organizationImage);

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(opportunity.name);

        ((TextView) findViewById(R.id.opportunity_information_title_text)).setText(Html.fromHtml(opportunity.title));

        ((TextView) findViewById(R.id.opportunity_information_description_text)).setText(Html.fromHtml(opportunity.description));

        ((TextView) findViewById(R.id.opportunity_information_address)).setText(String.format("%s, %s", opportunity.city, opportunity.state));

//        if(StringUtilities.IsNullOrEmpty(opportunity.startDate) || StringUtilities.IsNullOrEmpty(opportunity.endDate)) {
//            findViewById(R.id.opportunity_information_start_date).setVisibility(View.GONE);
//            findViewById(R.id.opportunity_information_end_date).setVisibility(View.GONE);
//        } else {
//            ((TextView) findViewById(R.id.opportunity_information_start_date_text)).setText(String.format("%s, %s", opportunity.startDate, opportunity.startTime));
//            ((TextView) findViewById(R.id.opportunity_information_end_date_text)).setText(String.format("%s, %s", opportunity.endDate, opportunity.endTime));
//        }

        if(StringUtilities.IsNullOrEmpty(opportunity.eventDate)) {
            findViewById(R.id.opportunity_information_event_date).setVisibility(View.GONE);
        } else {
            ((TextView) findViewById(R.id.opportunity_information_event_date_text)).setText(opportunity.eventDate);
        }

        findViewById(R.id.opportunity_information_volunteer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInBrowser(opportunity.url);
            }
        });

    }

    private void openInBrowser(String url) {
        if (!url.startsWith("https://") && !url.startsWith("http://")){
            url = "http://" + url;
        }
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}
