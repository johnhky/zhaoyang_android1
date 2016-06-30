package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.pager.AppointmentPagerAdapter;

/**
 * Created by rick on 30/12/2015.
 */
public class AppointmentListFragment extends RefreshListFragment {
    private AppointmentModule api = Api.of(AppointmentModule.class);
    private int position;


    public static AppointmentListFragment getInstance(int position) {
        AppointmentListFragment instance;
        instance = new AppointmentListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.POSITION, position);
        instance.setArguments(bundle);
        return instance;
    }

    public AppointmentListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.position = getArguments().getInt(Constants.POSITION);
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        String orderType = String.valueOf(AppointmentPagerAdapter.STATUS[position]);
        String page = getPageCallback().getPage();
        api.doctorAppointment(page, orderType).enqueue(getPageCallback());
    }

}
