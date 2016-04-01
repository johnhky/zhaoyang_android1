package com.doctor.sun.ui.fragment;

import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.vo.ItemDivider;

/**
 * Created by rick on 1/4/2016.
 */
public class DoctorDescriptionFragment extends ListFragment {


    private static DoctorDescriptionFragment instance;

    public static DoctorDescriptionFragment getInstance(String description) {
        if (instance == null) {
            instance = new DoctorDescriptionFragment();
            Bundle args = new Bundle();
            args.putString(Constants.DATA, description);
            instance.setArguments(args);
        }
        return instance;
    }

    private String getDescription() {
        return getArguments().getString(Constants.DATA);
    }

    @Override
    protected void loadMore() {
        ItemDivider data = new ItemDivider(R.layout.item_doctor_description);
        data.setContent(getDescription());
        getAdapter().add(data);
        getAdapter().onFinishLoadMore(true);
        getAdapter().notifyDataSetChanged();
    }
}
