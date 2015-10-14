package com.mobile.bmg.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mobile.android.client.utilities.CollectionUtilities;
import com.mobile.android.common.model.PageRequest;
import com.mobile.android.contract.IResultCallback;
import com.mobile.bmg.fragment.OpportunityFragment;
import com.mobile.bmg.model.Opportunity;
import com.mobile.bmg.model.api.ResponseOpportunities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josh on 10/12/15.
 */
public class OpportunityAdapter extends FragmentPagerAdapter {

    private PageRequest<ResponseOpportunities> pageRequest;

    private final List<Opportunity> opportunities = new ArrayList<Opportunity>();

    private int currentPage = 0;

    private int pageSize = 20;

    private int totalCount = 0;

    public OpportunityAdapter(FragmentManager fragmentManager, PageRequest<ResponseOpportunities> pageRequest) {
        super(fragmentManager);

        this.pageRequest = pageRequest;
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
            return OpportunityFragment.newInstance(this.opportunities.get(position));
        }

        final OpportunityFragment fragment = new OpportunityFragment();

        this.pageRequest.Request(this.currentPage + 1, new IResultCallback<ResponseOpportunities>() {
            @Override
            public Class<ResponseOpportunities> GetType() {
                return ResponseOpportunities.class;
            }

            @Override
            public void call(ResponseOpportunities result) {
                currentPage++;

                totalCount = result.totalCount;

                opportunities.addAll(CollectionUtilities.ToList(result.opportunities));

                if(result != null && result.opportunities != null && result.opportunities.length > 0)
                    fragment.setOpportunity(result.opportunities[0]);

                notifyDataSetChanged();
            }
        });

        return fragment;
    }

    @Override
    public int getItemPosition(Object item) {
        Class c = item.getClass();

        if (c != OpportunityFragment.class) {
            return POSITION_NONE;
        }

        OpportunityFragment fragment = (OpportunityFragment)item;

        Opportunity opportunity = fragment.getOpportunity();
        if(opportunity == null)
            return POSITION_UNCHANGED;

        return this.opportunities.indexOf(opportunity);
    }

    @Override
    public float getPageWidth (int position)
    {
        return 0.93f;
    }
}
