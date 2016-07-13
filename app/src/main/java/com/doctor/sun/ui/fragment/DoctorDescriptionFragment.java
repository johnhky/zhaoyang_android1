package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Description;
import com.doctor.sun.ui.adapter.SimpleAdapter;

/**
 * Created by rick on 1/4/2016.
 * 医生简介
 */
public class DoctorDescriptionFragment extends ListFragment {


    public static DoctorDescriptionFragment getInstance(String description) {
        DoctorDescriptionFragment instance = new DoctorDescriptionFragment();
        Bundle args = new Bundle();
        args.putString(Constants.DATA, description);
        instance.setArguments(args);
        return instance;
    }

    private String getDescription() {
        return getArguments().getString(Constants.DATA);
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.onFinishLoadMore(true);
        Description data = new Description(R.layout.item_doctor_description, getDescription());
        adapter.add(data);
        return adapter;
    }
}
