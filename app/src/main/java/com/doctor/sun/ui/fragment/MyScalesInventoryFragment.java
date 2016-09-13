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

public class MyScalesInventoryFragment extends RefreshListFragment {
    public static final String TAG = MyScalesInventoryFragment.class.getSimpleName();

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
        adapter.mapLayout(R.layout.item_template2,R.layout.item_invetory_template);
        adapter.putInt(AdapterConfigKey.ID, getAppointmentId());
        return adapter;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        refreshEmptyIndicator();
        questionModule.myTemplates(getPageCallback().getPage()).enqueue(getPageCallback());
    }

    public int getAppointmentId() {
        return Integer.parseInt(getArguments().getString(Constants.DATA, ""));
    }
}
