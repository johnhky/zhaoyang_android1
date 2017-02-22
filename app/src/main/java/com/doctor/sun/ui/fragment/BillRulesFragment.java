package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.doctor.auto.Factory;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.BillDetail;
import com.doctor.sun.event.ConfigChangedEvent;
import com.doctor.sun.module.wraper.IncomeModuleWrapper;
import com.squareup.otto.Subscribe;

/**
 * Created by rick on 16/2/2017.
 */
@Factory(type = BaseFragment.class, id = "BillRulesFragment")
public class BillRulesFragment extends SortedListFragment {
    public static final String TAG = BillRulesFragment.class.getSimpleName();

    private String time;

    public static Bundle getArgs(String time) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);

        bundle.putString(Constants.BILL_TIME, time);

        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        time = getTime();
        setHasOptionsMenu(true);
    }

    public String getTime() {
        return getArguments().getString(Constants.BILL_TIME);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        disableRefresh();
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        BillDetail billDetail = IncomeModuleWrapper.getInstance().getBillDetail(time);
        billDetail.setItemLayoutId(R.layout.item_bill_detail);
        getAdapter().insert(billDetail);
        getBinding().swipeRefresh.setRefreshing(false);
    }

    @Subscribe
    public void onEventMainThread(ConfigChangedEvent event) {
        if (event.getKey().equals(time + Constants.BILL_DETAIL)) {
            loadMore();
        }
    }
}
