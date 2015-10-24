package com.mobile.bmg.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobile.android.client.utilities.JsonUtilities;
import com.mobile.android.client.utilities.StringUtilities;
import com.mobile.android.common.TextValidator;
import com.mobile.android.common.model.ResultCallback;
import com.mobile.android.contract.IParamAction;
import com.mobile.bmg.R;
import com.mobile.bmg.common.BmgApiClient;
import com.mobile.bmg.model.Organization;
import com.mobile.bmg.model.api.RequestOrganizations;
import com.mobile.bmg.model.api.ResponseOrganizations;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Josh on 10/20/15.
 */
public class SearchActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap googleMap;

    private Marker selectedMarker;

    private HashMap<Marker, Organization> markerMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        final EditText searchBox = (EditText)findViewById(R.id.search_text);
        searchBox.addTextChangedListener(new TextValidator(searchBox) {
            @Override
            public void validate(TextView textView, String text) {
                if (Pattern.matches("^[0-9]{5}(?:-[0-9]{4})?$", text)) {
                    search(text);
                }
            }
        });

        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String text = v.getText().toString();
                    if(StringUtilities.IsNullOrEmpty(text)) {
                        return false;
                    }

                    if (!Pattern.matches("^[0-9]{5}(?:-[0-9]{4})?$", v.getText())) {
                        searchBox.setError("please enter a valid zip code");
                    }
                }
                return false;
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.search_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        this.googleMap.setOnCameraChangeListener(this);

        this.googleMap.setOnInfoWindowClickListener(this);

        this.googleMap.setOnMarkerClickListener(this);

        search("45040");
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if(selectedMarker != null && distanceBetween(selectedMarker.getPosition(), cameraPosition.target) < 8046.72 || cameraPosition.zoom <= 2) { //five miles
            return;
        }

        setBusy(true);

        String zipcode = getZipcodeFromLocation(cameraPosition.target);

        if(StringUtilities.IsNullOrEmpty(zipcode)) {
            setBusy(false);
            return;
        }

        BmgApiClient.getOrganizations(new RequestOrganizations(zipcode, 20, 1), new ResultCallback(searchSuccess, responseError, ResponseOrganizations.class));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        this.selectedMarker = marker;

        return false; //default behavior should occur
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Organization organization = markerMap.get(marker);

        Intent intent = new Intent(SearchActivity.this, OrganizationInformationActivity.class);

        intent.putExtra(OrganizationInformationActivity.ItemKey, JsonUtilities.getJson(organization));

        startActivity(intent);
    }

    private IParamAction searchSuccess = new IParamAction<Void, ResponseOrganizations>() {
        @Override
        public Void Execute(ResponseOrganizations response) {
            markerMap.clear();
            googleMap.clear();
            for(Organization organization: response.organizations) {
                LatLng location = getLocationFromAddress(organization);
                if(location != null) {
                    Marker marker = googleMap.addMarker(new MarkerOptions().position(location).title(organization.name).icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_image)));

                    markerMap.put(marker, organization);
                }
            }

            setBusy(false);

            return null;
        }
    };

    private void search(String zipcode) {
        if(this.googleMap == null) {
            Toast.makeText(this, "map has not finished initializing.  please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        LatLng location = getLocationFromZipCode(zipcode);

        if(location == null) {
            return;
        }

        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10f));
    }

    private LatLng getLocationFromZipCode(String zipcode) {
        LatLng result = null;

        try {
            List<Address> address = new Geocoder(this).getFromLocationName(zipcode, 5);
            if (address == null || address.size() < 1) {
                Toast.makeText(this, "could not find location from zip code"  + zipcode, Toast.LENGTH_SHORT).show();
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            result = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {
            Log.e("Lcation Search", "Could not get location from zip code " + zipcode, ex);
            Toast.makeText(this, "could not find location from zip code " + zipcode, Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    private String getZipcodeFromLocation(LatLng location) {
        try {
            List<Address> address = new Geocoder(this).getFromLocation(location.latitude, location.longitude, 5);
            if (address == null || address.size() < 1) {
                Toast.makeText(this, "could not find zip code", Toast.LENGTH_SHORT).show();
            }
            return address.get(0).getPostalCode();

        } catch (Exception ex) {
            Log.e("Lcation Search", "Could not get location from zip code", ex);
            Toast.makeText(this, "could not find zip code", Toast.LENGTH_SHORT).show();
        }

        return "";
    }

    private LatLng getLocationFromAddress(Organization organization) {

        if (organization.latitude != 0 && organization.longitude != 0) {
            return new LatLng(organization.latitude, organization.longitude);
        }

        String strAddress = getAddress(organization);

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

    private String getAddress(Organization organization) {
        return String.format("%s %s %s, %s %s", organization.streetAddress, organization.streetAddressExtended, organization.city, organization.state, organization.postalCode);
    }

    private double distanceBetween(LatLng locationStart, LatLng locationEnd) {
        Location start = new Location("start");
        start.setLatitude(locationStart.latitude);
        start.setLongitude(locationStart.longitude);

        Location end = new Location("end");
        end.setLatitude(locationEnd.latitude);
        end.setLongitude(locationEnd.longitude);

        return start.distanceTo(end);
    }
}
