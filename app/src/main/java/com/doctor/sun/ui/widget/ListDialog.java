package com.doctor.sun.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.doctor.sun.R;
import com.doctor.sun.databinding.DialogListBinding;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;

/**
 * Created by rick on 25/1/2016.
 */
public class ListDialog extends Dialog {
    public ListDialog(Context context) {
        super(context);
    }

    private DialogListBinding binding;
    private SimpleAdapter adapter;
    private PageCallback<Object> callback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initAdapter();
        initRecyclerView();
    }


    protected void initRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    protected void initAdapter() {
        adapter = new SimpleAdapter(getContext());
        callback = new PageCallback<>(adapter);
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                loadMore();
            }
        });

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

    public DialogListBinding getBinding() {
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
