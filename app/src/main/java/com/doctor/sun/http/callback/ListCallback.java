package com.doctor.sun.http.callback;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreAdapter;

import java.util.List;

import retrofit2.Call;

/**
 * Created by rick on 11/10/15.
 */
public class ListCallback<T> extends ApiCallback<List<T>> {
    public static final String TAG = ListCallback.class.getSimpleName();
    private LoadMoreAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    public ListCallback(LoadMoreAdapter adapter) {
        this.adapter = adapter;
    }

    public ListCallback(SimpleAdapter adapter, SwipeRefreshLayout refreshLayout) {
        this(adapter);
        this.refreshLayout = refreshLayout;
    }

    @Override
    protected void handleResponse(List<T> response) {
        Log.e(TAG, "handleResponse: " + response.size());
        if (getAdapter() == null) {
            return;
        }
        onInitHeader();
        getAdapter().addAll(response);
        getAdapter().onFinishLoadMore(true);
        getAdapter().notifyDataSetChanged();
        onFinishRefresh();
    }

    public void onFinishRefresh() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
    }

    public void onInitHeader() {

    }

    @Override
    public void onFailure(Call call, Throwable t) {
        t.printStackTrace();
        if (getAdapter() != null) {
            getAdapter().onFinishLoadMore(true);
        }
    }

    public LoadMoreAdapter getAdapter() {
        return adapter;
    }
}
