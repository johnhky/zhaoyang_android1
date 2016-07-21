package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 11/20/15.
 * 医生首页
 */
public class DoctorIndex {


    /**
     * login_num : 4
     * last_login : 2015-08-03 15:39
     * appointment_num : 1
     * consult_num : 0
     * transfer_num : 0
     * name : 钟医生
     */

    @JsonProperty("login_num")
    private int loginNum = 0;
    @JsonProperty("last_login")
    private String lastLogin = "";
    @JsonProperty("appointment_num")
    private int appointmentNum = 0;
    @JsonProperty("consult_num")
    private int consultNum = 0;
    @JsonProperty("transfer_num")
    private int transferNum = 0;
    @JsonProperty("follow_up_num")
    private int followUpNum = 0;
    @JsonProperty("name")
    private String name = "";

    public void setLoginNum(int loginNum) {
        this.loginNum = loginNum;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setAppointmentNum(int appointmentNum) {
        this.appointmentNum = appointmentNum;
    }

    public void setConsultNum(int consultNum) {
        this.consultNum = consultNum;
    }

    public void setTransferNum(int transferNum) {
        this.transferNum = transferNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLoginNum() {
        return loginNum;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public int getAppointmentNum() {
        return appointmentNum;
    }

    public int getConsultNum() {
        return consultNum;
    }

    public int getTransferNum() {
        return transferNum;
    }

    public String getName() {
        return name;
    }

    public int getFollowUpNum() {
        return followUpNum;
    }

    public void setFollowUpNum(int followUpNum) {
        this.followUpNum = followUpNum;
    }
}
