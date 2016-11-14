package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 8/10/2016.
 */
public class JPushExtra {
    @JsonProperty("appointment_id")
    public String appointmentId;
    @JsonProperty("apply_id")
    public String applyId;
}
