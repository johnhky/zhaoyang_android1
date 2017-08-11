package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by heky on 17/5/17.
 */

public class LogTime {
    @JsonProperty("log_time")
    private String log_time ="";

    public String getLog_time() {
        return log_time;
    }

    public void setLog_time(String log_time) {
        this.log_time = log_time;
    }
}
