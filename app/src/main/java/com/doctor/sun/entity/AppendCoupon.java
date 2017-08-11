package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by heky on 17/5/25.
 */

public class AppendCoupon {
    @JsonProperty("detail")
    Coupon detail;
    @JsonProperty("simple")
    Coupon simple;
    @JsonProperty("surface")
    Coupon surface;

    public Coupon getDetail() {
        return detail;
    }

    public void setDetail(Coupon detail) {
        this.detail = detail;
    }

    public Coupon getSurface() {
        return surface;
    }

    public void setSurface(Coupon surface) {
        this.surface = surface;
    }

    public Coupon getSimple() {
        return simple;
    }

    public void setSimple(Coupon simple) {
        this.simple = simple;
    }

    @Override
    public String toString() {
        return "simple:"+simple+",surface:"+surface+",detail:"+detail;
    }
}
