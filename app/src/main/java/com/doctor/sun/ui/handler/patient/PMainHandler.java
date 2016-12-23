package com.doctor.sun.ui.handler.patient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.SystemMsg;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.module.PushModule;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.activity.SystemMsgListActivity;
import com.doctor.sun.ui.activity.patient.MedicineStoreActivity;
import com.doctor.sun.ui.activity.patient.MyOrderActivity;
import com.doctor.sun.ui.activity.patient.SearchDoctorActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.doctor.sun.ui.fragment.DrugListFragment;

import java.util.ArrayList;
import java.util.HashMap;

import io.ganguo.library.Config;

/**
 * Created by kb on 21/12/2016.
 */

public class PMainHandler {
    public static final String LAST_VISIT_TIME = "LAST_VISIT_TIME";
    private String visitTimeKey = LAST_VISIT_TIME + Config.getString(Constants.VOIP_ACCOUNT);

    public void lookForDoctor(Context context) {
        Intent intent = SearchDoctorActivity.makeIntent(context);
        context.startActivity(intent);
    }

    public void myOrder(Context context) {
        Intent intent = MyOrderActivity.makeIntent(context);
        context.startActivity(intent);
    }

    public void myDrug(Context context) {
        Bundle bundle = DrugListFragment.getArgs();
        Intent intent = SingleFragmentActivity.intentFor(context, "寄药订单", bundle);
        context.startActivity(intent);
    }

    public void askForService(Context context) {
        Intent intent = MedicineStoreActivity.makeIntent(context);
        context.startActivity(intent);
    }

    public void allMyMessages(Context context) {
        Intent intent = SystemMsgListActivity.makeIntent(context);
        context.startActivity(intent);
    }

    public SimpleAdapter getMessageAdapter() {
        PushModule messageApi = Api.of(PushModule.class);
        final SimpleAdapter adapter = new SimpleAdapter();
        adapter.putLong(AdapterConfigKey.LAST_VISIT_TIME, Config.getLong(visitTimeKey, System.currentTimeMillis()));
        messageApi.systemMsg("1").enqueue(new SimpleCallback<PageDTO<SystemMsg>>() {
            @Override
            protected void handleResponse(PageDTO<SystemMsg> response) {
                // 只显示两条信息
                adapter.insert(response.getData().get(0));
                adapter.insert(response.getData().get(1));
                adapter.notifyDataSetChanged();
            }
        });
        adapter.mapLayout(R.layout.p_item_system_msg, R.layout.item_new_message);

        return adapter;
    }

    public SimpleAdapter getDoctorAdapter() {
        AppointmentModule doctorApi = Api.of(AppointmentModule.class);
        final SimpleAdapter adapter = new SimpleAdapter();
        doctorApi.allDoctors("1", getQueryParams(), getTitleParams()).enqueue(new SimpleCallback<PageDTO<Doctor>>() {
            @Override
            protected void handleResponse(PageDTO<Doctor> response) {
                adapter.insert(response.getData().get(0));
                adapter.insert(response.getData().get(1));
                adapter.notifyDataSetChanged();
            }
        });
        adapter.mapLayout(R.layout.item_doctor, R.layout.item_search_doctor);

        return adapter;
    }

    private HashMap<String, String> getQueryParams() {
        HashMap<String, String> hashMap = new HashMap<>();


        return hashMap;
    }

    private ArrayList<Integer> getTitleParams() {
        ArrayList<Integer> result = new ArrayList<>();


        return result;
    }
}
