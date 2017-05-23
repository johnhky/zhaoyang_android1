package com.doctor.sun.immutables;

import com.doctor.sun.R;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.vm.BaseItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by heky on 17/4/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Titration{

    @JsonProperty("take_medicine_days")
    public String take_medicine_days;
    @JsonProperty("morning")
    public String morning;
    @JsonProperty("noon")
    public String noon;
    @JsonProperty("night")
    public String night;
    @JsonProperty("before_sleep")
    public String before_sleep;

    public Titration() {
    }

    public String getTake_medicine_days() {
        return take_medicine_days;
    }

    public void setTake_medicine_days(String take_medicine_days) {
        this.take_medicine_days = take_medicine_days;
    }

    public String getMorning() {
        return morning;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public String getNoon() {
        return noon;
    }

    public void setNoon(String noon) {
        this.noon = noon;
    }

    public String getNight() {
        return night;
    }

    public void setNight(String night) {
        this.night = night;
    }

    public String getBefore_sleep() {
        return before_sleep;
    }

    public void setBefore_sleep(String before_sleep) {
        this.before_sleep = before_sleep;
    }

    @Override
    public String toString() {
        return "titration:{take_medicine_days:"+take_medicine_days+",morning:"+morning+",noon:"+noon+",night:"+night+",before_sleep:"+before_sleep+"}";
    }
}
