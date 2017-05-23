package com.doctor.sun.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.R;
import com.doctor.sun.vm.BaseItem;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by heky on 17/4/26.
 */

public class Address extends BaseItem implements Serializable{

    @JsonProperty("address")
    private String address;
    @JsonProperty("defaults")
    private String defaults;
    @JsonProperty("id")
    private int id;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("remark")
    private String remark;
    @JsonProperty("to")
    private String to;
    @JsonProperty("user_id")
    private int user_id;
    @JsonProperty("city")
    private String city;
    @JsonProperty("area")
    private String area;
    @JsonProperty("province")
    private String province;


    @Override
    public int getLayoutId() {
        return R.layout.p_item_addresslist;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDefaults() {
        return defaults;
    }

    public void setDefaults(String defaults) {
        this.defaults = defaults;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getArea() {
        return area;
    }

    @Override
    public void setArea(String area) {
        this.area = area;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return "Address{ province:+" + province + ", city:" + city + ", area:" + area +
                ", address:" + address + ", defaults:" + defaults + ", phone:" + phone +
                ", remark:" + remark + ", to:" + to + ", userid:" + user_id +",id:"+id+ "}";
    }

}
