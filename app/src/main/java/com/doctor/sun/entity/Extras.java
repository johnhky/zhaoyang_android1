package com.doctor.sun.entity;

import com.doctor.sun.vo.BaseItem;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by kb on 16-10-9.
 */

public class Extras extends BaseItem{

    @JsonProperty("appointment_id")
    private String appointmentId;

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }
}
