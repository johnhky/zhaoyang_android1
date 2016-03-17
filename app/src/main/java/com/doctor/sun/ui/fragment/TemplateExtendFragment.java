package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.databinding.ItemCategoryExtendBinding;
import com.doctor.sun.entity.QTemplate;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;

/**
 * Created by lucas on 1/19/16.
 */
public class TemplateExtendFragment extends Fragment {
    private ItemCategoryExtendBinding binding;
    private QuestionModule api = Api.of(QuestionModule.class);
    private SimpleAdapter mAdapter;
    private static TemplateExtendFragment instance;

    public static TemplateExtendFragment getInstance() {
        if (instance == null) {
            instance = new TemplateExtendFragment();
        }
        return instance;
    }

    public SimpleAdapter createAdapter() {
        return new SimpleAdapter(getContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                loadMore();
            }
        });
        return binding.getRoot();
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        binding = ItemCategoryExtendBinding.inflate(inflater, container, false);
        mAdapter = createAdapter();
        binding.rvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCategory.setAdapter(mAdapter);
    }

    private void loadMore() {
        api.templates().enqueue(new PageCallback<QTemplate>(mAdapter));
    }
}
