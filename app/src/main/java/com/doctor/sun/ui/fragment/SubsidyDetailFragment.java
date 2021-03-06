package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.doctor.auto.Factory;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.event.ConfigChangedEvent;
import com.doctor.sun.immutables.Subsidy;
import com.doctor.sun.module.wraper.IncomeModuleWrapper;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by rick on 16/2/2017.
 */
@Factory(type = BaseFragment.class, id = "SubsidyDetailFragment")
public class SubsidyDetailFragment extends SortedListFragment {
    public static final String TAG = SubsidyDetailFragment.class.getSimpleName();

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
//        String string = getTime();
//        IncomeModuleWrapper.getInstance().refreshSubsidy(string);
    }

    public String getTime() {
        return getArguments().getString(Constants.BILL_TIME);
    }

    @NonNull
    @Override
    public SortedListAdapter createAdapter() {
        SortedListAdapter adapter = super.createAdapter();
        return adapter;
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
        showOrHideEmptyIndicator(false, "");
        List<Subsidy> subsidies = IncomeModuleWrapper.getInstance().getSubsidy(time).detail;
        int i = 0;
        for (Subsidy p : subsidies) {
            p.setItemId(i + "");
            p.setPosition(i);
            p.setItemLayoutId(R.layout.item_subsidy);
            i += 1;
        }
        getAdapter().insertAll(subsidies);
        getBinding().swipeRefresh.setRefreshing(false);
        showOrHideEmptyIndicator(getAdapter().size() <= 0, "暂时没有更多了");
    }

    @Subscribe
    public void onEventMainThread(ConfigChangedEvent event) {
        if (event.getKey().equals(time + Constants.SUBSIDY)) {
            loadMore();
        }
    }

}
