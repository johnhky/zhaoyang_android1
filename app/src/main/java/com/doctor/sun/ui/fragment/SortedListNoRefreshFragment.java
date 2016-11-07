package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.R;
import com.doctor.sun.databinding.FragmentList2Binding;
import com.doctor.sun.databinding.FragmentRefreshListBinding;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;

import io.ganguo.library.core.event.EventHub;
import io.realm.Realm;

/**
 * Created by Rick on 6/7/16.
 */
public class SortedListNoRefreshFragment extends BaseFragment  {
    protected FragmentList2Binding binding;
    private SortedListAdapter mAdapter;
    public Realm realm;

    public SortedListNoRefreshFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        EventHub.register(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        EventHub.unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentList2Binding.inflate(inflater, container, false);
        binding.recyclerView.setLayoutManager(createLayoutManager());
        mAdapter = createAdapter();
        binding.recyclerView.setAdapter(mAdapter);

        return binding.getRoot();
    }

    @NonNull
    public GridLayoutManager createLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 12, getOrientation(), false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return getAdapter().get(position).getSpan();
            }
        });
        return gridLayoutManager;
    }

    public int getOrientation() {
        return LinearLayoutManager.VERTICAL;
    }

    @NonNull
    public SortedListAdapter createAdapter() {
        return new SortedListAdapter();
    }

    @CallSuper
    protected void loadMore() {
    }

    public SortedListAdapter getAdapter() {
        return mAdapter;
    }

    public FragmentList2Binding getBinding() {
        return binding;
    }

}
