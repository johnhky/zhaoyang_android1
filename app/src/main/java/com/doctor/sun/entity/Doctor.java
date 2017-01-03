package com.doctor.sun.entity;import android.databinding.Bindable;import android.os.Parcel;import android.os.Parcelable;import com.doctor.sun.BR;import com.doctor.sun.R;import com.doctor.sun.entity.handler.DoctorHandler;import com.doctor.sun.util.NameComparator;import com.doctor.sun.vm.BaseItem;import com.fasterxml.jackson.annotation.JsonIgnore;import com.fasterxml.jackson.annotation.JsonProperty;import java.util.ArrayList;import java.util.List;import java.util.Locale;import java.util.Map;/** * Created by rick on 11/17/15. */public class Doctor extends BaseItem implements Parcelable, NameComparator.Name {    //    public static final String STATUS_REJECTED = "rejected";    public static final int MALE = 1;    public static final int FEMALE = 2;    public boolean isSelected = false;    /**     * attitude_point : 5.00     * professional_point : 5.00     * punctuality_point : 5.00     */    @JsonProperty("attitude_point")    private float attitudePoint;    @JsonProperty("professional_point")    private float professionalPoint;    @JsonProperty("punctuality_point")    private float punctualityPoint;    /**     */    /*     * age :     * is_fav : 1(0未收藏 1已收藏该医生)     * id : 1     * avatar : http://p.3761.com/pic/71271413852950.jpg     * name : 新医生     * email : waymen@ganguo.hk     * gender : 1     * hospital_id : 8     * specialist : 妇科     * hospital_phone :     * title : 主任医师     * title_img :     * practitioner_img :     * certified_img :     * detail :     * hospital_name : 新私人诊所     * voipAccount : 88797700000028     * phone : 15917748280     * status : pending (pending审核中 pass审核通过 reject审核不通过 空字符串的话就代表是还未提交过审核)     * level : 执业医师认证     * city :     * money : 1     * second_money : 1     * need_review : 0     * point : 4.1     * yunxin_accid : 24     */    @JsonProperty("age")    private String birthday;    @JsonProperty("is_fav")    private String isFav = "";    @JsonProperty("id")    private int id = -1;    @JsonProperty("doctor_id")    private int doctorId = -1;    @JsonProperty("avatar")    private String avatar;    @JsonProperty("name")    private String name;    @JsonProperty("email")    private String email;    @JsonProperty("gender")    private int gender = -1;    @JsonProperty("hospital_id")    private int hospitalId;    @JsonProperty("specialist")    private String specialist;    @JsonProperty("hospital_phone")    private String hospitalPhone;    @JsonProperty("title")    private String title;    @JsonProperty("title_img")    private String titleImg;    @JsonProperty("practitioner_img")    private String practitionerImg;    @JsonProperty("certified_img")    private String certifiedImg;    @JsonProperty("detail")    private String detail;    @JsonProperty("hospital_name")    private String hospitalName;    @JsonProperty("voipAccount")    private String voipAccount;    @JsonProperty("phone")    private String phone;    @JsonProperty("review_status")    private String reviewStatus = "";    @JsonProperty("level")    private String level;    @JsonProperty("city")    private String city;    //专属咨询金额    @JsonProperty("money")    private double money;    //简捷就诊金额    @JsonProperty("second_money")    private double secondMoney;    @JsonProperty("point")    private float point;    @JsonProperty("follow_up_permission")    private int followUpPermission;    @JsonProperty("apply_id")    public String applyId;    @JsonProperty("tags")    public List<Tags> tags = new ArrayList<>();    @JsonProperty("yunxin_accid")    private String yunxinAccid;    @JsonProperty("tid")    private String tid;    @JsonProperty("is_recommend")    private String isRecommend;    @JsonProperty("is_ban")    private String isBan;    @JsonProperty("hide")    private int hide = 0;    public int getDoctorId() {        return doctorId;    }    public void setDoctorId(int doctorId) {        this.doctorId = doctorId;    }    public void setId(int id) {        this.id = id;    }    public void setAvatar(String avatar) {        this.avatar = avatar;    }    public void setName(String name) {        this.name = name;    }    public void setEmail(String email) {        this.email = email;    }    public void setGender(int gender) {        this.gender = gender;    }    public void setHospitalId(int hospitalId) {        this.hospitalId = hospitalId;    }    public void setSpecialist(String specialist) {        this.specialist = specialist;    }    public void setHospitalPhone(String hospitalPhone) {        this.hospitalPhone = hospitalPhone;    }    public void setTitle(String title) {        this.title = title;    }    public void setTitleImg(String titleImg) {        this.titleImg = titleImg;    }    public void setPractitionerImg(String practitionerImg) {        this.practitionerImg = practitionerImg;    }    public void setCertifiedImg(String certifiedImg) {        this.certifiedImg = certifiedImg;    }    public void setDetail(String detail) {        this.detail = detail;    }    public void setHospitalName(String hospitalName) {        this.hospitalName = hospitalName;    }    public void setVoipAccount(String voipAccount) {        this.voipAccount = voipAccount;    }    public void setPhone(String phone) {        this.phone = phone;    }    public void setReviewStatus(String reviewStatus) {        this.reviewStatus = reviewStatus;    }    public int getId() {        if (id == -1) {            return doctorId;        }        return id;    }    public String getAvatar() {        return avatar;    }    public String getName() {        return name;    }    public String getEmail() {        return email;    }    public int getGender() {        return gender;    }    public int getHospitalId() {        return hospitalId;    }    public String getSpecialist() {        return specialist;    }    public String getHospitalPhone() {        return hospitalPhone;    }    public String getTitle() {        return title;    }    public String getTitleImg() {        return titleImg;    }    public String getPractitionerImg() {        return practitionerImg;    }    public String getCertifiedImg() {        return certifiedImg;    }    public String getDetail() {        return detail;    }    public String getHospitalName() {        return hospitalName;    }    public String getVoipAccount() {        return voipAccount;    }    public String getPhone() {        return semiHidePhoneNum(phone);    }    public String getReviewStatus() {        return reviewStatus;    }    public Doctor() {    }    public void setLevel(String level) {        this.level = level;    }    public void setCity(String city) {        this.city = city;    }    public void setMoney(double money) {        this.money = money;    }    public void setSecondMoney(double secondMoney) {        this.secondMoney = secondMoney;    }    public void setPoint(float point) {        this.point = point;    }    public String getLevel() {        return level;    }    public String getCity() {        return city;    }    public double getMoney() {        return money;    }    public double getSecondMoney() {        return secondMoney;    }    public float getPoint() {        return point;    }    public boolean isSelected() {        return isSelected;    }    public void setIsSelected(boolean isSelected) {        this.isSelected = isSelected;    }    public int getHide() {        return hide;    }    public void setHide(int hide) {        this.hide = hide;    }    @Override    @JsonIgnore    public int getItemLayoutId() {        if (super.getItemLayoutId() == -1) {            return R.layout.item_doctor;        } else {            return super.getItemLayoutId();        }    }    @JsonIgnore    public DoctorHandler getHandler() {        return new DoctorHandler(this);    }    public void setBirthday(String birthday) {        this.birthday = birthday;    }    public void setIsFav(String isFav) {        this.isFav = isFav;    }    public String getBirthday() {        return birthday;    }    public String getIsFav() {        return isFav;    }    public String getYunxinAccid() {        return yunxinAccid;    }    public void setYunxinAccid(String yunxinAccid) {        this.yunxinAccid = yunxinAccid;    }    public String getTid() {        return tid;    }    public void setTid(String tid) {        this.tid = tid;    }    public int getFollowUpPermission() {        return followUpPermission;    }    public void setFollowUpPermission(int followUpPermission) {        this.followUpPermission = followUpPermission;    }    public String getIsRecommend() {        return isRecommend;    }    public void setIsRecommend(String isRecommend) {        this.isRecommend = isRecommend;    }    public String getIsBan() {        return isBan;    }    public void setIsBan(String isBan) {        this.isBan = isBan;    }    @Bindable    public float getAttitudePoint() {        return attitudePoint;    }    public void setAttitudePoint(float attitudePoint) {        this.attitudePoint = attitudePoint;        notifyPropertyChanged(BR.attitudePoint);    }    @Bindable    public float getProfessionalPoint() {        return professionalPoint;    }    public void setProfessionalPoint(float professionalPoint) {        this.professionalPoint = professionalPoint;        notifyPropertyChanged(BR.professionalPoint);    }    @Bindable    public float getPunctualityPoint() {        return punctualityPoint;    }    public void setPunctualityPoint(float punctualityPoint) {        this.punctualityPoint = punctualityPoint;        notifyPropertyChanged(BR.punctualityPoint);    }    public String getAveragePoint() {        float averagePoint = (getProfessionalPoint() + getAttitudePoint() + getPunctualityPoint()) / 3;        return String.format(Locale.CHINA, "%.1f", averagePoint);    }    @JsonIgnore    public int getLevelBackgroundColor() {        return R.drawable.stroke_rect_blue;    }    @Override    public String toString() {        return "Doctor{" +                "isSelected=" + isSelected +                ", attitudePoint=" + attitudePoint +                ", professionalPoint=" + professionalPoint +                ", punctualityPoint=" + punctualityPoint +                ", birthday='" + birthday + '\'' +                ", isFav='" + isFav + '\'' +                ", id=" + id +                ", doctorId=" + doctorId +                ", avatar='" + avatar + '\'' +                ", name='" + name + '\'' +                ", email='" + email + '\'' +                ", gender=" + gender +                ", hospitalId=" + hospitalId +                ", specialist='" + specialist + '\'' +                ", hospitalPhone='" + hospitalPhone + '\'' +                ", title='" + title + '\'' +                ", titleImg='" + titleImg + '\'' +                ", practitionerImg='" + practitionerImg + '\'' +                ", certifiedImg='" + certifiedImg + '\'' +                ", detail='" + detail + '\'' +                ", hospitalName='" + hospitalName + '\'' +                ", voipAccount='" + voipAccount + '\'' +                ", phone='" + phone + '\'' +                ", reviewStatus='" + reviewStatus + '\'' +                ", level='" + level + '\'' +                ", city='" + city + '\'' +                ", money=" + money +                ", secondMoney=" + secondMoney +                ", point=" + point +                ", followUpPermission=" + followUpPermission +                ", applyId='" + applyId + '\'' +                ", tags=" + tags +                ", yunxinAccid='" + yunxinAccid + '\'' +                ", tid='" + tid + '\'' +                ", isRecommend='" + isRecommend + '\'' +                ", isBan='" + isBan + '\'' +                ", hide=" + hide +                '}';    }    public void fromHashMap(Map<String, String> map) {        birthday = map.get("age");        isFav = map.get("is_fav");        avatar = map.get("avatar");        name = map.get("name");        email = map.get("email");        try {            id = Integer.parseInt(map.get("id"));            gender = Integer.parseInt(map.get("gender"));            hospitalId = Integer.parseInt(map.get("hospital_id"));            money = Double.parseDouble(map.get("money"));            secondMoney = Double.parseDouble(map.get("second_money"));            point = Float.parseFloat(map.get("point"));            hide = Integer.parseInt(map.get("hide"));        } catch (Exception ignored) {        }        specialist = map.get("specialist");        hospitalPhone = map.get("hospital_phone");        title = map.get("title");        titleImg = map.get("title_img");        practitionerImg = map.get("practitioner_img");        certifiedImg = map.get("certified_img");        detail = map.get("detail");        hospitalName = map.get("hospital_name");        voipAccount = map.get("voipAccount");        phone = map.get("phone");        reviewStatus = map.get("status");        level = map.get("level");        city = map.get("city");        yunxinAccid = map.get("yunxin_accid");        tid = map.get("tid");    }    @Override    public int getLayoutId() {        return getItemLayoutId();    }    @Override    public String getKey() {        return String.valueOf(getId());    }    @Override    public int describeContents() {        return 0;    }    @Override    public void writeToParcel(Parcel dest, int flags) {        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);        dest.writeFloat(this.attitudePoint);        dest.writeFloat(this.professionalPoint);        dest.writeFloat(this.punctualityPoint);        dest.writeString(this.birthday);        dest.writeString(this.isFav);        dest.writeInt(this.id);        dest.writeInt(this.doctorId);        dest.writeString(this.avatar);        dest.writeString(this.name);        dest.writeString(this.email);        dest.writeInt(this.gender);        dest.writeInt(this.hospitalId);        dest.writeString(this.specialist);        dest.writeString(this.hospitalPhone);        dest.writeString(this.title);        dest.writeString(this.titleImg);        dest.writeString(this.practitionerImg);        dest.writeString(this.certifiedImg);        dest.writeString(this.detail);        dest.writeString(this.hospitalName);        dest.writeString(this.voipAccount);        dest.writeString(this.phone);        dest.writeString(this.reviewStatus);        dest.writeString(this.level);        dest.writeString(this.city);        dest.writeDouble(this.money);        dest.writeDouble(this.secondMoney);        dest.writeInt(this.hide);        dest.writeFloat(this.point);        dest.writeInt(this.followUpPermission);        dest.writeString(this.applyId);        dest.writeTypedList(this.tags);        dest.writeString(this.yunxinAccid);        dest.writeString(this.tid);        dest.writeString(this.isRecommend);        dest.writeString(this.isBan);    }    protected Doctor(Parcel in) {        this.isSelected = in.readByte() != 0;        this.attitudePoint = in.readFloat();        this.professionalPoint = in.readFloat();        this.punctualityPoint = in.readFloat();        this.birthday = in.readString();        this.isFav = in.readString();        this.id = in.readInt();        this.doctorId = in.readInt();        this.avatar = in.readString();        this.name = in.readString();        this.email = in.readString();        this.gender = in.readInt();        this.hospitalId = in.readInt();        this.specialist = in.readString();        this.hospitalPhone = in.readString();        this.title = in.readString();        this.titleImg = in.readString();        this.practitionerImg = in.readString();        this.certifiedImg = in.readString();        this.detail = in.readString();        this.hospitalName = in.readString();        this.voipAccount = in.readString();        this.phone = in.readString();        this.reviewStatus = in.readString();        this.level = in.readString();        this.city = in.readString();        this.money = in.readDouble();        this.secondMoney = in.readDouble();        this.hide = in.readInt();        this.point = in.readFloat();        this.followUpPermission = in.readInt();        this.applyId = in.readString();        this.tags = in.createTypedArrayList(Tags.CREATOR);        this.yunxinAccid = in.readString();        this.tid = in.readString();        this.isRecommend = in.readString();        this.isBan = in.readString();    }    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {        @Override        public Doctor createFromParcel(Parcel source) {            return new Doctor(source);        }        @Override        public Doctor[] newArray(int size) {            return new Doctor[size];        }    };    private String semiHidePhoneNum(String originPhone) {        if (originPhone == null || "".equals(originPhone)) {            return "";        }        return originPhone.substring(0, 3) + "****" + originPhone.substring(7);    }}