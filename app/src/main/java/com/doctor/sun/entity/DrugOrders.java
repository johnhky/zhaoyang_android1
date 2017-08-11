package com.doctor.sun.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.vm.BaseItem;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Created by heky on 17/4/25.
 */

public class DrugOrders {
    @JsonProperty("appointment_id")
    public int appointment_id;
    @JsonProperty("id")
    public int id;
    @JsonProperty("state")
    public int state;
    @JsonProperty("type")
    public int type;


    @Override
    public String toString() {
        return "drug_orders{id=" + id + ",appointment_id=" + appointment_id + ",state=" + state + ",type=" + type + "}";
    }


}
