package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 11/18/15.
 */
public class VoipAccount {
    @JsonProperty("user_id")
    private long userId;
    @JsonProperty("subAccountSid")
    private String subAccountSid;
    @JsonProperty("subToken")
    private String subToken;
    @JsonProperty("voipAccount")
    private String voipAccount;
    @JsonProperty("voipPwd")
    private String voipPwd;

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

    @Override
    public String toString() {
        return "AccountEntity{" +
                "userId=" + userId +
                ", subAccountSid='" + subAccountSid + '\'' +
                ", subToken='" + subToken + '\'' +
                ", voipAccount='" + voipAccount + '\'' +
                ", voipPwd='" + voipPwd + '\'' +
                '}';
    }
}
