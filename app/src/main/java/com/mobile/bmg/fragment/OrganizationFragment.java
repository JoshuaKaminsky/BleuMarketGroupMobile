package com.mobile.bmg.fragment;

import android.content.Intent;
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
import com.mobile.android.client.utilities.StringUtilities;
import com.mobile.android.client.utilities.Utilities;
import com.mobile.android.common.FontableTextView;
import com.mobile.bmg.R;
import com.mobile.bmg.activity.OrganizationInformationActivity;
import com.mobile.bmg.model.Organization;

import java.util.Random;

/**
 * Created by Josh on 10/12/15.
 */
public class OrganizationFragment extends Fragment {

    private static String ItemKey = "ITEM_KEY";
    private static String CoverIndexKey = "COVER_INDEX_KEY";

    private Organization organization;

    private static int coverIndex = -1;

    private boolean noItems;

    private View rootView;

    public static OrganizationFragment newInstance(Organization organization, int coverIndex) {
        OrganizationFragment fragment = new OrganizationFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ItemKey, JsonUtilities.getJson(organization));
        bundle.putInt(CoverIndexKey, coverIndex);

        fragment.setArguments(bundle);
        fragment.setOrganization(organization, coverIndex);

        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            this.setOrganization(JsonUtilities.parseJson(savedInstanceState.getString(ItemKey), Organization.class), savedInstanceState.getInt(CoverIndexKey));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ItemKey, JsonUtilities.getJson(this.organization));
        outState.putInt(CoverIndexKey, this.coverIndex);
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
            if(this.noItems) {
                rootView.findViewById(R.id.loading_spinner).setVisibility(View.GONE);

                ImageView noItemTextView = (ImageView)rootView.findViewById(R.id.no_item_image);
                noItemTextView.setVisibility(View.VISIBLE);
                noItemTextView.setImageResource(R.drawable.no_charities);
            }
            return rootView;
        }

        rootView.findViewById(R.id.loading_spinner).setVisibility(View.GONE);

        rootView.findViewById(R.id.browse_fragment_view).setVisibility(View.VISIBLE);

        //set the background of this based on the category
        FrameLayout header = (FrameLayout)rootView.findViewById(R.id.section_header);
        header.setBackgroundResource(Utilities.getResId(String.format("cover%d", this.coverIndex), R.drawable.class));
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OrganizationInformationActivity.class);
                intent.putExtra(OrganizationInformationActivity.ItemKey, JsonUtilities.getJson(organization));

                startActivity(intent);
            }
        });

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
        tags.setText(StringUtilities.Join("\n", this.organization.tags));

        String descriptionText = StringUtilities.IsNullOrEmpty(this.organization.description) ? this.organization.missionStatement : this.organization.description;
        FontableTextView description = (FontableTextView)rootView.findViewById(R.id.item_description);
        description.setText(Html.fromHtml(descriptionText));

        return rootView;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        int coverIndex = this.coverIndex;
        if(coverIndex == -1){
            coverIndex = new Random().nextInt(10);
        }

        this.setOrganization(organization, coverIndex);
    }

    public void setOrganization(Organization organization, int coverIndex) {
        this.organization = organization;

        this.noItems = false;

        this.coverIndex = coverIndex;

        this.setView(this.rootView);
    }

    public void setNoOrganization() {
        this.organization = null;

        this.noItems = true;

        this.setView(this.rootView);
    }
}