package com.doctor.sun.ui.fragment;

import android.support.annotation.NonNull;

import com.doctor.sun.http.Api;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.adapter.DruglistAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;

/**
 * Created by rick on 1/3/2016.
 */
public class DrugListFragment extends ListFragment {
    private DrugModule api = Api.of(DrugModule.class);
    private static DrugListFragment instance;

    public static DrugListFragment getInstance() {
        if (instance == null) {
            instance = new DrugListFragment();
        }
        return instance;
    }


    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        return new DruglistAdapter(getContext());
    }

    @Override
    protected void loadMore() {
        api.orderList(getCallback().getPage()).enqueue(getCallback());
    }
}
