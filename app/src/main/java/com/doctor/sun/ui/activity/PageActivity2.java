package com.doctor.sun.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityListBinding;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;

/**
 * Created by rick on 20/1/2016.
 */
public class PageActivity2 extends BaseActivity2 implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ActivityListBinding binding;
    private SimpleAdapter adapter;
    private PageCallback<Object> callback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        initHeader();
        initAdapter();
        initRecyclerView();
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.refreshLayout.setOnRefreshListener(this);
    }

    protected void initHeader() {
    }

    protected void initRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    protected void initAdapter() {
        adapter = createAdapter();
        callback = createCallback();
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                loadMore();
            }
        });

    }

    @NonNull
    private PageCallback<Object> createCallback() {
        return new PageCallback<Object>(adapter) {
            @Override
            public void onFinishRefresh() {
                super.onFinishRefresh();
                binding.refreshLayout.setRefreshing(false);
            }
        };
    }

    @NonNull
    public SimpleAdapter createAdapter() {
        return new SimpleAdapter(this);
    }

    protected void loadMore() {
    }


    public ActivityListBinding getBinding() {
        return binding;
    }

    public SimpleAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(SimpleAdapter adapter) {
        this.adapter = adapter;
    }

    public PageCallback getCallback() {
        return callback;
    }

    public void setCallback(PageCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onRefresh() {
        callback.resetPage();
        adapter.clear();
        adapter.onFinishLoadMore(true);
        adapter.notifyDataSetChanged();
        loadMore();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }
}

