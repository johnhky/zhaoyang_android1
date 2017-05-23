package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by heky on 17/4/25.
 */

public class SentDrugRecordInfo{

    @JsonProperty
    private boolean data;
    @JsonProperty
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean getData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "sentDrugInfo:{data:"+data+",status:"+status+"}";
    }
}
