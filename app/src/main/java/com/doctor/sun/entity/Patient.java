package com.doctor.sun.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.ui.activity.patient.handler.EditPatientHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by lucas on 1/4/16.
 */
public class Patient implements Parcelable {

    /**
     * id : 11
     * name : 大明
     * email : waymen@ganguo.hk
     * gender : 1
     * birthday : 1991-01
     * avatar : https://trello-avatars.s3.amazonaws.com/eb8345770e0fd6183d370fc3e2b1f1d3/30.png
     * point : 1
     * voipAccount : 88797700000050
     * phone : 11118748284
     */

    @JsonProperty("id")
    private int id;

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
    private int point;
    @JsonProperty("voipAccount")
    private String voipAccount;
    @JsonProperty("phone")
    private String phone;

    protected Patient(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.email = in.readString();
        this.gender = in.readInt();
        this.birthday = in.readString();
        this.avatar = in.readString();
        this.point = in.readInt();
        this.voipAccount = in.readString();
        this.phone = in.readString();
    }

    public Patient() {
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

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

    public void setPoint(int point) {
        this.point = point;
    }

    public void setVoipAccount(String voipAccount) {
        this.voipAccount = voipAccount;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
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
        return point;
    }

    public String getVoipAccount() {
        return voipAccount;
    }

    public String getPhone() {
        return phone;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeInt(gender);
        dest.writeString(birthday);
        dest.writeString(avatar);
        dest.writeInt(point);
        dest.writeString(voipAccount);
        dest.writeString(phone);
    }

    @JsonIgnore
    public String getGenderResult() {
        String result = "";
        switch (getGender()) {
            case 1:
                result = "男";
                break;
            case 2:
                result = "女";
                break;
        }
        return result;
    }

    @JsonIgnore
    private EditPatientHandler handler = new EditPatientHandler(this);

    public EditPatientHandler getHandler() {
        return handler;
    }

    public void setHandler(EditPatientHandler handler) {
        this.handler = handler;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", birthday='" + birthday + '\'' +
                ", avatar='" + avatar + '\'' +
                ", point=" + point +
                ", voipAccount='" + voipAccount + '\'' +
                ", phone='" + phone + '\'' +
                ", handler=" + handler +
                '}';
    }
}
