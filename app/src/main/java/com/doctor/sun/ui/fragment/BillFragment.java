package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.auto.Factory;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.event.ConfigChangedEvent;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.model.BillModel;
import com.doctor.sun.module.wraper.IncomeModuleWrapper;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.vm.BaseItem;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 16/2/2017.
 */
@Factory(type = BaseFragment.class, id = "BillFragment")
public class BillFragment extends SortedListFragment {
    public static final String TAG = BillFragment.class.getSimpleName();

    private BillModel model;
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
        List<SortedItem> sortedItems = model.parseData(time);
        binding.swipeRefresh.setRefreshing(false);
        for (int i = 0; i < sortedItems.size(); i++) {
            BaseItem item = (BaseItem) sortedItems.get(i);
            item.setPosition(i);
            getAdapter().insert(item);
        }
    }

    @Subscribe
    public void onEventMainThread(ConfigChangedEvent event) {
        if (event.getKey().equals(time + Constants.BILL_DETAIL)) {
            loadMore();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bill_history, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_bill_history) {
            IncomeModuleWrapper.getInstance().monthList().enqueue(new SimpleCallback<ArrayList<String>>() {
                @Override
                protected void handleResponse(ArrayList<String> response) {
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
                    builder.items(response)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                    time = String.valueOf(text);
                                    IncomeModuleWrapper.getInstance().refreshBillDetail(time);
                                }
                            });
                    builder.build().show();
                }
            });

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
