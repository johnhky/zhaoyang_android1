package com.doctor.sun.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.R;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.entity.handler.DoctorHandler;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.util.NameComparator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Created by rick on 11/17/15.
 */
public class Doctor implements LayoutId, Parcelable, NameComparator.Name {

    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_PASS = "pass";
    public static final String STATUS_REJECT = "reject";
    public static final int MALE = 1;
    public static final int FEMALE = 2;
    public boolean isSelected = false;
    /**
     */


    /**
     * birthday :
     * is_fav : 1(0未收藏 1已收藏该医生)
     * id : 1
     * avatar : http://p.3761.com/pic/71271413852950.jpg
     * name : 新医生
     * email : waymen@ganguo.hk
     * gender : 1
     * hospital_id : 8
     * specialist : 妇科
     * hospital_phone :
     * title : 主任医师
     * title_img :
     * practitioner_img :
     * certified_img :
     * detail :
     * hospital_name : 新私人诊所
     * voipAccount : 88797700000028
     * phone : 15917748280
     * status : pending (pending审核中 pass审核通过 reject审核不通过 空字符串的话就代表是还未提交过审核)
     * level : 执业医师认证
     * city :
     * money : 1
     * second_money : 1
     * need_review : 0
     * point : 4.1
     * yunxin_accid : 24
     */
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("is_fav")
    private String isFav;
    @JsonProperty("id")
    private int id;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("gender")
    private int gender;
    @JsonProperty("hospital_id")
    private int hospitalId;
    @JsonProperty("specialist")
    private String specialist;
    @JsonProperty("hospital_phone")
    private String hospitalPhone;
    @JsonProperty("title")
    private String title;
    @JsonProperty("title_img")
    private String titleImg;
    @JsonProperty("practitioner_img")
    private String practitionerImg;
    @JsonProperty("certified_img")
    private String certifiedImg;
    @JsonProperty("detail")
    private String detail;
    @JsonProperty("hospital_name")
    private String hospitalName;
    @JsonProperty("voipAccount")
    private String voipAccount;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("status")
    private String status;
    @JsonProperty("level")
    private String level;
    @JsonProperty("city")
    private String city;
    //详细就诊金额
    @JsonProperty("money")
    private int money;
    //简捷就诊金额
    @JsonProperty("second_money")
    private int secondMoney;
    @JsonProperty("need_review")
    private int needReview;
    @JsonProperty("point")
    private float point;
    // 预约相关信息
    @JsonIgnore
    private String recordId;
    @JsonIgnore
    private String duration;
    @AppointmentType
    @JsonIgnore
    private int type = AppointmentType.DETAIL;
    @JsonProperty("yunxin_accid")
    private String yunxinAccid;
    @JsonProperty("tid")
    private String tid;

    public void setId(int id) {
        this.id = id;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public void setHospitalPhone(String hospitalPhone) {
        this.hospitalPhone = hospitalPhone;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }

    public void setPractitionerImg(String practitionerImg) {
        this.practitionerImg = practitionerImg;
    }

    public void setCertifiedImg(String certifiedImg) {
        this.certifiedImg = certifiedImg;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public void setVoipAccount(String voipAccount) {
        this.voipAccount = voipAccount;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
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

    public int getHospitalId() {
        return hospitalId;
    }

    public String getSpecialist() {
        return specialist;
    }

    public String getHospitalPhone() {
        return hospitalPhone;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleImg() {
        return titleImg;
    }

    public String getPractitionerImg() {
        return practitionerImg;
    }

    public String getCertifiedImg() {
        return certifiedImg;
    }

    public String getDetail() {
        return detail;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getVoipAccount() {
        return voipAccount;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public Doctor() {
    }


    public void setLevel(String level) {
        this.level = level;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setSecondMoney(int secondMoney) {
        this.secondMoney = secondMoney;
    }

    public void setNeedReview(int needReview) {
        this.needReview = needReview;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    public String getLevel() {
        return level;
    }

    public String getCity() {
        return city;
    }

    public int getMoney() {
        return money;
    }

    public int getSecondMoney() {
        return secondMoney;
    }

    public int getNeedReview() {
        return needReview;
    }

    public float getPoint() {
        return point;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    @JsonIgnore
    public int getItemLayoutId() {
        return R.layout.item_doctor;
    }

    @JsonIgnore
    public DoctorHandler getHandler() {
        return new DoctorHandler(this);
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setIsFav(String isFav) {
        this.isFav = isFav;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getIsFav() {
        return isFav;
    }

    public String getYunxinAccid() {
        return yunxinAccid;
    }

    public void setYunxinAccid(String yunxinAccid) {
        this.yunxinAccid = yunxinAccid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(isSelected ? (byte) 1 : (byte) 0);
        dest.writeString(this.birthday);
        dest.writeString(this.isFav);
        dest.writeInt(this.id);
        dest.writeString(this.avatar);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeInt(this.gender);
        dest.writeInt(this.hospitalId);
        dest.writeString(this.specialist);
        dest.writeString(this.hospitalPhone);
        dest.writeString(this.title);
        dest.writeString(this.titleImg);
        dest.writeString(this.practitionerImg);
        dest.writeString(this.certifiedImg);
        dest.writeString(this.detail);
        dest.writeString(this.hospitalName);
        dest.writeString(this.voipAccount);
        dest.writeString(this.phone);
        dest.writeString(this.status);
        dest.writeString(this.level);
        dest.writeString(this.city);
        dest.writeInt(this.money);
        dest.writeInt(this.secondMoney);
        dest.writeInt(this.needReview);
        dest.writeFloat(this.point);
        dest.writeString(this.recordId);
        dest.writeString(this.duration);
        dest.writeInt(this.type);
        dest.writeString(this.yunxinAccid);
        dest.writeString(this.tid);
    }

    protected Doctor(Parcel in) {
        this.isSelected = in.readByte() != 0;
        this.birthday = in.readString();
        this.isFav = in.readString();
        this.id = in.readInt();
        this.avatar = in.readString();
        this.name = in.readString();
        this.email = in.readString();
        this.gender = in.readInt();
        this.hospitalId = in.readInt();
        this.specialist = in.readString();
        this.hospitalPhone = in.readString();
        this.title = in.readString();
        this.titleImg = in.readString();
        this.practitionerImg = in.readString();
        this.certifiedImg = in.readString();
        this.detail = in.readString();
        this.hospitalName = in.readString();
        this.voipAccount = in.readString();
        this.phone = in.readString();
        this.status = in.readString();
        this.level = in.readString();
        this.city = in.readString();
        this.money = in.readInt();
        this.secondMoney = in.readInt();
        this.needReview = in.readInt();
        this.point = in.readFloat();
        this.recordId = in.readString();
        this.duration = in.readString();
        //noinspection WrongConstant
        this.type = in.readInt();
        this.yunxinAccid = in.readString();
        this.tid = in.readString();
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        public Doctor createFromParcel(Parcel source) {
            return new Doctor(source);
        }

        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    @Override
    public String toString() {
        return "Doctor{" +
                "isSelected=" + isSelected +
                ", birthday='" + birthday + '\'' +
                ", isFav='" + isFav + '\'' +
                ", id=" + id +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", hospitalId=" + hospitalId +
                ", specialist='" + specialist + '\'' +
                ", hospitalPhone='" + hospitalPhone + '\'' +
                ", title='" + title + '\'' +
                ", titleImg='" + titleImg + '\'' +
                ", practitionerImg='" + practitionerImg + '\'' +
                ", certifiedImg='" + certifiedImg + '\'' +
                ", detail='" + detail + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", voipAccount='" + voipAccount + '\'' +
                ", phone='" + phone + '\'' +
                ", status='" + status + '\'' +
                ", level='" + level + '\'' +
                ", city='" + city + '\'' +
                ", money=" + money +
                ", secondMoney=" + secondMoney +
                ", needReview=" + needReview +
                ", point=" + point +
                ", recordId='" + recordId + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }

}
