package com.doctor.sun.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Created by rick on 12/10/15.
 * 转诊,复诊信息
 */
public class ReturnInfo implements Parcelable {

    /**
     * need_return : 0
     * return_paid : 0
     * return_appointment_id : 169
     */

    @JsonProperty("need_return")
    private int needReturn;
    @JsonProperty("return_paid")
    private int returnPaid;
    @JsonProperty("return_appointment_id")
    private int returnAppointmentId;

    public void setNeedReturn(int needReturn) {
        this.needReturn = needReturn;
    }

    public void setReturnPaid(int returnPaid) {
        this.returnPaid = returnPaid;
    }

    public void setReturnAppointmentId(int returnAppointmentId) {
        this.returnAppointmentId = returnAppointmentId;
    }

    public int getNeedReturn() {
        return needReturn;
    }

    public int getReturnPaid() {
        return returnPaid;
    }

    public int getReturnAppointmentId() {
        return returnAppointmentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.needReturn);
        dest.writeInt(this.returnPaid);
        dest.writeInt(this.returnAppointmentId);
    }

    public ReturnInfo() {
    }

    protected ReturnInfo(Parcel in) {
        this.needReturn = in.readInt();
        this.returnPaid = in.readInt();
        this.returnAppointmentId = in.readInt();
    }

    public static final Creator<ReturnInfo> CREATOR = new Creator<ReturnInfo>() {
        public ReturnInfo createFromParcel(Parcel source) {
            return new ReturnInfo(source);
        }

        public ReturnInfo[] newArray(int size) {
            return new ReturnInfo[size];
        }
    };
}
