package com.doctor.sun.dto;

import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.util.JacksonUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

import java.util.List;

/**
 * Created by rick on 15/2/2016.
 */
public class PrescriptionDTO implements MsgAttachment {


    @JsonProperty("appointment_info")
    private Appointment appointmentInfo;
    /**
     * mediaclName : 好好保护
     * productName :
     * interval : 每天
     * numbers : [{"早":"0.1"},{"午":"0.2"},{"晚":"0.3"},{"睡前":"100.5"}]
     * unit : 克
     * remark :
     */
    @JsonProperty("drug")
    private List<Prescription> drug;


    public Appointment getAppointmentInfo() {
        return appointmentInfo;
    }

    public void setAppointmentInfo(Appointment appointmentInfo) {
        this.appointmentInfo = appointmentInfo;
    }

    public List<Prescription> getDrug() {
        return drug;
    }

    public void setDrug(List<Prescription> drug) {
        this.drug = drug;
    }

    @Override
    public String toJson(boolean b) {
        return JacksonUtils.toJson(this);
    }
}
