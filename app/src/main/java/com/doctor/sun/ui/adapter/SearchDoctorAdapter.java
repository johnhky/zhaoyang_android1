package com.doctor.sun.ui.adapter;

import android.content.Context;

import com.doctor.sun.bean.AppointmentType;


/**
 * Created by rick on 5/3/2016.
 */
public class SearchDoctorAdapter extends SimpleAdapter {

    private int type;

    public SearchDoctorAdapter(Context context, int type) {
        super(context);
        this.type = type;
    }

    public int getType() {
        return type;
    }
    public String getTypeLabel() {
        if (type == AppointmentType.DETAIL) {
            return "详细咨询";
        } else {
            return "简捷复诊";
        }
    }

}
