package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 20/5/2016.
 */
public class Coupon implements LayoutId{


    /**
     * id : 7
     * coupon_money : 100
     * valid_begin_time : 2016-06-01
     * valid_end_time : 2016-06-30
     * coupon_status : 0
     * created_at : 2016-05-19 11:25:32
     */

    @JsonProperty("id")
    private String id;
    @JsonProperty("coupon_money")
    private String couponMoney;
    @JsonProperty("valid_begin_time")
    private String validBeginTime;
    @JsonProperty("valid_end_time")
    private String validEndTime;
    @JsonProperty("coupon_status")
    private String couponStatus;
    @JsonProperty("created_at")
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(String couponMoney) {
        this.couponMoney = couponMoney;
    }

    public String getValidBeginTime() {
        return validBeginTime;
    }

    public void setValidBeginTime(String validBeginTime) {
        this.validBeginTime = validBeginTime;
    }

    public String getValidEndTime() {
        return validEndTime;
    }

    public void setValidEndTime(String validEndTime) {
        this.validEndTime = validEndTime;
    }

    public String getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(String couponStatus) {
        this.couponStatus = couponStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_coupon;
    }
}
