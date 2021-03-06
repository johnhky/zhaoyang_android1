package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.R;
import com.doctor.sun.databinding.FragmentListBinding;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;

import io.realm.Realm;

/**
 * Created by rick on 11/20/15.
 */
public class ListFragment extends BaseFragment {
    protected FragmentListBinding binding;
    private SimpleAdapter mAdapter;
    private PageCallback callback;
    public Realm realm;

    public ListFragment() {
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
        binding = FragmentListBinding.inflate(inflater, container, false);
        binding.recyclerView.setLayoutManager(createLayoutManager());
        mAdapter = createAdapter();
        binding.recyclerView.setAdapter(mAdapter);
        callback = new PageCallback<>(mAdapter);
        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                loadMore();
            }
        });
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.refreshLayout.setRefreshing(false);
            }
        });

        return binding.getRoot();
    }

    @NonNull
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @NonNull
    public SimpleAdapter createAdapter() {
        return new SimpleAdapter();
    }

    protected void loadMore() {

    }

    public PageCallback getCallback() {
        return callback;
    }

    public SimpleAdapter getAdapter() {
        return mAdapter;
    }

    public FragmentListBinding getBinding() {
        return binding;
    }

    public interface SetHeaderListener {
        void setHeaderRightTitle(String title);
    }
}
