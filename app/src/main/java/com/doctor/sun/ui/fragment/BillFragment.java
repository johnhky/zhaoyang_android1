package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.doctor.auto.Factory;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.event.ConfigChangedEvent;
import com.doctor.sun.model.BillModel;
import com.doctor.sun.module.wraper.IncomeModuleWrapper;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.vm.BaseItem;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by rick on 16/2/2017.
 */
@Factory(type = BaseFragment.class, id = "BillFragment")
public class BillFragment extends SortedListFragment {
    public static final String TAG = BillFragment.class.getSimpleName();

    private BillModel model;

    public static Bundle getArgs(String time) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);

        bundle.putString(Constants.BILL_TIME, time);

        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new BillModel();
        String string = getTime();
        IncomeModuleWrapper.getInstance().refreshBillDetail(string);
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
        List<SortedItem> sortedItems = model.parseData(getTime());
        binding.swipeRefresh.setRefreshing(false);
        for (int i = 0; i < sortedItems.size(); i++) {
            BaseItem item = (BaseItem) sortedItems.get(i);
            item.setPosition(i);
            getAdapter().insert(item);
        }
    }

    @Subscribe
    public void onEventMainThread(ConfigChangedEvent event) {
        if (event.getKey().equals(getTime() + Constants.BILL_DETAIL)) {
            loadMore();
        }
    }
}
