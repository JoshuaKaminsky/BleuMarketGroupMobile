package com.mobile.bmg.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mobile.android.client.utilities.ImageDownloader;
import com.mobile.android.client.utilities.JsonUtilities;
import com.mobile.android.common.FontableTextView;
import com.mobile.bmg.R;
import com.mobile.bmg.model.Opportunity;
import com.mobile.bmg.model.Organization;

/**
 * Created by Josh on 10/12/15.
 */
public class OrganizationFragment extends Fragment {

    private static String ItemKey = "ITEM_KEY";

    private Organization organization;

    private View rootView;

    public static OrganizationFragment newInstance(Organization organization) {
        OrganizationFragment fragment = new OrganizationFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ItemKey, JsonUtilities.getJson(organization));

        fragment.setArguments(bundle);
        fragment.setOrganization(organization);

        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            this.setOrganization(JsonUtilities.parseJson(savedInstanceState.getString(ItemKey), Organization.class));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ItemKey, JsonUtilities.getJson(this.organization));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null)
            this.setOrganization(JsonUtilities.parseJson(savedInstanceState.getString(ItemKey), Organization.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_browse, container, false);

        return setView(this.rootView);
    }

    public View setView(View rootView) {
        if(this.getOrganization() == null || rootView == null) {
            return rootView;
        }

        rootView.findViewById(R.id.loading_spinner).setVisibility(View.GONE);

        rootView.findViewById(R.id.browse_fragment_view).setVisibility(View.VISIBLE);

        //set the background of this based on the category
        FrameLayout header = (FrameLayout)rootView.findViewById(R.id.section_header);

        ImageView logo = (ImageView)rootView.findViewById(R.id.item_logo);
        new ImageDownloader().download(this.organization.primaryLogo, logo);

        FontableTextView name = (FontableTextView)rootView.findViewById(R.id.item_name);
        name.setText(this.organization.name);

        FontableTextView location = (FontableTextView)rootView.findViewById(R.id.item_location);
        location.setText(this.organization.city + ", " + this.organization.state);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to maps
            }
        });

        FontableTextView tags = (FontableTextView)rootView.findViewById(R.id.item_tags);
        tags.setText(this.organization.type);

        FontableTextView description = (FontableTextView)rootView.findViewById(R.id.item_description);
        description.setText(Html.fromHtml(this.organization.description));

        return rootView;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;

        this.setView(this.rootView);
    }
}