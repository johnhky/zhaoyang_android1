package com.doctor.sun.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 23/1/2016.
 */
public class WeChatPayDTO {
    /**
     * appid : wxe541efd34c189cf1
     * noncestr : BGZaN0IQrG9XbvWc
     * package : Sign=WXPay
     * prepayid : wx2015110413412332e5263c8c0297796090
     * partnerid : 1282212201
     * timestamp : 1446615683
     * sign : 0FB48C26D018DF9475AB2FD3E580E1C5
     */

    @JsonProperty("appid")
    private String appid;
    @JsonProperty("noncestr")
    private String noncestr;
    @JsonProperty("package")
    private String packageX;
    @JsonProperty("prepayid")
    private String prepayid;
    @JsonProperty("partnerid")
    private String partnerid;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("sign")
    private String sign;

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAppid() {
        return appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public String getPackageX() {
        return packageX;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSign() {
        return sign;
    }


    @Override
    public String toString() {
        return "WeChatPayDTO{" +
                "appid='" + appid + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", packageX='" + packageX + '\'' +
                ", prepayid='" + prepayid + '\'' +
                ", partnerid='" + partnerid + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
