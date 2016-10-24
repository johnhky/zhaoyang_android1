package com.doctor.sun.http.callback;

import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.ui.adapter.core.LoadMoreAdapter;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

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
        page += 1;
        if (response != null) {
            getAdapter().insertAll(response.getData());
        }
        int to = response != null ? response.getTo() : 0;
        int total = response != null ? response.getTotal() : 0;
        int perPage = response != null ? response.getPerPage() : 0;
        boolean isLastPage = to >= total || (page - 1) * perPage >= total;
        getAdapter().onFinishLoadMore(isLastPage);
        if (isLastPage) {
            insertFooter();
        }
        getAdapter().notifyDataSetChanged();
        onFinishRefresh();
    }

    public void insertFooter() {

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
        if (t instanceof UnknownHostException) {
            Toast.makeText(AppContext.me(), "无法连接服务器,请检查您的网络连接", Toast.LENGTH_SHORT).show();
        } else if (t instanceof SocketTimeoutException) {
            Toast.makeText(AppContext.me(), "无法连接服务器,请检查您的网络连接", Toast.LENGTH_SHORT).show();
        }
        if (getAdapter() != null) {
            getAdapter().onFinishLoadMore(true);
        }
        onFinishRefresh();
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
