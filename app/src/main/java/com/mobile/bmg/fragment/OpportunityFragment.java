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
import com.mobile.android.client.utilities.StringUtilities;
import com.mobile.android.common.FontableTextView;
import com.mobile.bmg.R;
import com.mobile.bmg.model.Category;
import com.mobile.bmg.model.Opportunity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josh on 10/12/15.
 */
public class OpportunityFragment  extends Fragment {

    private static String ItemKey = "ITEM_KEY";

    protected Opportunity opportunity;

    private View rootView;

    public static OpportunityFragment newInstance(Opportunity opportunity) {
        OpportunityFragment fragment = new OpportunityFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ItemKey, JsonUtilities.getJson(opportunity));

        fragment.setArguments(bundle);
        fragment.opportunity = opportunity;

        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            this.setOpportunity(JsonUtilities.parseJson(savedInstanceState.getString(ItemKey), Opportunity.class));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ItemKey, JsonUtilities.getJson(this.opportunity));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null)
            this.setOpportunity(JsonUtilities.parseJson(savedInstanceState.getString(ItemKey), Opportunity.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_browse, container, false);

        return setView(this.rootView);
    }

    private View setView(View rootView) {
        if(this.getOpportunity() == null || rootView == null) {
            return rootView;
        }

        rootView.findViewById(R.id.loading_spinner).setVisibility(View.GONE);

        rootView.findViewById(R.id.browse_fragment_view).setVisibility(View.VISIBLE);
        //set the background of this based on the category
        FrameLayout header = (FrameLayout)rootView.findViewById(R.id.section_header);

        ImageView logo = (ImageView)rootView.findViewById(R.id.item_logo);
        new ImageDownloader().download(this.opportunity.image, logo);

        FontableTextView name = (FontableTextView)rootView.findViewById(R.id.item_name);
        name.setText(this.opportunity.name);

        FontableTextView location = (FontableTextView)rootView.findViewById(R.id.item_location);
        location.setText(this.opportunity.city + ", " + this.opportunity.state);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to maps
            }
        });

        List<String> categories = new ArrayList<String>();
        for(Category category: this.opportunity.categories) {
            categories.add(category.name);
        }

        FontableTextView tags = (FontableTextView)rootView.findViewById(R.id.item_tags);
        tags.setText(StringUtilities.Join("\n", categories.toArray(new String[categories.size()])));

        FontableTextView description = (FontableTextView)rootView.findViewById(R.id.item_description);
        description.setText(Html.fromHtml(this.opportunity.description));

        return rootView;
    }

    public Opportunity getOpportunity() {
        return this.opportunity;
    }

    public void setOpportunity(Opportunity opportunity) {
        this.opportunity = opportunity;

        this.setView(this.rootView);
    }
}