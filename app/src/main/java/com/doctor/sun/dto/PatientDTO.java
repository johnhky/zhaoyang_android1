package com.doctor.sun.dto;

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

}
