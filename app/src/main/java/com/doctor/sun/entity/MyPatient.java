package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.entity.handler.DoctorHandler;
import com.doctor.sun.vm.BaseItem;
import com.doctor.sun.vm.LayoutId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by heky on 17/5/27.
 */

public class MyPatient implements LayoutId{

    @JsonProperty("doctor_id")
    public int doctorId;
    @JsonProperty("id")
    public int id;
    @JsonProperty("intro_patient")
    public PatientData patient;
    @JsonProperty("intro_type")
    public int type;
    @JsonProperty("new")
    public int news;
    @JsonProperty("new_time")
    public String newTime;
    @JsonProperty("patient_id")
    public int patientId;

    @JsonIgnore
    public DoctorHandler getHandler(){
        DoctorHandler handler = new DoctorHandler(this);
        return handler;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getNews() {
        return news;
    }

    public void setNews(int news) {
        this.news = news;
    }

    public PatientData getPatient() {
        return patient;
    }

    public void setPatient(PatientData patient) {
        this.patient = patient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getNewTime() {
        return newTime;
    }

    public void setNewTime(String newTime) {
        this.newTime = newTime;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_my_patient;
    }
}
