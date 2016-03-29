package com.doctor.sun.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

import java.util.HashMap;

/**
 * Created by lucas on 1/7/16.
 */
public class DoctorDTO  implements Parcelable, LayoutId {
    private Doctor doctor;
    public boolean isSelected = false;

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    

    public void setPoint(float point) {
        doctor.setPoint(point);
    }

    public String getHospitalName() {
        return doctor.getHospitalName();
    }

    public String getDetail() {
        return doctor.getDetail();
    }

    public void setId(int id) {
        doctor.setId(id);
    }

    public void setAvatar(String avatar) {
        doctor.setAvatar(avatar);
    }

    public String getLevel() {
        return doctor.getLevel();
    }

    public String getHospitalPhone() {
        return doctor.getHospitalPhone();
    }

    public void setPhone(String phone) {
        doctor.setPhone(phone);
    }

    public int getSecondMoney() {
        return doctor.getSecondMoney();
    }

    public void setMoney(int money) {
        doctor.setMoney(money);
    }

    public String getStatus() {
        return doctor.getStatus();
    }

    @Override
    public int getItemLayoutId() {
        return doctor.getItemLayoutId();
    }

    public void setHospitalName(String hospitalName) {
        doctor.setHospitalName(hospitalName);
    }

    public void setCertifiedImg(String certifiedImg) {
        doctor.setCertifiedImg(certifiedImg);
    }

    public void setTitle(String title) {
        doctor.setTitle(title);
    }

    public void setVoipAccount(String voipAccount) {
        doctor.setVoipAccount(voipAccount);
    }

    public void setHospitalId(int hospitalId) {
        doctor.setHospitalId(hospitalId);
    }

    public void setPractitionerImg(String practitionerImg) {
        doctor.setPractitionerImg(practitionerImg);
    }

    public void itemClick(View v) {
        doctor.itemClick(v);
    }

    public int getHospitalId() {
        return doctor.getHospitalId();
    }

    public void setSpecialist(String specialist) {
        doctor.setSpecialist(specialist);
    }

    public void setName(String name) {
        doctor.setName(name);
    }

    public void setNeedReview(int needReview) {
        doctor.setNeedReview(needReview);
    }

    public String getEmail() {
        return doctor.getEmail();
    }

    public void setDetail(String detail) {
        doctor.setDetail(detail);
    }

    public String getTitleImg() {
        return doctor.getTitleImg();
    }

    public String getCertifiedImg() {
        return doctor.getCertifiedImg();
    }

    public String getPractitionerImg() {
        return doctor.getPractitionerImg();
    }

    public String getLocate() {
        return doctor.getLocate();
    }

    public String getName() {
        return doctor.getName();
    }

    public String getFee(@Appointment.Type int type) {
        return doctor.getFee(type);
    }

    public String getTitle() {
        return doctor.getTitle();
    }

    public int getNeedReview() {
        return doctor.getNeedReview();
    }

    public String getCity() {
        return doctor.getCity();
    }

    public void setEmail(String email) {
        doctor.setEmail(email);
    }

    public void setHospitalPhone(String hospitalPhone) {
        doctor.setHospitalPhone(hospitalPhone);
    }

    public String getAvatar() {
        return doctor.getAvatar();
    }

    public void setCity(String city) {
        doctor.setCity(city);
    }

    public float getPoint() {
        return doctor.getPoint();
    }

    public void setStatus(String status) {
        doctor.setStatus(status);
    }

    public void setGender(int gender) {
        doctor.setGender(gender);
    }

    public String getPhone() {
        return doctor.getPhone();
    }

    public int getGender() {
        return doctor.getGender();
    }

    public String getSpecialist() {
        return doctor.getSpecialist();
    }

    public String getSpecial() {
        return doctor.getSpecial();
    }

    public String getVoipAccount() {
        return doctor.getVoipAccount();
    }

    public int getMoney() {
        return doctor.getMoney();
    }

    public int getId() {
        return doctor.getId();
    }

    public void setSecondMoney(int secondMoney) {
        doctor.setSecondMoney(secondMoney);
    }

    public void setTitleImg(String titleImg) {
        doctor.setTitleImg(titleImg);
    }

    public HashMap<String, String> toParams() {
        return doctor.toHashMap();
    }

    public void setLevel(String level) {
        doctor.setLevel(level);
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.doctor, 0);
        dest.writeByte(isSelected ? (byte) 1 : (byte) 0);
    }

    public DoctorDTO() {
    }

    protected DoctorDTO(Parcel in) {
        this.doctor = in.readParcelable(Doctor.class.getClassLoader());
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<DoctorDTO> CREATOR = new Creator<DoctorDTO>() {
        public DoctorDTO createFromParcel(Parcel source) {
            return new DoctorDTO(source);
        }

        public DoctorDTO[] newArray(int size) {
            return new DoctorDTO[size];
        }
    };
}
