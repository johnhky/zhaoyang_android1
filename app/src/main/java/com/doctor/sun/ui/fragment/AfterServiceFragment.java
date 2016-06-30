package com.doctor.sun.ui.fragment;

import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.entity.AfterService;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;

/**
 * Created by rick on 2/6/2016.
 */
public class AfterServiceFragment extends RefreshListFragment<AfterService> {
    private AfterServiceModule api = Api.of(AfterServiceModule.class);

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_after_service, R.layout.p_item_after_service);
        return adapter;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.patientOrders("", getPageCallback().getPage()).enqueue(getPageCallback());
    }
}
