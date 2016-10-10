package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 8/10/2016.
 */
public class JPushExtra {
    @JsonProperty("appointment_id")
    public String appointmentId;
    @JsonProperty("follow_up_id")
    public String followUpId;
    @JsonProperty("apply_id")
    public String applyId;
}
