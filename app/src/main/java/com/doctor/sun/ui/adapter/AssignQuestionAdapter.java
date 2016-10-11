package com.doctor.sun.ui.adapter;

import android.content.Context;

/**
 * Created by lucas on 1/18/16.
 */
public class AssignQuestionAdapter extends SimpleAdapter {
    private String appointmentId;

    public String getAppointmentId() {
        return appointmentId;
    }

    public AssignQuestionAdapter(Context context, String appointmentId) {
        super(context);
        this.appointmentId = appointmentId;
    }

    public interface GetAppointmentId {
        String getAppointmentId();
    }
}
