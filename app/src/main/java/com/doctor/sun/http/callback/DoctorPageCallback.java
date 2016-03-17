package com.doctor.sun.http.callback;

import com.doctor.sun.dto.DoctorPageDTO;
import com.doctor.sun.ui.adapter.core.LoadMoreAdapter;

/**
 * Created by rick on 11/11/15.
 */
public class DoctorPageCallback<T> extends ApiCallback<DoctorPageDTO<T>> {
    public static final String TAG = DoctorPageCallback.class.getSimpleName();

    private int page = 1;
    private LoadMoreAdapter adapter;

    public DoctorPageCallback(LoadMoreAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected void handleResponse(DoctorPageDTO<T> response) {
        int totalPage = -1;
        if (response != null) {
            totalPage = Integer.parseInt(response.getTotal()) / Integer.parseInt(response.getPerPage());
        } else {
            totalPage = 0;
        }
        if (response != null) {
            getAdapter().addAll(response.getList());
        }
        getAdapter().onFinishLoadMore(page >= totalPage || totalPage == 0);
        getAdapter().notifyDataSetChanged();
        page += 1;
    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
        getAdapter().onFinishLoadMore(true);
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

    public void incrementPage() {
        page += 1;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void resetPage() {
        page = 1;
    }
}
