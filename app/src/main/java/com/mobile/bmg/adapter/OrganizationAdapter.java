package com.mobile.bmg.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mobile.android.client.utilities.CollectionUtilities;
import com.mobile.android.common.model.PageRequest;
import com.mobile.android.contract.IResultCallback;
import com.mobile.bmg.fragment.OrganizationFragment;
import com.mobile.bmg.model.Organization;
import com.mobile.bmg.model.api.ResponseOrganizations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josh on 10/12/15.
 */
public class OrganizationAdapter extends FragmentStatePagerAdapter {

    private PageRequest<ResponseOrganizations> pageRequest;

    private final List<Organization> organizations = new ArrayList<Organization>();

    private int currentPage = 0;

    private int pageSize = 20;

    private int totalCount = 0;

    public OrganizationAdapter(FragmentManager fragmentManager, PageRequest<ResponseOrganizations> pageRequest) {
        super(fragmentManager);

        this.pageRequest = pageRequest;
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
            return OrganizationFragment.newInstance(this.organizations.get(position));
        }

        final OrganizationFragment fragment = new OrganizationFragment();

        this.pageRequest.Request(this.currentPage + 1, new IResultCallback<ResponseOrganizations>() {
            @Override
            public Class<ResponseOrganizations> GetType() {
                return ResponseOrganizations.class;
            }

            @Override
            public void call(ResponseOrganizations result) {
                currentPage++;

                totalCount = result.totalCount;

                organizations.addAll(CollectionUtilities.ToList(result.organizations));

                if(result != null && result.organizations != null && result.organizations.length > 0)
                    fragment.setOrganization(result.organizations[0]);

                notifyDataSetChanged();
            }
        });

        return fragment;
    }

    @Override
    public int getItemPosition(Object item) {
        Class c = item.getClass();

        if (c != OrganizationFragment.class) {
            return POSITION_NONE;
        }

        OrganizationFragment fragment = (OrganizationFragment)item;

        Organization organization = fragment.getOrganization();
        if(organization == null)
            return POSITION_UNCHANGED;

        int position = this.organizations.indexOf(organization);

        return position;
    }

    @Override
    public float getPageWidth (int position)
    {
        return 0.93f;
    }
}
