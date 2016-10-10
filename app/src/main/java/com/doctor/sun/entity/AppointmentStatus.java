package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 10/10/2016.
 */

public class AppointmentStatus {

    @JsonProperty("can_edit")
    public int canEdit;
    @JsonProperty("display_status")
    public String displayStatus;
}
