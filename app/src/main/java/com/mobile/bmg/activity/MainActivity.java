package com.mobile.bmg.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TabHost;

import com.mobile.bmg.R;
import com.mobile.bmg.common.BmgApp;

/**
 * Created by Josh on 10/18/15.
 */
public class MainActivity extends TabActivity{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        BmgApp.setWindow(getWindow());

        setContentView(R.layout.activity_main);

        final TabHost tabhost = getTabHost();
        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                setTabColor(tabhost);

                if(tabId.equalsIgnoreCase("browse"))
                    return;

                BmgApp.setStatusBarColor(getResources().getColor(R.color.bmg_dark_green));
            }
        });

        tabhost.getTabWidget().setDividerDrawable(null);

        tabhost.addTab(createTab(BrowseActivity.class, "Browse", R.drawable.tabbaritem_home));
        tabhost.addTab(createTab(SearchActivity.class, "Search", R.drawable.tabbaritem_search));
        tabhost.addTab(createTab(MessagesActivity.class, "Messages", R.drawable.tabbaritem_messages));
        tabhost.addTab(createTab(FavoritesActivity.class, "Favorites", R.drawable.tabbaritem_favorites));
        tabhost.addTab(createTab(SettingsActivity.class, "Settings", R.drawable.tabbaritem_settings));

        setTabColor(tabhost);
    }

    @Override
    public void onBackPressed() {
        if(getTabHost().getCurrentTab() == 0) {
            super.onBackPressed();
        } else {
            getTabHost().setCurrentTab(0);
        }
    }

    private TabHost.TabSpec createTab(final Class<?> intentClass, final String tag, final int drawable)
    {
        final Intent intent = new Intent().setClass(this, intentClass);

        final ImageView tab = (ImageView)LayoutInflater.from(getTabHost().getContext()).inflate(R.layout.tab_layout, null);
        tab.setImageResource(drawable);

        return getTabHost().newTabSpec(tag).setIndicator(tab).setContent(intent);
    }

    public static void setTabColor(TabHost tabhost) {

        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
            ((ImageView)tabhost.getTabWidget().getChildAt(i)).setColorFilter(null);
        }

        ((ImageView)tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab())).setColorFilter(tabhost.getResources().getColor(R.color.bmg_dark_blue));
    }
}
