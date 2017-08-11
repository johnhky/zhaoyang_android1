package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.netease.nimlib.sdk.auth.LoginInfo;

/**
 * Created by rick on 11/18/15.
 */
public class ImAccount {
    @JsonProperty("user_id")
    private long userId;
    @JsonProperty("subAccountSid")
    private String subAccountSid;
    @JsonProperty("subToken")
    private String subToken;
    @JsonProperty("voip_account")
    private String voipAccount;
    @JsonProperty("voipPwd")
    private String voipPwd;
    @JsonProperty("yunxin_accid")
    private String yunxinAccid;
    @JsonProperty("yunxin_token")
    private String yunxinToken;
    @JsonProperty("phone")
    private String phone;


    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setSubAccountSid(String subAccountSid) {
        this.subAccountSid = subAccountSid;
    }

    public void setSubToken(String subToken) {
        this.subToken = subToken;
    }

    public void setVoipAccount(String voipAccount) {
        this.voipAccount = voipAccount;
    }

    public void setVoipPwd(String voipPwd) {
        this.voipPwd = voipPwd;
    }

    public long getUserId() {
        return userId;
    }

    public String getSubAccountSid() {
        return subAccountSid;
    }

    public String getSubToken() {
        return subToken;
    }

    public String getVoipAccount() {
        return voipAccount;
    }

    public String getVoipPwd() {
        return voipPwd;
    }

    public String getYunxinAccid() {
        return yunxinAccid;
    }

    public void setYunxinAccid(String yunxinAccid) {
        this.yunxinAccid = yunxinAccid;
    }

    public String getYunxinToken() {
        return yunxinToken;
    }

    public void setYunxinToken(String yunxinToken) {
        this.yunxinToken = yunxinToken;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "userId=" + userId +
                ", subAccountSid='" + subAccountSid + '\'' +
                ", subToken='" + subToken + '\'' +
                ", voipAccount='" + voipAccount + '\'' +
                ", voipPwd='" + voipPwd + '\''+"yunxinAccount: "+yunxinAccid+ "yunxinToken:  "+yunxinToken+"phone: "+phone+
                '}';
    }

    @JsonIgnore
    public LoginInfo getLoginInfo() {
        return new LoginInfo(getYunxinAccid(), getYunxinToken());
    }
}
