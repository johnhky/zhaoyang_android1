package com.doctor.sun.ui.adapter;

import android.content.Context;

/**
 * Created by rick on 3/6/2016.
 */
public class AllowAfterServiceAdapter extends SimpleAdapter {
    private int doctorId;
    public AllowAfterServiceAdapter(Context context,int doctorId) {
        super(context);
        this.doctorId = doctorId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
}
