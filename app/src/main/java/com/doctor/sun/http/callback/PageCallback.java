package com.doctor.sun.http.callback;

import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.ui.adapter.core.LoadMoreAdapter;

import retrofit2.Call;

/**
 * Created by rick on 11/11/15.
 */
public class PageCallback<T> extends ApiCallback<PageDTO<T>> {
    public static final String TAG = PageCallback.class.getSimpleName();

    private int page = 1;
    private LoadMoreAdapter adapter;

    public PageCallback(LoadMoreAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected void handleResponse(PageDTO<T> response) {
        if (adapter == null) {
            return;
        }
        if (page == 1) {
            adapter.clear();
            onInitHeader();
        }
        if (response != null) {
            getAdapter().addAll(response.getData());
        }
        int to = response != null ? response.getTo() : 0;
        int total = response != null ? response.getTotal() : 0;
        getAdapter().onFinishLoadMore(to >= total);
        getAdapter().notifyDataSetChanged();
        page += 1;
        onFinishRefresh();
    }

    public void onInitHeader() {
    }

    public void onFinishRefresh() {

    }

    public void setRefresh() {
        page = 1;
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

    public void setAdapter(LoadMoreAdapter adapter) {
        this.adapter = adapter;
    }

    public String getPage() {
        return String.valueOf(page);
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void resetPage() {
        page = 1;
    }
}
