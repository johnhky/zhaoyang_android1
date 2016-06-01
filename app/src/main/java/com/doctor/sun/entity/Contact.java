package com.doctor.sun.entity;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.ui.activity.patient.DoctorInfoActivity;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.widget.SelectDialog;
import com.doctor.sun.util.NameComparator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Lynn on 12/29/15.
 */
public class Contact implements LayoutId, Parcelable, NameComparator.Name {
    @JsonProperty("patient_id")
    private int patientId = -1;
    @JsonProperty("name")
    private String name;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("voipAccount")
    private String voipAccount;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("medical_records")
    private List<MedicalRecord> medicalRecords;
    /**
     * doctor_id : 25
     */
    @JsonProperty("doctor_id")
    private int doctorId = -1;

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getVoipAccount() {
        return voipAccount;
    }

    public void setVoipAccount(String voipAccount) {
        this.voipAccount = voipAccount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "patientId=" + patientId +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", voipAccount='" + voipAccount + '\'' +
                ", phone='" + phone + '\'' +
                ", medicalRecords=" + medicalRecords +
                '}';
    }


    public Contact() {
    }


    @Override
    public int getItemLayoutId() {
        return R.layout.item_contact;
    }

    public void itemClick(View view) {
        if (patientId != -1) {
            SelectDialog.showSelectDialog(view.getContext(), getPatientId());
        } else if (doctorId != -1) {
            Intent intent = DoctorInfoActivity.makeIntent(view.getContext(), this);
            view.getContext().startActivity(intent);
        }
    }

    public View.OnClickListener applyAfterService() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.patientId);
        dest.writeString(this.name);
        dest.writeString(this.nickname);
        dest.writeString(this.avatar);
        dest.writeString(this.voipAccount);
        dest.writeString(this.phone);
        dest.writeTypedList(medicalRecords);
        dest.writeInt(this.doctorId);
    }

    protected Contact(Parcel in) {
        this.patientId = in.readInt();
        this.name = in.readString();
        this.nickname = in.readString();
        this.avatar = in.readString();
        this.voipAccount = in.readString();
        this.phone = in.readString();
        this.medicalRecords = in.createTypedArrayList(MedicalRecord.CREATOR);
        this.doctorId = in.readInt();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
