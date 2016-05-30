package com.doctor.sun.http.callback;

import android.util.Log;

import com.doctor.sun.ui.adapter.core.LoadMoreAdapter;

import java.util.List;

import retrofit2.Call;

/**
 * Created by rick on 11/10/15.
 */
public class ListCallback<T> extends ApiCallback<List<T>> {
    public static final String TAG = ListCallback.class.getSimpleName();
    private LoadMoreAdapter adapter;

    public ListCallback(LoadMoreAdapter adapter) {
        this.adapter = adapter;
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
