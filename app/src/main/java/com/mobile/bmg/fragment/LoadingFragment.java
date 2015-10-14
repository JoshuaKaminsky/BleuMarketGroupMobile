package com.mobile.bmg.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.bmg.R;

/**
 * Created by Josh on 10/14/15.
 */
public class LoadingFragment extends Fragment{

    protected String message;

    public LoadingFragment(){
        this.message = "Loading...";
    }

    public static LoadingFragment newInstance(String message) {
        LoadingFragment fragment = new LoadingFragment();

        fragment.message = message;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.busy_dialog, container, false);

        TextView textView = (TextView)rootView.findViewById(R.id.busy_text);
        textView.setText(this.message);

        return rootView;
    }
}
