package com.doctor.sun.vm;

import android.content.Context;
import android.util.Log;

import com.doctor.sun.R;
import com.doctor.sun.entity.handler.AppointmentHandler2;
import com.doctor.sun.immutables.Appointment;

/**
 * Created by rick on 16/8/2016.
 */

public class ItemPatientDetail extends BaseItem {
    public Appointment data;


    public ItemPatientDetail(int itemLayoutId, Appointment data) {
        super(itemLayoutId);
        setItemId("INCLUDE_PATIENT_DETAIL");
        this.data = data;
    }

    public String getProgress() {
        return data.getProgress();
    }

    public Appointment getData() {
        return data;
    }

    public String getPatientInfo(Context context) {
        return context.getResources().getString(R.string.patient_info,
                AppointmentHandler2.getGender(data),
                AppointmentHandler2.getBirthday(data));
    }

    @Override
    public long getCreated() {
        return Long.MAX_VALUE - 1;
    }
}
