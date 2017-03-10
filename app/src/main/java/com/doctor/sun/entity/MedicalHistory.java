package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.vm.LayoutId;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by test on 17/3/2.
 */

public class MedicalHistory implements LayoutId {
    @JsonProperty("age")
    public int age;
    @JsonProperty("birthday")
    public  String birthday;
    @JsonProperty("gender")
    public  int gender;
    @JsonProperty("id")
    public  String id;
    @JsonProperty("patient_avatar")
    public String patient_avatar;
    @JsonProperty("patient_id")
    public String patient_id;
    @JsonProperty("patient_name")
    public String patient_name;
    @JsonProperty("record_name")
    public String record_name;
    @JsonProperty("yunxin_accid")
    public String yunxin_accid;

    public int getAge() {
        return age;
    }

    public String getBirthday() {
        return birthday;
    }

    public int getGender() {
        return gender;
    }

    public String getId() {
        return id;
    }

    public String getPatient_avatar() {
        return patient_avatar;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public String getRecord_name() {
        return record_name;
    }

    public String getYunxin_accid() {
        return yunxin_accid;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPatient_avatar(String patient_avatar) {
        this.patient_avatar = patient_avatar;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public void setRecord_name(String record_name) {
        this.record_name = record_name;
    }

    public void setYunxin_accid(String yunxin_accid) {
        this.yunxin_accid = yunxin_accid;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_single;
    }
}
