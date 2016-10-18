package com.doctor.sun.ui.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.ui.fragment.BottomSheetTabFragment;
import com.doctor.sun.ui.pager.AnswerPagerAdapter;

/**
 * Created by rick on 18/10/2016.
 */

public class AppointmentHistoryDialog extends BottomSheetTabFragment {
    private Appointment appointment;

    public static AppointmentHistoryDialog newInstance(Appointment data) {
        AppointmentHistoryDialog fragment = new AppointmentHistoryDialog();
        Bundle bundle = new Bundle();

        bundle.putParcelable(Constants.DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointment = getArguments().getParcelable(Constants.DATA);
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        return new AnswerPagerAdapter(getChildFragmentManager(), appointment);
    }

}
