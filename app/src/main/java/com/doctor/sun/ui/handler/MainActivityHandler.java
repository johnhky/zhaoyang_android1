package com.doctor.sun.ui.handler;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.doctor.sun.ui.activity.doctor.AfterServiceActivity;
import com.doctor.sun.ui.activity.doctor.AppointmentListActivity;

/**
 * Created by rick on 11/20/15.
 */
public class MainActivityHandler extends BaseHandler {

    public MainActivityHandler(Activity context) {
        super(context);
    }

    /**
     * 已预约患者
     *
     * @param view
     */
    public void appointment(View view) {
        Intent intent = AppointmentListActivity.makeIntent(getContext());
        getContext().startActivity(intent);
    }

    /**
     * 紧急咨询
     *
     * @param view
     */
    public void emergencyCall(View view) {
//        Intent intent = UrgentListActivity.makeIntent(getContext());
//        getContext().startActivity(intent);
    }

    /**
     * 随访患者
     *
     * @param view
     */
    public void consultation(View view) {
        Intent intent = AfterServiceActivity.intentFor(getContext());
        getContext().startActivity(intent);
    }
}
