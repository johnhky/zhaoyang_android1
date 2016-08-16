package com.doctor.sun.vo;

import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.handler.AppointmentHandler;

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

    public AppointmentHandler getHandler() {
        return data.getHandler();
    }

    public String getProgress() {
        return data.getProgress();
    }

    @Override
    public long getCreated() {
        return Long.MAX_VALUE-1;
    }
}
