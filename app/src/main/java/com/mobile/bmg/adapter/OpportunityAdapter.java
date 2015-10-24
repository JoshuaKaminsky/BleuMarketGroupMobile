package com.mobile.bmg.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mobile.android.client.utilities.CollectionUtilities;
import com.mobile.android.common.model.ApiRequest;
import com.mobile.android.contract.IResultCallback;
import com.mobile.bmg.fragment.OpportunityFragment;
import com.mobile.bmg.model.Opportunity;
import com.mobile.bmg.model.api.ResponseOpportunities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Josh on 10/12/15.
 */
public class OpportunityAdapter extends FragmentPagerAdapter {

    private final Random coverIndexGenerator;

    private FragmentManager fragmentManager;

    private ApiRequest<ResponseOpportunities> apiRequest;

    private final List<Opportunity> opportunities = new ArrayList<Opportunity>();

    private int currentPage = 0;

    private int pageSize = 20;

    private int totalCount = 0;

    public OpportunityAdapter(FragmentManager fragmentManager, ApiRequest<ResponseOpportunities> apiRequest) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;

        this.apiRequest = apiRequest;

        this.coverIndexGenerator = new Random();
    }

    @Override
    public int getCount() {
        int count = this.opportunities.size() + 1;

        int total = totalCount > 0 ? totalCount : 1;

        return count > total ? total : count;
    }

    @Override
    public Fragment getItem(int position) {
        this.currentPage = position / pageSize;

        if(opportunities.size() > position) {
            return OpportunityFragment.newInstance(this.opportunities.get(position), this.coverIndexGenerator.nextInt(10));
        }

        final OpportunityFragment fragment = new OpportunityFragment();

        this.apiRequest.Request(this.currentPage + 1, new IResultCallback<ResponseOpportunities>() {
            @Override
            public Class<ResponseOpportunities> getType() {
                return ResponseOpportunities.class;
            }

            @Override
            public void call(ResponseOpportunities result) {
                currentPage++;

                totalCount = result.totalCount;

                opportunities.addAll(CollectionUtilities.ToList(result.opportunities));

                if(result != null && result.opportunities != null && result.opportunities.length > 0) {
                    fragment.setOpportunity(result.opportunities[0], coverIndexGenerator.nextInt(10));
                } else {
                    fragment.setNoOpportunity();
                }

                notifyDataSetChanged();
            }
        });

        return fragment;
    }

    @Override
    public int getItemPosition(Object item) {
        if (!(item instanceof OpportunityFragment)) {
            return POSITION_NONE;
        }

        OpportunityFragment fragment = (OpportunityFragment)item;

        Opportunity opportunity = fragment.getOpportunity();
        if(opportunity == null)
            return POSITION_NONE;

        return this.opportunities.indexOf(opportunity);
    }

    @Override
    public float getPageWidth (int position) {
        if(this.totalCount <= 1) {
            return 1f;
        }

        return 0.93f;
    }

    public void update() {
        this.currentPage = 0;

        this.totalCount = 0;

        this.opportunities.clear();

        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments == null) {
            return;
        }

        fragments.clear();

        notifyDataSetChanged();
    }

}
