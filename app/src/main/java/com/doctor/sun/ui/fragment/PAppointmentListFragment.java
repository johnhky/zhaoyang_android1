package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;

/**
 * Created by rick on 30/12/2015.
 */
public class PAppointmentListFragment extends ListFragment {
    private AppointmentModule api = Api.of(AppointmentModule.class);


    public static PAppointmentListFragment newInstance(int type) {
        PAppointmentListFragment instance;
        instance = new PAppointmentListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA, type);
        instance.setArguments(bundle);
        return instance;
    }

    public PAppointmentListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_appointment, R.layout.p_item_appointment);
        return adapter;
    }

    @Override
    protected void loadMore() {
        api.patientAppointment(getCallback().getPage()).enqueue(getCallback());
    }

    private int getType() {
        return getArguments().getInt(Constants.DATA);
    }

}
