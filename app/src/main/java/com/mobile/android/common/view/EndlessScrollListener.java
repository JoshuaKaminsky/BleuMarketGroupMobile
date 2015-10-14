package com.mobile.android.common.view;

import android.widget.AbsListView;

import com.mobile.android.contract.IParamAction;

/**
 * Created by Josh on 10/14/15.
 */
public class EndlessScrollListener implements AbsListView.OnScrollListener {

    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;

    private IParamAction<Void, Integer> getDataByPage;

    public EndlessScrollListener(IParamAction<Void, Integer> getDataByPage) {
        this.getDataByPage = getDataByPage;
    }

    public EndlessScrollListener(IParamAction<Void, Integer> getDataByPage, int visibleThreshold) {
        this.getDataByPage = getDataByPage;
        this.visibleThreshold = visibleThreshold;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            this.getDataByPage.Execute(this.currentPage + 1);
            loading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}