package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.R;
import com.doctor.sun.databinding.FragmentRefreshListBinding;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;

import io.realm.Realm;

/**
 * Created by Lynn on 2/22/16.
 */
public class RefreshListFragment extends Fragment {
    protected FragmentRefreshListBinding binding;
    private SimpleAdapter mAdapter;
    public Realm realm;

    public RefreshListFragment() {
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
        binding = FragmentRefreshListBinding.inflate(inflater, container, false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = createAdapter();
        binding.recyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                loadMore();
            }
        });
        binding.swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));

        return binding.getRoot();
    }

    @NonNull
    public SimpleAdapter createAdapter() {
        return new SimpleAdapter(getContext());
    }

    protected void loadMore() {

    }

    public SimpleAdapter getAdapter() {
        return mAdapter;
    }

    public FragmentRefreshListBinding getBinding() {
        return binding;
    }
}
