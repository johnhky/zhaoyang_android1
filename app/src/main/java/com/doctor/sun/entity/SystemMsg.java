package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.ui.activity.patient.handler.SystemMsgHandler;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.annotations.Ignore;

/**
 * Created by lucas on 1/29/16.
 * 系统信息
 */
public class SystemMsg implements LayoutId {

    /**
     * title : 【昭阳医生】提醒：刘医生提醒您完善问卷，请及时登录处理。
     * doctor_name : 刘医生
     * doctor_avatar : http://7xkt51.com2.z0.glb.qiniucdn.com/FjOdeBEi-6FnkhgIx8wMtUq475gZ
     * patient_avatar : null
     */

    @JsonProperty("title")
    private String title;
    @JsonProperty("doctor_name")
    private String doctorName;
    @JsonProperty("doctor_avatar")
    private String doctorAvatar;
    @JsonProperty("patient_avatar")
    private String patientAvatar;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("patient_name")
    private Object patientName;

    @Ignore
    private int itemLayoutId = R.layout.p_item_system_tip;

    @Override
    public int getItemLayoutId() {
        return itemLayoutId;
    }

    public void setItemLayoutId(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }
    /*@Override
    public int getItemLayoutId() {
        return R.layout.p_item_system_tip;
    }*/

    @Ignore
    private SystemMsgHandler handler = new SystemMsgHandler(this);


    public SystemMsgHandler getHandler() {
        return handler;
    }

    public void setHandler(SystemMsgHandler handler) {
        this.handler = handler;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setDoctorAvatar(String doctorAvatar) {
        this.doctorAvatar = doctorAvatar;
    }

    public void setPatientAvatar(String patientAvatar) {
        this.patientAvatar = patientAvatar;
    }

    public String getTitle() {
        return title;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDoctorAvatar() {
        return doctorAvatar;
    }

    public String getPatientAvatar() {
        return patientAvatar;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }


    public void setPatientName(Object patientName) {
        this.patientName = patientName;
    }

    public Object getPatientName() {
        return patientName;
    }
}
