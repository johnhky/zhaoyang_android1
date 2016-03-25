package com.doctor.sun.dto;

import com.doctor.sun.entity.AppointMent;
import com.doctor.sun.entity.Prescription;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by rick on 15/2/2016.
 */
public class PrescriptionDTO {


    @JsonProperty("appointment_info")
    private AppointMent appointmentInfo;
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


    public AppointMent getAppointmentInfo() {
        return appointmentInfo;
    }

    public void setAppointmentInfo(AppointMent appointmentInfo) {
        this.appointmentInfo = appointmentInfo;
    }

    public List<Prescription> getDrug() {
        return drug;
    }

    public void setDrug(List<Prescription> drug) {
        this.drug = drug;
    }
}
