package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.R;
import com.doctor.sun.databinding.FragmentRefreshListBinding;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;

import io.realm.Realm;

/**
 * Created by Rick on 6/7/16.
 */
public class SortedListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    protected FragmentRefreshListBinding binding;
    private SortedListAdapter mAdapter;
    public Realm realm;
    protected boolean isLoading = false;

    public SortedListFragment() {
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 12);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return getAdapter().get(position).getSpan();
            }
        });
        binding.recyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = createAdapter();
        binding.recyclerView.setAdapter(mAdapter);
        binding.swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.swipeRefresh.setOnRefreshListener(this);

        return binding.getRoot();
    }

    @NonNull
    public SortedListAdapter createAdapter() {
        return new SortedListAdapter(getContext());
    }

    @CallSuper
    protected void loadMore() {
        binding.swipeRefresh.setRefreshing(true);
    }

    public SortedListAdapter getAdapter() {
        return mAdapter;
    }

    public FragmentRefreshListBinding getBinding() {
        return binding;
    }

    @Override
    public void onRefresh() {
        if (!isLoading) {
            isLoading = true;
            binding.swipeRefresh.setRefreshing(true);
            loadMore();
        } else {
            binding.swipeRefresh.setRefreshing(false);
        }
    }

    public void disableRefresh() {
        isLoading = true;
    }
}
