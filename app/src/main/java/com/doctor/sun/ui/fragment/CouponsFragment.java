package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Coupon;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ListCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.adapter.core.LoadMoreAdapter;

/**
 * Created by rick on 20/5/2016.
 */
public class CouponsFragment extends RefreshListFragment {
    private ProfileModule api = Api.of(ProfileModule.class);
    private Callback callback;

    public static CouponsFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(Constants.TYPE, type);
        CouponsFragment fragment = new CouponsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        if (callback == null) {
            callback = new Callback(getAdapter());
        }
        api.coupons(getArguments().getString(Constants.TYPE)).enqueue(callback);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMore();
            }
        });
    }


    private class Callback extends ListCallback<Coupon> {

        public Callback(LoadMoreAdapter adapter) {
            super(adapter);
        }

        @Override
        public void onInitHeader() {
            getAdapter().clear();
        }


        @Override
        public void onFinishRefresh() {
            super.onFinishRefresh();
            binding.swipeRefresh.setRefreshing(false);
        }
    }

    @NonNull
    @Override
    protected String getEmptyIndicatorText() {
        return "没有任何优惠券";
    }
}
