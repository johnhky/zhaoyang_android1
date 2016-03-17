package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 8/1/2016.
 */
public class ReserveDate {

    /**
     * optional : 1
     * quick : 1
     * detail : 0
     * data : 2015-08-12
     * week : 3
     */

    @JsonProperty("optional")
    private int optional;
    @JsonProperty("quick")
    private int quick;
    @JsonProperty("detail")
    private int detail;
    @JsonProperty("date")
    private String date;
    @JsonProperty("week")
    private int week;


    public void setOptional(int optional) {
        this.optional = optional;
    }

    public void setQuick(int quick) {
        this.quick = quick;
    }

    public void setDetail(int detail) {
        this.detail = detail;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getOptional() {
        return optional;
    }

    public int getQuick() {
        return quick;
    }

    public int getDetail() {
        return detail;
    }

    public String getDate() {
        return date;
    }

    public int getWeek() {
        return week;
    }
}
