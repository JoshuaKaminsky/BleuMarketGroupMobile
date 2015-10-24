package com.mobile.bmg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ToggleButton;

import com.mobile.android.common.FontableTextView;
import com.mobile.bmg.R;
import com.mobile.bmg.common.BmgApiClient;
import com.mobile.bmg.common.BmgApp;
import com.mobile.bmg.model.Organization;
import com.mobile.bmg.model.api.RequestToggleUserLike;

/**
 * Created by Josh on 10/21/15.
 */
public class FavoritesListAdapter extends ArrayAdapter<Organization> {

    private final Context context;

    public FavoritesListAdapter(Context context, Organization[] items) {
        super(context, -1, items);

        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Organization organization = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.favorites_list_item, parent, false);

        FontableTextView textView = (FontableTextView) view.findViewById(R.id.favorites_company_name);
        textView.setText(organization.name);

        final ToggleButton favorite = (ToggleButton)view.findViewById(R.id.favorites_button);
        favorite.setChecked(BmgApp.doesUserLike(organization.id));
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmgApp.setLike(organization, favorite.isChecked());
                BmgApiClient.setUserLike(new RequestToggleUserLike(favorite.isChecked(), organization.id, BmgApp.getToken()));
            }
        });

        return view;
    }
}
