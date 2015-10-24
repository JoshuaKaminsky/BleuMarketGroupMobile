package com.mobile.bmg.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mobile.android.client.utilities.CollectionUtilities;
import com.mobile.android.common.model.ApiRequest;
import com.mobile.android.contract.IResultCallback;
import com.mobile.bmg.fragment.OrganizationFragment;
import com.mobile.bmg.model.Organization;
import com.mobile.bmg.model.api.ResponseOrganizations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Josh on 10/12/15.
 */
public class OrganizationAdapter extends FragmentPagerAdapter {

    private final Random coverIndexGenerator;

    private FragmentManager fragmentManager;

    private ApiRequest<ResponseOrganizations> apiRequest;

    private final List<Organization> organizations = new ArrayList<Organization>();

    private int currentPage = 0;

    private int pageSize = 20;

    private int totalCount = 0;

    public OrganizationAdapter(FragmentManager fragmentManager, ApiRequest<ResponseOrganizations> apiRequest) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;

        this.apiRequest = apiRequest;

        this.coverIndexGenerator = new Random();
    }

    @Override
    public int getCount() {
        int count = this.organizations.size() + 1;

        int total = totalCount > 0 ? totalCount : 1;

        return count > total ? total : count;
    }

    @Override
    public Fragment getItem(final int position) {

        this.currentPage = position / pageSize;

        if(organizations.size() > position) {
            return OrganizationFragment.newInstance(this.organizations.get(position), this.coverIndexGenerator.nextInt(10));
        }

        final OrganizationFragment fragment = new OrganizationFragment();

        this.apiRequest.Request(this.currentPage + 1, new IResultCallback<ResponseOrganizations>() {
            @Override
            public Class<ResponseOrganizations> getType() {
                return ResponseOrganizations.class;
            }

            @Override
            public void call(ResponseOrganizations result) {
                currentPage++;

                totalCount = result.totalCount;

                organizations.addAll(CollectionUtilities.ToList(result.organizations));

                if(result != null && result.organizations != null && result.organizations.length > 0) {
                    fragment.setOrganization(result.organizations[0], coverIndexGenerator.nextInt(10));
                } else {
                    fragment.setNoOrganization();
                }

                notifyDataSetChanged();
            }
        });

        return fragment;
    }

    @Override
    public int getItemPosition(Object item) {
        if (!(item instanceof OrganizationFragment)) {
            return POSITION_NONE;
        }

        OrganizationFragment fragment = (OrganizationFragment)item;

        Organization organization = fragment.getOrganization();
        if(organization == null)
            return POSITION_NONE;

        return POSITION_NONE;
        //return this.organizations.indexOf(organization);
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

        this.organizations.clear();

        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments == null) {
            return;
        }

        fragments.clear();

        notifyDataSetChanged();
    }
}
