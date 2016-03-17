package com.doctor.sun.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityListBinding;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;
import com.doctor.sun.ui.model.HeaderViewModel;

/**
 * Created by rick on 11/25/15.
 */
public abstract class PageActivity extends BaseActivity2 {
    private ActivityListBinding binding;
    private SimpleAdapter mAdapter;
    private PageCallback callback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list);

        binding.setHeader(getHeaderViewModel());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = createAdapter();
        callback = createCallback();
        binding.recyclerView.setAdapter(mAdapter);

        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                loadMore();
            }
        });
    }

    @NonNull
    protected PageCallback createCallback() {
        return new PageCallback(mAdapter);
    }

    protected abstract void loadMore();

    protected SimpleAdapter getAdapter() {
        return mAdapter;
    }

    @NonNull
    protected HeaderViewModel getHeaderViewModel() {
        return new HeaderViewModel(this).setMidTitle("昭阳医生");
    }


    @NonNull
    protected SimpleAdapter createAdapter() {
        return new SimpleAdapter(this);
    }

    public PageCallback getCallback() {
        return callback;
    }
}
