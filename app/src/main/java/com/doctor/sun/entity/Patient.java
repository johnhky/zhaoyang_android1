package com.doctor.sun.entity;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.R;
import com.doctor.sun.ui.activity.patient.handler.PatientHandler;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.util.NameComparator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by lucas on 1/4/16.
 */
public class Patient extends BaseObservable implements LayoutId, Parcelable, NameComparator.Name {

    /**
     * status :
     * yunxin_accid : 57
     * id : 11
     * name : 大明
     * email : waymen@ganguo.hk
     * gender : 1
     * age : 1991-01
     * avatar : https://trello-avatars.s3.amazonaws.com/eb8345770e0fd6183d370fc3e2b1f1d3/30.png
     * point : 1
     * voipAccount : 88797700000050
     * phone : 11118748284
     */

    @JsonProperty("id")
    private int id = -1;
    @JsonProperty("patient_id")
    private int patientId;

    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("gender")
    private int gender;
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("point")
    private double point;
    @JsonProperty("voipAccount")
    private String voipAccount;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("yunxin_accid")
    public String yunxinAccid;
    @JsonProperty("status")
    public String status;

    @JsonProperty("recordNames")
    public List<String> recordNames;
    @JsonIgnore
    public PatientHandler handler = new PatientHandler(this);


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public void setVoipAccount(String voipAccount) {
        this.voipAccount = voipAccount;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        if (id == -1) {
            return patientId;
        }
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getPoint() {
        return (int) point;
    }

    public String getVoipAccount() {
        return voipAccount;
    }

    public String getPhone() {
        return phone;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public PatientHandler getHandler() {
        return handler;
    }

    public void setHandler(PatientHandler handler) {
        this.handler = handler;
    }

    public Patient() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.patientId);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeInt(this.gender);
        dest.writeString(this.birthday);
        dest.writeString(this.avatar);
        dest.writeDouble(this.point);
        dest.writeString(this.voipAccount);
        dest.writeString(this.phone);
        dest.writeString(this.yunxinAccid);
//        dest.writeParcelable(this.handler, flags);
    }

    protected Patient(Parcel in) {
        this.id = in.readInt();
        this.patientId = in.readInt();
        this.name = in.readString();
        this.email = in.readString();
        this.gender = in.readInt();
        this.birthday = in.readString();
        this.avatar = in.readString();
        this.point = in.readDouble();
        this.voipAccount = in.readString();
        this.phone = in.readString();
        this.yunxinAccid = in.readString();
//        this.handler = in.readParcelable(PatientHandler.class.getClassLoader());
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel source) {
            return new Patient(source);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };


    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", age='" + birthday + '\'' +
                ", avatar='" + avatar + '\'' +
                ", point=" + point +
                ", voipAccount='" + voipAccount + '\'' +
                ", phone='" + phone + '\'' +
                ", handler=" + handler +
                '}';
    }

    @JsonIgnore
    @Override
    public int getItemLayoutId() {
        return R.layout.item_patient;
    }
}
