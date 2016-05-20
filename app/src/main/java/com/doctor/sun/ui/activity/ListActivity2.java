package com.doctor.sun.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityListBinding;
import com.doctor.sun.http.callback.ListCallback;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;

/**
 * Created by rick on 20/1/2016.
 */
public class ListActivity2<T> extends BaseActivity2 implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ActivityListBinding binding;
    private SimpleAdapter adapter;
    private ListCallback<T> callback;

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
    private ListCallback<T> createCallback() {
        return new ListCallback<T>(adapter) {
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

    public ListCallback<T> getCallback() {
        return callback;
    }

    public void setCallback(ListCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        adapter.notifyDataSetChanged();
        adapter.onFinishLoadMore(false);
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

