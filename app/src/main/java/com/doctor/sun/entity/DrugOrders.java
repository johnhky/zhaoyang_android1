package com.doctor.sun.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.vm.BaseItem;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Created by heky on 17/4/25.
 */

public class DrugOrders extends BaseItem implements Parcelable {


    @JsonProperty("appointment_id")
    private int appointment_id;
    @JsonProperty("id")
    private int id;
    @JsonProperty("state")
    private int state;
    @JsonProperty("type")
    private int type;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(int appointment_id) {
        this.appointment_id = appointment_id;
    }


    @Override
    public String toString() {
        return "drug_orders{id="+id+",appointment_id="+appointment_id+",state="+state+",type="+type+"}";
    }

    public DrugOrders(Parcel parcel) {
        this.id = parcel.readInt();
        this.state = parcel.readInt();
        this.type = parcel.readInt();
        this.appointment_id = parcel.readInt();
    }

    public DrugOrders(){

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.appointment_id);
        dest.writeInt(this.type);
        dest.writeInt(this.state);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DrugOrders> CREATOR = new Creator<DrugOrders>() {
        @Override
        public DrugOrders createFromParcel(Parcel source) {
            return new DrugOrders(source);
        }

        @Override
        public DrugOrders[] newArray(int size) {
            return new DrugOrders[size];
        }
    };
}
