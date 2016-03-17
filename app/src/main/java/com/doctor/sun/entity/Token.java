package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 11/18/15.
 */
public class Token {


    /**
     * token : dd8f45c69c2f2cc3b92c610b11ebe96c
     * level : 未认证
     * type : 2
     * account : {"user_id":224,"subAccountSid":"741d9f478d1211e5bb61ac853d9d52fd","subToken":"e252f2865863ea88bf3f6cf156a0c734","voipAccount":"88797700000279","voipPwd":"wTKS0dVP"}
     */

    @JsonProperty("token")
    private String token;
    @JsonProperty("level")
    private String level;
    @JsonProperty("type")
    private int type;
    /**
     * user_id : 224
     * subAccountSid : 741d9f478d1211e5bb61ac853d9d52fd
     * subToken : e252f2865863ea88bf3f6cf156a0c734
     * voipAccount : 88797700000279
     * voipPwd : wTKS0dVP
     */

    @JsonProperty("account")
    private VoipAccount account;

    public void setToken(String token) {
        this.token = token;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAccount(VoipAccount account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public String getLevel() {
        return level;
    }

    public int getType() {
        return type;
    }

    public VoipAccount getAccount() {
        return account;
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                ", level='" + level + '\'' +
                ", type=" + type +
                ", account=" + account +
                '}';
    }
}
