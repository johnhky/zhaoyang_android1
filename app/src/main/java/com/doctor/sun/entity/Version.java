package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 19/3/2016.
 */
public class Version {

    /**
     * force_update : false
     * now_version : 1.1
     * download_url :
     */

    @JsonProperty("force_update")
    private boolean forceUpdate;
    @JsonProperty("now_version")
    private double nowVersion;
    @JsonProperty("download_url")
    private String downloadUrl;
    @JsonProperty("md5")
    private String md5;

    public boolean getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public double getNowVersion() {
        return nowVersion;
    }

    public void setNowVersion(double nowVersion) {
        this.nowVersion = nowVersion;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
