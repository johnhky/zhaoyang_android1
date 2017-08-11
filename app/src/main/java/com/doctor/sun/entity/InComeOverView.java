package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by rick on 15/2/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InComeOverView {

    public String recent_seven_days_fee;
    public String total_fee;
    public String time;

}

