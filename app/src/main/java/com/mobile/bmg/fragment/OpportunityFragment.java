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
import com.mobile.bmg.activity.OpportunityInformationActivity;
import com.mobile.bmg.model.Category;
import com.mobile.bmg.model.Opportunity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Josh on 10/12/15.
 */
public class OpportunityFragment  extends Fragment {

    private static String ItemKey = "ITEM_KEY";
    private static String CoverIndexKey = "COVER_INDEX_KEY";

    protected Opportunity opportunity;

    private static int coverIndex = -1;

    private boolean noItems;

    private View rootView;

    public static OpportunityFragment newInstance(Opportunity opportunity, int coverIndex) {
        OpportunityFragment fragment = new OpportunityFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ItemKey, JsonUtilities.getJson(opportunity));
        bundle.putInt(CoverIndexKey, coverIndex);

        fragment.setArguments(bundle);
        fragment.setOpportunity(opportunity, coverIndex);

        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            this.setOpportunity(JsonUtilities.parseJson(savedInstanceState.getString(ItemKey), Opportunity.class));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ItemKey, JsonUtilities.getJson(this.opportunity));
        outState.putInt(CoverIndexKey, this.coverIndex);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            this.setOpportunity(JsonUtilities.parseJson(savedInstanceState.getString(ItemKey), Opportunity.class));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_browse, container, false);

        return setView(this.rootView);
    }

    private View setView(View rootView) {
        if(this.getOpportunity() == null || rootView == null) {
            if(noItems) {
                rootView.findViewById(R.id.loading_spinner).setVisibility(View.GONE);

                ImageView noItemTextView = (ImageView)rootView.findViewById(R.id.no_item_image);
                noItemTextView.setVisibility(View.VISIBLE);
                noItemTextView.setImageResource(R.drawable.no_charities);
            }
            return rootView;
        }

        rootView.findViewById(R.id.loading_spinner).setVisibility(View.GONE);

        rootView.findViewById(R.id.browse_fragment_view).setVisibility(View.VISIBLE);

        FrameLayout header = (FrameLayout)rootView.findViewById(R.id.section_header);
        header.setBackgroundResource(Utilities.getResId(String.format("cover%d", this.coverIndex), R.drawable.class));
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OpportunityInformationActivity.class);
                intent.putExtra(OpportunityInformationActivity.ItemKey, JsonUtilities.getJson(opportunity));

                startActivity(intent);
            }
        });

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
        int coverIndex = this.coverIndex;
        if(coverIndex == -1){
            coverIndex = new Random().nextInt(10);
        }

        this.setOpportunity(opportunity, coverIndex);
    }

    public void setOpportunity(Opportunity opportunity, int coverIndex) {
        this.opportunity = opportunity;

        this.noItems = false;

        this.coverIndex = coverIndex;

        this.setView(this.rootView);
    }

    public void setNoOpportunity() {
        this.opportunity = null;

        this.noItems = true;

        this.setView(this.rootView);
    }
}