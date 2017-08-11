package com.doctor.sun.entity;import android.content.Context;import android.content.Intent;import android.databinding.Bindable;import android.os.Bundle;import android.os.Parcel;import android.os.Parcelable;import android.text.TextUtils;import android.widget.Toast;import com.doctor.sun.BR;import com.doctor.sun.R;import com.doctor.sun.entity.handler.DoctorHandler;import com.doctor.sun.http.Api;import com.doctor.sun.http.callback.SimpleCallback;import com.doctor.sun.module.ToolModule;import com.doctor.sun.ui.activity.SingleFragmentActivity;import com.doctor.sun.ui.activity.patient.MedicineStoreActivity;import com.doctor.sun.ui.fragment.DoctorArticleFragment;import com.doctor.sun.ui.fragment.DoctorCommentFragment;import com.doctor.sun.util.NameComparator;import com.doctor.sun.vm.BaseItem;import com.fasterxml.jackson.annotation.JsonIgnore;import com.fasterxml.jackson.annotation.JsonProperty;import com.google.gson.annotations.Expose;import java.text.DecimalFormat;import java.text.NumberFormat;import java.util.ArrayList;import java.util.List;import java.util.Locale;import java.util.Map;/** * Created by rick on 11/17/15. */public class Doctor extends BaseItem implements Parcelable, NameComparator.Name {    //    public static final String STATUS_REJECTED = "rejected";    public static final int MALE = 1;    public static final int FEMALE = 2;    public boolean isSelected = false;    /**     * attitude_point : 5.00     * professional_point : 5.00     * punctuality_point : 5.00     */    @JsonProperty("attitude_point")    private float attitudePoint;    @JsonProperty("professional_point")    private float professionalPoint;    @JsonProperty("punctuality_point")    private float punctualityPoint;    /**     */    /*     * age :     * is_fav : 1(0未收藏 1已收藏该医生)     * id : 1     * avatar : http://p.3761.com/pic/71271413852950.jpg     * name : 新医生     * email : waymen@ganguo.hk     * gender : 1     * hospital_id : 8     * specialist : 妇科     * hospital_phone :     * title : 主任医师     * title_img :     * practitioner_img :     * certified_img :     * detail :     * hospital_name : 新私人诊所     * voipAccount : 88797700000028     * phone : 15917748280     * status : pending (pending审核中 pass审核通过 reject审核不通过 空字符串的话就代表是还未提交过审核)     * level : 执业医师认证     * city :     * money : 1     * second_money : 1     * need_review : 0     * point : 4.1     * yunxin_accid : 24     */    @JsonProperty("age")    private String birthday;    @JsonProperty("is_fav")    private String isFav = "";    @JsonProperty("id")    private int id = -1;    @JsonProperty("doctor_id")    private int doctorId = -1;    @JsonProperty("avatar")    private String avatar;    @JsonProperty("name")    private String name;    @JsonProperty("email")    private String email;    @JsonProperty("gender")    private int gender = -1;    @JsonProperty("hospital_id")    private int hospitalId;    @JsonProperty("specialist")    private String specialist;    @JsonProperty("hospital_phone")    private String hospitalPhone;    @Expose    @JsonProperty("title")    private String titles;    @JsonProperty("title_img")    private String titleImg;    @JsonProperty("practitioner_img")    private String practitionerImg;    @JsonProperty("certified_img")    private String certifiedImg;    @JsonProperty("detail")    private String detail;    @JsonProperty("hospital_name")    private String hospitalName;    @JsonProperty("voipAccount")    private String voipAccount;    @JsonProperty("phone")    private String phone;    @JsonProperty("review_status")    private String reviewStatus = "";    @JsonProperty("level")    private String level;    @Expose    @JsonProperty("city")    private String citys;    //专属咨询金额    @JsonProperty("money")    private double money;    //简捷就诊金额    @JsonProperty("second_money")    private double secondMoney;    @JsonProperty("specialist_categ")    private int specialistCateg;    @JsonProperty("point")    private float point;    @JsonProperty("follow_up_permission")    private int followUpPermission;    @JsonProperty("apply_id")    public String applyId;    @JsonProperty("tags")    public List<Tags> tags = new ArrayList<>();    @JsonProperty("is_open")    public ISOpen isOpen;    @JsonProperty("clinic_address")    public ClinicAddress clinicAddress;    @JsonProperty("yunxin_accid")    private String yunxinAccid;    @JsonProperty("tid")    private String tid;    @JsonProperty("is_recommend")    private String isRecommend;    @JsonProperty("is_ban")    private String isBan;    @JsonProperty("hide")    private int hide = 0;    @JsonProperty("push_enable")    public int push_enable = 0;    @JsonProperty("network_minute")    private String networkMinute;    @JsonProperty("surface_money")    public String surfaceMoney;    @JsonProperty("surface_minute")    public String surfaceMinute;    @JsonProperty("coupons")    public AppendCoupon coupons;    @JsonIgnore    private boolean hasSharedTransition = true;    public String getSurfaceMinute() {        return surfaceMinute;    }    public void setSurfaceMinute(String surfaceMinute) {        this.surfaceMinute = surfaceMinute;    }    public String getSurfaceMoney() {        return surfaceMoney;    }    public void setSurfaceMoney(String surfaceMoney) {        this.surfaceMoney = surfaceMoney;    }    public ClinicAddress getClinicAddress() {        return clinicAddress;    }    public void setClinicAddress(ClinicAddress clinicAddress) {        this.clinicAddress = clinicAddress;    }    public AppendCoupon getCoupons() {        return coupons;    }    public void setCoupons(AppendCoupon coupons) {        this.coupons = coupons;    }    public ISOpen getIsOpen() {        return isOpen;    }    public void setIsOpen(ISOpen isOpen) {        this.isOpen = isOpen;    }    public String getNetworkMinute() {        return networkMinute;    }    public void setNetworkMinute(String networkMinute) {        this.networkMinute = networkMinute;    }    public int getDoctorId() {        return doctorId;    }    public void setDoctorId(int doctorId) {        this.doctorId = doctorId;    }    public void setId(int id) {        this.id = id;    }    public void setAvatar(String avatar) {        this.avatar = avatar;    }    public void setName(String name) {        this.name = name;    }    public void setEmail(String email) {        this.email = email;    }    public void setGender(int gender) {        this.gender = gender;    }    public void setHospitalId(int hospitalId) {        this.hospitalId = hospitalId;    }    public void setSpecialist(String specialist) {        this.specialist = specialist;    }    public void setHospitalPhone(String hospitalPhone) {        this.hospitalPhone = hospitalPhone;    }    public void setTitle(String title) {        this.titles = title;    }    public void setTitleImg(String titleImg) {        this.titleImg = titleImg;    }    public void setPractitionerImg(String practitionerImg) {        this.practitionerImg = practitionerImg;    }    public void setCertifiedImg(String certifiedImg) {        this.certifiedImg = certifiedImg;    }    public void setDetail(String detail) {        this.detail = detail;    }    public void setHospitalName(String hospitalName) {        this.hospitalName = hospitalName;    }    public void setVoipAccount(String voipAccount) {        this.voipAccount = voipAccount;    }    public void setPhone(String phone) {        this.phone = phone;    }    public void setReviewStatus(String reviewStatus) {        this.reviewStatus = reviewStatus;    }    public int getId() {        if (id == -1) {            return doctorId;        }        return id;    }    public int getSpecialistCateg() {        return specialistCateg;    }    public void setSpecialistCateg(int specialistCateg) {        this.specialistCateg = specialistCateg;    }    public String getAvatar() {        return avatar;    }    public String getName() {        return name;    }    public String getEmail() {        return email;    }    public int getGender() {        return gender;    }    public int getHospitalId() {        return hospitalId;    }    public String getSpecialist() {        return specialist;    }    public String getHospitalPhone() {        return hospitalPhone;    }    public String getTitle() {        return titles;    }    public String getTitleImg() {        return titleImg;    }    public String getPractitionerImg() {        return practitionerImg;    }    public String getCertifiedImg() {        return certifiedImg;    }    public String getDetail() {        return detail;    }    public String getHospitalName() {        return hospitalName;    }    public String getVoipAccount() {        return voipAccount;    }    public String getPhone() {        return semiHidePhoneNum(phone);    }    public String getReviewStatus() {        return reviewStatus;    }    public Doctor() {    }    public void setLevel(String level) {        this.level = level;    }    public void setCity(String city) {        this.citys = city;    }    public void setMoney(double money) {        this.money = money;    }    public void setSecondMoney(double secondMoney) {        this.secondMoney = secondMoney;    }    public void setPoint(float point) {        this.point = point;    }    public String getLevel() {        return level;    }    public String getCity() {        return citys;    }    public double getMoney() {        return money;    }    public double getSecondMoney() {        return secondMoney;    }    public float getPoint() {        return point;    }    public boolean isSelected() {        return isSelected;    }    public void setIsSelected(boolean isSelected) {        this.isSelected = isSelected;    }    public int getHide() {        return hide;    }    public void setHide(int hide) {        this.hide = hide;    }    @Override    @JsonIgnore    public int getItemLayoutId() {        if (super.getItemLayoutId() == -1) {            return R.layout.item_doctor;        } else {            return super.getItemLayoutId();        }    }    @JsonIgnore    public DoctorHandler getHandler() {        DoctorHandler handler = new DoctorHandler(this);        handler.hasSharedTransition(hasSharedTransition);        return handler;    }    public void setBirthday(String birthday) {        this.birthday = birthday;    }    public void setIsFav(String isFav) {        this.isFav = isFav;    }    public String getBirthday() {        return birthday;    }    public String getIsFav() {        return isFav;    }    public String getYunxinAccid() {        return yunxinAccid;    }    public void setYunxinAccid(String yunxinAccid) {        this.yunxinAccid = yunxinAccid;    }    public String getTid() {        return tid;    }    public void setTid(String tid) {        this.tid = tid;    }    public int getFollowUpPermission() {        return followUpPermission;    }    public void setFollowUpPermission(int followUpPermission) {        this.followUpPermission = followUpPermission;    }    public String getIsRecommend() {        return isRecommend;    }    public void setIsRecommend(String isRecommend) {        this.isRecommend = isRecommend;    }    public String getIsBan() {        return isBan;    }    public void setIsBan(String isBan) {        this.isBan = isBan;    }    @Bindable    public float getAttitudePoint() {        return attitudePoint;    }    public void setAttitudePoint(float attitudePoint) {        this.attitudePoint = attitudePoint;        notifyPropertyChanged(BR.attitudePoint);    }    @Bindable    public float getProfessionalPoint() {        return professionalPoint;    }    public void setProfessionalPoint(float professionalPoint) {        this.professionalPoint = professionalPoint;        notifyPropertyChanged(BR.professionalPoint);    }    @Bindable    public float getPunctualityPoint() {        return punctualityPoint;    }    public void setPunctualityPoint(float punctualityPoint) {        this.punctualityPoint = punctualityPoint;        notifyPropertyChanged(BR.punctualityPoint);    }    public String getAveragePoint() {        float averagePoint = (getProfessionalPoint() + getAttitudePoint() + getPunctualityPoint()) / 3;        return String.format(Locale.CHINA, "%.1f", averagePoint);    }    @JsonIgnore    public int getLevelBackgroundColor() {        if (!TextUtils.isEmpty(level)){            if (level.equals("执业医师认证")) {                return R.drawable.stroke_rect_red;            } else {                return R.drawable.stroke_rect_green;            }        }        return 0;    }    @JsonIgnore    public boolean getLevelColor() {        if (level.equals("执业医师认证")) {            return true;        } else {            return false;        }    }    @Override    public String toString() {        return "Doctor{" +                "isSelected=" + isSelected +                ", attitudePoint=" + attitudePoint +                ", professionalPoint=" + professionalPoint +                ", punctualityPoint=" + punctualityPoint +                ", birthday='" + birthday + '\'' +                ", isFav='" + isFav + '\'' +                ", id=" + id +                ", doctorId=" + doctorId +                ", avatar='" + avatar + '\'' +                ", name='" + name + '\'' +                ", email='" + email + '\'' +                ", gender=" + gender +                ", hospitalId=" + hospitalId +                ", specialist='" + specialist + '\'' +                ", hospitalPhone='" + hospitalPhone + '\'' +                ", title='" + titles + '\'' +                ", titleImg='" + titleImg + '\'' +                ", practitionerImg='" + practitionerImg + '\'' +                ", certifiedImg='" + certifiedImg + '\'' +                ", detail='" + detail + '\'' +                ", hospitalName='" + hospitalName + '\'' +                ", voipAccount='" + voipAccount + '\'' +                ", phone='" + phone + '\'' +                ", reviewStatus='" + reviewStatus + '\'' +                ", level='" + level + '\'' +                ", city='" + citys + '\'' +                ", money=" + money +                ", secondMoney=" + secondMoney +                ", point=" + point +                ", followUpPermission=" + followUpPermission +                ", applyId='" + applyId + '\'' +                ", tags=" + tags +                ", yunxinAccid='" + yunxinAccid + '\'' +                ", tid='" + tid + '\'' +                ", isRecommend='" + isRecommend + '\'' +                ", isBan='" + isBan + '\'' +                ", hide=" + hide + '\'' +                ", specialist_categ=" + specialistCateg +                ",network_minute=" + networkMinute + ",surface_money=" + surfaceMoney + ",surface_minute=" + surfaceMinute + ",append_coupon:{" + coupons + "},is_open:{" + isOpen + "}" +                '}';    }    public void fromHashMap(Map<String, String> map) {        birthday = map.get("age");        isFav = map.get("is_fav");        avatar = map.get("avatar");        name = map.get("name");        email = map.get("email");        try {            id = Integer.parseInt(map.get("id"));            gender = Integer.parseInt(map.get("gender"));            hospitalId = Integer.parseInt(map.get("hospital_id"));            money = Double.parseDouble(map.get("money"));            secondMoney = Double.parseDouble(map.get("second_money"));            point = Float.parseFloat(map.get("point"));            hide = Integer.parseInt(map.get("hide"));            specialistCateg = Integer.parseInt(map.get("specialist_categ"));        } catch (Exception ignored) {        }        specialist = map.get("specialist");        hospitalPhone = map.get("hospital_phone");        titles = map.get("title");        titleImg = map.get("title_img");        practitionerImg = map.get("practitioner_img");        certifiedImg = map.get("certified_img");        detail = map.get("detail");        hospitalName = map.get("hospital_name");        voipAccount = map.get("voipAccount");        phone = map.get("phone");        reviewStatus = map.get("status");        level = map.get("level");        citys = map.get("city");        yunxinAccid = map.get("yunxin_accid");        tid = map.get("tid");    }    @Override    public int getLayoutId() {        return getItemLayoutId();    }    @Override    public String getKey() {        return String.valueOf(getId());    }    @Override    public int describeContents() {        return 0;    }    @Override    public void writeToParcel(Parcel dest, int flags) {        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);        dest.writeFloat(this.attitudePoint);        dest.writeFloat(this.professionalPoint);        dest.writeFloat(this.punctualityPoint);        dest.writeString(this.birthday);        dest.writeString(this.isFav);        dest.writeInt(this.id);        dest.writeInt(this.doctorId);        dest.writeString(this.avatar);        dest.writeString(this.name);        dest.writeString(this.email);        dest.writeInt(this.gender);        dest.writeInt(this.hospitalId);        dest.writeString(this.specialist);        dest.writeString(this.hospitalPhone);        dest.writeString(this.titles);        dest.writeString(this.titleImg);        dest.writeString(this.practitionerImg);        dest.writeString(this.certifiedImg);        dest.writeString(this.detail);        dest.writeString(this.hospitalName);        dest.writeString(this.voipAccount);        dest.writeString(this.phone);        dest.writeString(this.reviewStatus);        dest.writeString(this.level);        dest.writeString(this.citys);        dest.writeDouble(this.money);        dest.writeDouble(this.secondMoney);        dest.writeInt(this.hide);        dest.writeFloat(this.point);        dest.writeInt(this.followUpPermission);        dest.writeString(this.applyId);        dest.writeTypedList(this.tags);        dest.writeString(this.yunxinAccid);        dest.writeString(this.tid);        dest.writeString(this.isRecommend);        dest.writeString(this.isBan);        dest.writeInt(this.push_enable);        dest.writeInt(this.specialistCateg);        dest.writeString(this.networkMinute);        dest.writeString(this.surfaceMinute);        dest.writeString(this.surfaceMoney);    }    protected Doctor(Parcel in) {        this.isSelected = in.readByte() != 0;        this.attitudePoint = in.readFloat();        this.professionalPoint = in.readFloat();        this.punctualityPoint = in.readFloat();        this.birthday = in.readString();        this.isFav = in.readString();        this.id = in.readInt();        this.doctorId = in.readInt();        this.avatar = in.readString();        this.name = in.readString();        this.email = in.readString();        this.gender = in.readInt();        this.hospitalId = in.readInt();        this.specialist = in.readString();        this.hospitalPhone = in.readString();        this.titles = in.readString();        this.titleImg = in.readString();        this.practitionerImg = in.readString();        this.certifiedImg = in.readString();        this.detail = in.readString();        this.hospitalName = in.readString();        this.voipAccount = in.readString();        this.phone = in.readString();        this.reviewStatus = in.readString();        this.level = in.readString();        this.citys = in.readString();        this.money = in.readDouble();        this.secondMoney = in.readDouble();        this.hide = in.readInt();        this.point = in.readFloat();        this.followUpPermission = in.readInt();        this.applyId = in.readString();        this.tags = in.createTypedArrayList(Tags.CREATOR);        this.yunxinAccid = in.readString();        this.tid = in.readString();        this.isRecommend = in.readString();        this.isBan = in.readString();        this.push_enable = in.readInt();        this.specialistCateg = in.readInt();        this.surfaceMinute = in.readString();        this.surfaceMoney = in.readString();        this.networkMinute = in.readString();    }    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {        @Override        public Doctor createFromParcel(Parcel source) {            return new Doctor(source);        }        @Override        public Doctor[] newArray(int size) {            return new Doctor[size];        }    };    private String semiHidePhoneNum(String originPhone) {        if (originPhone == null || "".equals(originPhone)) {            return "";        }        return originPhone.substring(0, 3) + "****" + originPhone.substring(7);    }    public void disableSharedTransition() {        hasSharedTransition = false;    }    public void toggleFav(final Context context, final Doctor isFav) {        ToolModule api = Api.of(ToolModule.class);        if ("1".equals(isFav.getIsFav())) {            api.unlikeDoctor(isFav.id).enqueue(new SimpleCallback<String>() {                @Override                protected void handleResponse(String response) {                    Intent toUpdate = new Intent();                    toUpdate.setAction("unconllect");                    context.sendBroadcast(toUpdate);                    isFav.setIsFav("0");                    Toast.makeText(context, "取消收藏医生", Toast.LENGTH_SHORT).show();                }            });        } else {            api.likeDoctor(isFav.id).enqueue(new SimpleCallback<String>() {                @Override                protected void handleResponse(String response) {                    Intent toUpdate = new Intent();                    toUpdate.setAction("conllect");                    context.sendBroadcast(toUpdate);                    isFav.setIsFav("1");                    Toast.makeText(context, "成功收藏医生", Toast.LENGTH_SHORT).show();                }            });        }    }    public void toArticle(Context context, int id) {        Bundle bundle = DoctorArticleFragment.getArgs(id);        Intent intent = SingleFragmentActivity.intentFor(context, "文章", bundle);        context.startActivity(intent);    }    public void toComments(Context context, int id) {        Bundle bundle = DoctorCommentFragment.getArgs(id);        Intent intent = SingleFragmentActivity.intentFor(context, "评论", bundle);        context.startActivity(intent);    }    public void askForService(Context context) {        Intent intent = MedicineStoreActivity.intentForCustomerService(context);        context.startActivity(intent);    }    public String getCoupon(Coupon coupon) {        String text = "";        if (coupon != null) {            if (!TextUtils.isEmpty(coupon.threshold)) {                int threshold = Integer.parseInt(coupon.threshold);                if (threshold == 0) {                    text = "立减" + coupon.couponMoney + "元";                } else {                    text = coupon.couponMoney + "元现金券";                }            } else {                text = "";            }        }        return text;    }    public String getSimpleCoupon(Coupon coupon) {        String text = "";        if (coupon != null) {            if (!TextUtils.isEmpty(coupon.threshold)) {                text = "立减" + coupon.couponMoney + "元";            } else {                text = "";            }        }        return text;    }    public String getSimpleMoney() {        double money = 0;        NumberFormat numberFormat = new DecimalFormat("0.00");        if (coupons != null) {            if (null != coupons.simple) {                if (!TextUtils.isEmpty(coupons.getSimple().couponMoney)) {                    double simpleMoney = Double.parseDouble(coupons.getSimple().couponMoney);                    money = Double.parseDouble(numberFormat.format(secondMoney - simpleMoney));                    if (money < 0) {                        money = 0;                    }                }            }        }        return money + "";    }}