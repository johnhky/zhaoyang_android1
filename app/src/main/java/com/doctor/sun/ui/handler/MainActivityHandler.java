package com.doctor.sun.ui.handler;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.doctor.sun.ui.activity.doctor.AfterServiceActivity;
import com.doctor.sun.ui.activity.doctor.AppointmentListActivity;

/**
 * Created by rick on 11/20/15.
 */
public class MainActivityHandler {
    /**
     * 已预约患者
     *
     * @param context
     */
    public void appointment(Context context) {
        Intent intent = AppointmentListActivity.makeIntent(context);
        context.startActivity(intent);
    }

    /**
     * 紧急咨询
     *
     * @param view
     */
    public void emergencyCall(View view) {
//        Intent intent = UrgentListActivity.intentFor(getContext());
//        getContext().startActivity(intent);
    }

    /**
     * 随访患者
     *
     * @param context
     */
    public void consultation(Context context) {
        Intent intent = AfterServiceActivity.intentFor(context);
        context.startActivity(intent);
    }
}
