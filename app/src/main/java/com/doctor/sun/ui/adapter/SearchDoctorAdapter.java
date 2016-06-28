package com.doctor.sun.ui.adapter;

import android.content.Context;

import com.doctor.sun.entity.constans.AppointmentType;


/**
 * Created by rick on 5/3/2016.
 */
public class SearchDoctorAdapter extends SimpleAdapter {

    private int type;

    public SearchDoctorAdapter(Context context,@AppointmentType int type) {
        super(context);
        this.type = type;
    }

    public int getType() {
        return type;
    }
    public String getTypeLabel() {
        if (type == AppointmentType.DETAIL) {
            return "专属咨询";
        } else {
            return "留言咨询";
        }
    }

}
