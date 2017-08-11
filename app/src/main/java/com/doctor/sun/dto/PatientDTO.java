package com.doctor.sun.dto;

import com.doctor.sun.entity.AppointmentStatus;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.entity.RecentAppointment;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 12/9/15.
 */
public class PatientDTO {


    /**
     * id : 25
     * doctor_name : 新医生
     * book_time : 2015-08-21 18:02－18:32
     * progress : 0/0
     * return_info : []
     */

    private RecentAppointment recent_appointment;
    @JsonProperty("follow_up_doing_num")
    public int followUpDoingNum = 0;
    @JsonProperty("appointment_num")
    public int appointmentNum = 0;
    @JsonProperty("appointment_status_num")
    public AppointmentStatus appointment_status_num;
    @JsonProperty("drug_order_num")
    public int drugOrderNum = 0;
    @JsonProperty("applying_num")
    public int applyingNum = 0;
    @JsonProperty("self_record")
    public boolean self_record;
    public void setRecent_appointment(RecentAppointment recent_appointment) {
        this.recent_appointment = recent_appointment;
    }

    public RecentAppointment getRecent_appointment() {
        return recent_appointment;
    }

    private Patient info;

    public Patient getInfo() {
        return info;
    }

    public void setInfo(Patient info) {
        this.info = info;
    }

    public int totalOrderNum() {
        return appointmentNum + drugOrderNum;
    }

    public int totalFollowUpNum() {
        return followUpDoingNum + applyingNum;
    }


    public boolean isSelf_record() {
        return self_record;
    }

    public void setSelf_record(boolean self_record) {
        this.self_record = self_record;
    }

    // 新流程使用
    public int getMyOrderNum() {
        return appointmentNum + followUpDoingNum + applyingNum;
    }

    public AppointmentStatus getAppointment_status_num() {
        return appointment_status_num;
    }

    public void setAppointment_status_num(AppointmentStatus appointment_status_num) {
        this.appointment_status_num = appointment_status_num;
    }

    @Override
    public String toString() {
        return "data{recent_appointment: "+recent_appointment+",self_record: "+self_record+",followUpDoingNum: "
                +followUpDoingNum+",patientInfo: "+info+",appointment_status_num: "+appointment_status_num+"}";
    }
}
