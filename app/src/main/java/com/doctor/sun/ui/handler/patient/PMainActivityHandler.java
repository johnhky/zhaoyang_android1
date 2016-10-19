package com.doctor.sun.ui.handler.patient;

import android.content.Context;
import android.content.Intent;

import com.doctor.sun.ui.activity.patient.PAfterServiceActivity;
import com.doctor.sun.ui.activity.patient.PAppointmentListActivity;
import com.doctor.sun.ui.activity.patient.SelectAppointmentTypeActivity;

/**
 * Created by rick on 14/7/2016.
 */

public class PMainActivityHandler {
    public static final String TAG = PMainActivityHandler.class.getSimpleName();

    public void selectAppointmentType(Context context) {
        Intent intent = SelectAppointmentTypeActivity.intentFor(context);
        context.startActivity(intent);
    }

    public void appointmentList(Context context) {
        Intent intent = PAppointmentListActivity.makeIntent(context);
        context.startActivity(intent);
    }

    public void afterService(Context context) {
        Intent intent = PAfterServiceActivity.intentFor(context);
        context.startActivity(intent);
    }
}
