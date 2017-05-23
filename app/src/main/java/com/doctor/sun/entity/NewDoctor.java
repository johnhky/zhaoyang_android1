package com.doctor.sun.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.R;
import com.doctor.sun.entity.handler.NewDoctorHandler;
import com.doctor.sun.vm.BaseItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by heky on 17/5/12.
 */

public class NewDoctor extends BaseItem implements Parcelable {

    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("clinic_address")
    private ClinicAddress clinic_address;
    @JsonProperty("detail")
    private String detail;
    @JsonProperty("email")
    private String email;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("hospital_id")
    private String hospital_id;
    @JsonProperty("hospital_name")
    private String hospital_name;
    @JsonProperty("hospital_phone")
    private String hospitol_phone;
    @JsonProperty("id")
    private String id;
    @JsonProperty("is_open")
    private ISOpen is_open;
    @JsonProperty("level")
    private String level;
    @JsonProperty("log_time")
    private String log_time;
    @JsonProperty("min_time")
    private String min_time;
    @JsonProperty("money")
    private String money;
    @JsonProperty("multiple")
    private String multiple;
    @JsonProperty("name")
    private String name;
    @JsonProperty("network_minute")
    private String network_minute;
    @JsonProperty("point")
    private String point;
    @JsonProperty("second_money")
    private String second_money;
    @JsonProperty("specialist")
    private String specialist;
    @JsonProperty("specialist_categ")
    private String specialist_categ;
    @JsonProperty("surface_minute")
    private String surface_minute;
    @JsonProperty("surface_money")
    private String surface_money;
    @JsonProperty("title")
    private String titles;
    @JsonProperty("user_type")
    private String user_type;
    @JsonProperty("voip_account")
    private String voip_account;
    @JsonProperty("yunxin_accid")
    private String yunxin_accid;

    @JsonIgnore
    private boolean hasSharedTransition = true;

    @Override
    @JsonIgnore
    public int getItemLayoutId() {
        if (super.getItemLayoutId() == -1) {
            return R.layout.item_doctor;
        } else {
            return super.getItemLayoutId();
        }

    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public ClinicAddress getClinic_address() {
        return clinic_address;
    }

    public void setClinic_address(ClinicAddress clinic_address) {
        this.clinic_address = clinic_address;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getHospitol_phone() {
        return hospitol_phone;
    }

    public void setHospitol_phone(String hospitol_phone) {
        this.hospitol_phone = hospitol_phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ISOpen getIs_open() {
        return is_open;
    }

    public void setIs_open(ISOpen is_open) {
        this.is_open = is_open;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLog_time() {
        return log_time;
    }

    public void setLog_time(String log_time) {
        this.log_time = log_time;
    }

    public String getMin_time() {
        return min_time;
    }

    public void setMin_time(String min_time) {
        this.min_time = min_time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNetwork_minute() {
        return network_minute;
    }

    public void setNetwork_minute(String network_minute) {
        this.network_minute = network_minute;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getSecond_money() {
        return second_money;
    }

    public void setSecond_money(String second_money) {
        this.second_money = second_money;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getSpecialist_categ() {
        return specialist_categ;
    }

    public void setSpecialist_categ(String specialist_categ) {
        this.specialist_categ = specialist_categ;
    }

    public String getSurface_minute() {
        return surface_minute;
    }

    public void setSurface_minute(String surface_minute) {
        this.surface_minute = surface_minute;
    }

    public String getSurface_money() {
        return surface_money;
    }

    public void setSurface_money(String surface_money) {
        this.surface_money = surface_money;
    }

    public String getTitle() {
        return titles;
    }

    public void setTitle(String title) {
        this.titles = title;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getVoip_account() {
        return voip_account;
    }

    public void setVoip_account(String voip_account) {
        this.voip_account = voip_account;
    }

    public String getYunxin_accid() {
        return yunxin_accid;
    }

    public void setYunxin_accid(String yunxin_accid) {
        this.yunxin_accid = yunxin_accid;
    }

    @JsonIgnore
    public NewDoctorHandler getHandler() {
        NewDoctorHandler handler = new NewDoctorHandler(this);
        handler.hasSharedTransition(true);
        return handler;
    }
    @JsonIgnore
    public int getLevelBackgroundColor() {
        return R.drawable.stroke_rect_blue;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avatar);
        dest.writeString(this.birthday);
        dest.writeString(this.detail);
        dest.writeString(this.email);
        dest.writeString(this.gender);
        dest.writeString(this.hospital_id);
        dest.writeString(this.hospital_name);
        dest.writeString(this.hospitol_phone);
        dest.writeString(this.id);
        dest.writeString(this.level);
        dest.writeString(this.log_time);
        dest.writeString(this.min_time);
        dest.writeString(this.money);
        dest.writeString(this.multiple);
        dest.writeString(this.name);
        dest.writeString(this.network_minute);
        dest.writeString(this.point);
        dest.writeString(this.second_money);
        dest.writeString(this.specialist);
        dest.writeString(this.specialist_categ);
        dest.writeString(this.surface_minute);
        dest.writeString(this.surface_money);
        dest.writeString(this.titles);
        dest.writeString(this.yunxin_accid);
        dest.writeString(this.voip_account);
        dest.writeString(this.user_type);
    }

    public NewDoctor(Parcel parcel){
       this.avatar = parcel.readString();
        this.birthday = parcel.readString();
        this.detail = parcel.readString();
        this.email = parcel.readString();
        this.gender = parcel.readString();
        this.hospital_id = parcel.readString();
        this.hospital_name = parcel.readString();
        this.hospitol_phone = parcel.readString();
        this.id = parcel.readString();
        this.level = parcel.readString();
        this.log_time = parcel.readString();
        this.min_time = parcel.readString();
        this.money = parcel.readString();
        this.multiple = parcel.readString();
        this.name = parcel.readString();
        this.network_minute = parcel.readString();
        this.point = parcel.readString();
        this.second_money = parcel.readString();
        this.specialist = parcel.readString();
        this.specialist_categ = parcel.readString();
        this.surface_minute = parcel.readString();
        this.surface_money = parcel.readString();
        this.titles = parcel.readString();
        this.user_type = parcel.readString();
        this.voip_account = parcel.readString();
        this.yunxin_accid = parcel.readString();
    }

    public static final Creator<NewDoctor> CREATOR  = new Creator<NewDoctor>() {
        @Override
        public NewDoctor createFromParcel(Parcel source) {
            return new NewDoctor(source);
        }

        @Override
        public NewDoctor[] newArray(int size) {
            return new NewDoctor[0];
        }
    };

    public void disableSharedTransition() {
        hasSharedTransition = false;
    }
}
