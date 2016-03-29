package com.doctor.sun.http.callback;

import android.util.Log;

import com.doctor.sun.ui.adapter.core.LoadMoreAdapter;

import java.util.List;

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
        Log.e(TAG, "handleResponse: " + response.size() );
        getAdapter().addAll(response);
        getAdapter().onFinishLoadMore(true);
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
        getAdapter().onFinishLoadMore(true);
    }

    public LoadMoreAdapter getAdapter() {
        return adapter;
    }
}
