package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;

/**
 * Created by rick on 9/9/2016.
 */

public class ScalesInventoryFragment extends RefreshListFragment {
    public static final String TAG = ScalesInventoryFragment.class.getSimpleName();

    QuestionModule questionModule = Api.of(QuestionModule.class);


    public static Bundle getArgs(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        bundle.putString(Constants.DATA, id);
        return bundle;
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.putString(AdapterConfigKey.ID, getAppointmentId());
        adapter.mapLayout(R.layout.item_scales, R.layout.item_invetory_scales);
        return adapter;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        refreshEmptyIndicator();
        questionModule.scales(getAppointmentId(), "2", getPageCallback().getPage()).enqueue(getPageCallback());
    }


    private String getAppointmentId() {
        return getArguments().getString(Constants.DATA);
    }

}
