package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.databinding.FragmentApplyBinding;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;

import io.realm.Realm;

/**
 * Created by lucas on 12/24/15.
 */
public class ApplyFragment extends android.support.v4.app.Fragment {
    private FragmentApplyBinding binding;
    private SimpleAdapter mAdapter;
    public Realm realm;
    public PageCallback callback;

    public ApplyFragment() {

    }

    public FragmentApplyBinding getBinding() {
        return binding;
    }

    public PageCallback getCallback() {
        return callback;
    }

    public SimpleAdapter getAdapter() {
        return mAdapter;
    }

    @NonNull
    public SimpleAdapter createAdapter() {
        return new SimpleAdapter(getContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        binding = FragmentApplyBinding.inflate(inflater, container, false);
        binding.rvQuestion.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = createAdapter();
        binding.rvQuestion.setAdapter(mAdapter);


        callback = new PageCallback<>(mAdapter);
        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                loadMore();
            }
        });
        return binding.getRoot();
    }

    protected void loadMore() {

    }

}
