package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.doctor.auto.Factory;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.module.wraper.IncomeModuleWrapper;
import com.doctor.sun.ui.adapter.SimpleAdapter;

/**
 * Created by rick on 30/12/2015.
 */
@Factory(type = BaseFragment.class, id = "HistoryBillFragment")
public class HistoryBillFragment extends RefreshListFragment {
    public static final String TAG = HistoryBillFragment.class.getSimpleName();

    @NonNull
    public static Bundle getArgs(String time, int type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        bundle.putInt(Constants.DATA, type);
        bundle.putString(Constants.BILL_TIME, time);
        return bundle;
    }

    public HistoryBillFragment() {
    }


    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_appointment,R.layout.item_appointment_bill);
        return adapter;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        PageCallback pageCallback = getPageCallback();
        IncomeModuleWrapper.getInstance()
                .appointmentList(getTime(), getType(), pageCallback.getPage())
                .enqueue(pageCallback);
    }

    @NonNull
    @Override
    protected String getEmptyIndicatorText() {
        return "没有任何咨询订单";
    }

    public String getTime() {
        return getArguments().getString(Constants.BILL_TIME, "");
    }

    public int getType() {
        return getArguments().getInt(Constants.DATA, 0);
    }
}
