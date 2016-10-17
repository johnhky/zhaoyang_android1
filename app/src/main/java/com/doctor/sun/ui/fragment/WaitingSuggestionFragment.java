package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.adapter.SimpleAdapter;

import io.ganguo.library.util.Tasks;

/**
 * Created by rick on 3/6/2016.
 */
public class WaitingSuggestionFragment extends RefreshListFragment {
    private SimpleAdapter adapter;

    public static WaitingSuggestionFragment newInstance() {

        WaitingSuggestionFragment fragment = new WaitingSuggestionFragment();

        return fragment;
    }

    public String getData() {
        return getArguments().getString(Constants.DATA);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadMore();
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        adapter = new SimpleAdapter(getContext());
        adapter.onFinishLoadMore(true);

        return adapter;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        getAdapter().onFinishLoadMore(true);
        getAdapter().clear();
        binding.emptyIndicator.setText("待医生诊断");
        binding.emptyIndicator.setVisibility(View.VISIBLE);
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getAdapter().notifyDataSetChanged();
                binding.swipeRefresh.setRefreshing(false);
            }
        }, 1000);
    }

}
