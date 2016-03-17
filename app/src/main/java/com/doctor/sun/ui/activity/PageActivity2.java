package com.doctor.sun.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
public class PageActivity2 extends BaseActivity2 implements View.OnClickListener {

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
    }

    protected void initHeader() {
    }

    protected void initRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    protected void initAdapter() {
        adapter = createAdapter();
        callback = new PageCallback<>(adapter);
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                loadMore();
            }
        });

    }

    @NonNull
    public  SimpleAdapter createAdapter() {
        return new SimpleAdapter(this);
    }

    protected void loadMore() {
    }

    protected void refresh() {
        callback.resetPage();
        adapter.clear();
        adapter.notifyDataSetChanged();
        adapter.onFinishLoadMore(false);
        loadMore();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
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
}

