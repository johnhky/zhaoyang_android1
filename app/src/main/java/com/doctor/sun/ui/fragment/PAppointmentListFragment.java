package com.doctor.sun.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.util.JacksonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 30/12/2015.
 */
public class PAppointmentListFragment extends RefreshListFragment {
    private AppointmentModule api = Api.of(AppointmentModule.class);

    public static PAppointmentListFragment newInstance(String keyword) {
        PAppointmentListFragment instance = new PAppointmentListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.REMARK, keyword);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public PAppointmentListFragment() {
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.patientAppointment(getPageCallback().getPage(), getKeyword()).enqueue(getPageCallback());
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_appointment, R.layout.p_item_appointment);
        return adapter;
    }

    public String getKeyword() {
        return getArguments().getString(Constants.REMARK);
    }

    @NonNull
    @Override
    protected String getEmptyIndicatorText() {
        return "没有任何咨询订单";
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("MSG_UPDATE_SUCCESS")) {
                onRefresh();
            }
        }
    };
}
