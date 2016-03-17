package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.doctor.sun.http.Api;
import com.doctor.sun.module.AppointmentModule;

/**
 * Created by rick on 30/12/2015.
 */
public class PUrgentCallFragment extends ListFragment {
    private AppointmentModule api = Api.of(AppointmentModule.class);


    public static PUrgentCallFragment getInstance() {
        PUrgentCallFragment instance;
        instance = new PUrgentCallFragment();
        return instance;
    }

    public PUrgentCallFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void loadMore() {
        api.pUrgentCalls(getCallback().getPage()).enqueue(getCallback());
    }

}
