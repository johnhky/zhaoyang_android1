package com.doctor.sun.ui.widget;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.databinding.FragmentList2Binding;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;

import io.realm.Realm;

/**
 * Created by Lynn on 2/22/16.
 */
public class BottomSheetListFragment<T> extends BottomSheetDialogFragment {
    protected FragmentList2Binding binding;
    private SimpleAdapter mAdapter;
    public Realm realm;
    private boolean isLoading = false;

    public BottomSheetListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentList2Binding.inflate(inflater, container, false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = createAdapter();
        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                if (!isLoading) {
                    loadMore();
                    isLoading = true;
                }
            }

            @Override
            protected void onFinishLoadMore() {
                super.onFinishLoadMore();
                isLoading = false;
            }
        });
        binding.recyclerView.setAdapter(mAdapter);

        return binding.getRoot();
    }


    @NonNull
    protected String getEmptyIndicatorText() {
        return "";
    }

    @NonNull
    public SimpleAdapter createAdapter() {
        return new SimpleAdapter();
    }

    @CallSuper
    protected void loadMore() {
    }

    public SimpleAdapter getAdapter() {
        return mAdapter;
    }

    public FragmentList2Binding getBinding() {
        return binding;
    }

}
