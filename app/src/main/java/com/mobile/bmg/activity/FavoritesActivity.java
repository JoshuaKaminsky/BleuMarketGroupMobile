package com.mobile.bmg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mobile.android.client.utilities.JsonUtilities;
import com.mobile.android.common.model.ResultCallback;
import com.mobile.android.contract.IParamAction;
import com.mobile.bmg.R;
import com.mobile.bmg.adapter.FavoritesListAdapter;
import com.mobile.bmg.common.BmgApiClient;
import com.mobile.bmg.common.BmgApp;
import com.mobile.bmg.model.Organization;
import com.mobile.bmg.model.api.RequestOrganization;
import com.mobile.bmg.model.api.ResponseOrganization;

/**
 * Created by Josh on 10/20/15.
 */
public class FavoritesActivity extends BaseActivity {

    Organization[] favorites;

    ListView favoritesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorites);

        favoritesListView = (ListView)findViewById(R.id.favorites_list_view);
        favoritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Organization organization = (Organization) parent.getAdapter().getItem(position);

                setBusy(true);
                BmgApiClient.getOrganization(new RequestOrganization(organization.id), new ResultCallback(getOrganizationSuccess, responseError, ResponseOrganization.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Refresh();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }

    private void Refresh() {
        favorites = BmgApp.getUserLikes();

        favoritesListView.setAdapter(new FavoritesListAdapter(favoritesListView.getContext(), favorites));

        if(favorites != null && favorites.length > 0) {
            findViewById(R.id.no_favorites_image).setVisibility(View.GONE);
        } else {
            findViewById(R.id.no_favorites_image).setVisibility(View.VISIBLE);
        }
    }

    IParamAction getOrganizationSuccess = new IParamAction<Void, ResponseOrganization>() {
        @Override
        public Void Execute(ResponseOrganization response) {
            setBusy(false);

            Intent intent = new Intent(FavoritesActivity.this, OrganizationInformationActivity.class);

            intent.putExtra(OrganizationInformationActivity.ItemKey, JsonUtilities.getJson(response.organization));

            startActivity(intent);

            return null;
        }
    };
}
