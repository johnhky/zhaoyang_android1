package com.doctor.sun.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.handler.EmergencyCallHandler;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by lucas on 1/20/16.
 */
public class EmergencyCall implements LayoutId, Parcelable {

    /**
     * id : 935
     * record_id : 206
     * search_title : 不限
     * search_city : 1
     * search_gender : 不限
     * waiting_time : 12
     * money : 12
     * add_money : 0
     * created_at : 2016-01-20 18:39:01
     * real_add_money : 0
     * unpay_money : 12
     * progress : 0/0
     */

    @JsonProperty("id")
    private int id;
    @JsonProperty("record_id")
    private int recordId;
    @JsonProperty("search_title")
    private String searchTitle;
    @JsonProperty("search_city")
    private String searchCity;
    @JsonProperty("search_gender")
    private String searchGender;
    @JsonProperty("waiting_time")
    private long waitingTime;
    @JsonProperty("money")
    private int money;
    @JsonProperty("add_money")
    private int addMoney;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("real_add_money")
    private int realAddMoney;
    @JsonProperty("unpay_money")
    private int unpayMoney;
    @JsonProperty("progress")
    private String progress;
    /**
     * is_pay : 0
     * add_num : 0
     * is_pay_add : 0
     * pay_time : 0
     * is_appointment : 0
     * is_valid : 1
     * expired_time : 0
     * need_refund : 0
     */

    @JsonProperty("is_pay")
    private int isPay;
    @JsonProperty("add_num")
    private int addNum;
    @JsonProperty("is_pay_add")
    private int isPayAdd;
    @JsonProperty("pay_time")
    private long payTime;
    @JsonProperty("is_appointment")
    private int isAppointment;
    @JsonProperty("is_valid")
    private int isValid;
    @JsonProperty("expired_time")
    private int expiredTime;
    @JsonProperty("need_refund")
    private int needRefund;

    private EmergencyCallHandler handler = new EmergencyCallHandler(this);

    public void setId(int id) {
        this.id = id;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public void setSearchCity(String searchCity) {
        this.searchCity = searchCity;
    }

    public void setSearchGender(String searchGender) {
        this.searchGender = searchGender;
    }

    public void setWaitingTime(long waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setAddMoney(int addMoney) {
        this.addMoney = addMoney;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setRealAddMoney(int realAddMoney) {
        this.realAddMoney = realAddMoney;
    }

    public void setUnpayMoney(int unpayMoney) {
        this.unpayMoney = unpayMoney;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public int getRecordId() {
        return recordId;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public String getSearchCity() {
        return searchCity;
    }

    public String getSearchGender() {
        return searchGender;
    }

    public long getWaitingTime() {
        return waitingTime;
    }

    public int getMoney() {
        return money;
    }

    public int getAddMoney() {
        return addMoney;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getRealAddMoney() {
        return realAddMoney;
    }

    public int getUnpayMoney() {
        return unpayMoney;
    }

    public String getProgress() {
        return progress;
    }

    @Override
    public String toString() {
        return "EmergencyCall{" +
                "id=" + id +
                ", recordId=" + recordId +
                ", searchTitle='" + searchTitle + '\'' +
                ", searchCity='" + searchCity + '\'' +
                ", searchGender='" + searchGender + '\'' +
                ", waitingTime=" + waitingTime +
                ", money=" + money +
                ", addMoney=" + addMoney +
                ", createdAt='" + createdAt + '\'' +
                ", realAddMoney=" + realAddMoney +
                ", unpayMoney=" + unpayMoney +
                ", progress='" + progress + '\'' +
                '}';
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public void setAddNum(int addNum) {
        this.addNum = addNum;
    }

    public void setIsPayAdd(int isPayAdd) {
        this.isPayAdd = isPayAdd;
    }

    public void setPayTime(int payTime) {
        this.payTime = payTime;
    }

    public void setIsAppointment(int isAppointment) {
        this.isAppointment = isAppointment;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    public void setExpiredTime(int expiredTime) {
        this.expiredTime = expiredTime;
    }

    public void setNeedRefund(int needRefund) {
        this.needRefund = needRefund;
    }

    public int getIsPay() {
        return isPay;
    }

    public int getAddNum() {
        return addNum;
    }

    public int getIsPayAdd() {
        return isPayAdd;
    }

    public long getPayTime() {
        return payTime;
    }

    public int getIsAppointment() {
        return isAppointment;
    }

    public int getIsValid() {
        return isValid;
    }

    public int getExpiredTime() {
        return expiredTime;
    }

    public int getNeedRefund() {
        return needRefund;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.p_item_urgent_call;
    }

    public EmergencyCallHandler getHandler() {
        return handler;
    }

    public void setHandler(EmergencyCallHandler handler) {
        this.handler = handler;
    }

    ;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.recordId);
        dest.writeString(this.searchTitle);
        dest.writeString(this.searchCity);
        dest.writeString(this.searchGender);
        dest.writeLong(this.waitingTime);
        dest.writeInt(this.money);
        dest.writeInt(this.addMoney);
        dest.writeString(this.createdAt);
        dest.writeInt(this.realAddMoney);
        dest.writeInt(this.unpayMoney);
        dest.writeString(this.progress);
        dest.writeInt(this.isPay);
        dest.writeInt(this.addNum);
        dest.writeInt(this.isPayAdd);
        dest.writeLong(this.payTime);
        dest.writeInt(this.isAppointment);
        dest.writeInt(this.isValid);
        dest.writeInt(this.expiredTime);
        dest.writeInt(this.needRefund);
    }

    public EmergencyCall() {
    }

    protected EmergencyCall(Parcel in) {
        this.id = in.readInt();
        this.recordId = in.readInt();
        this.searchTitle = in.readString();
        this.searchCity = in.readString();
        this.searchGender = in.readString();
        this.waitingTime = in.readLong();
        this.money = in.readInt();
        this.addMoney = in.readInt();
        this.createdAt = in.readString();
        this.realAddMoney = in.readInt();
        this.unpayMoney = in.readInt();
        this.progress = in.readString();
        this.isPay = in.readInt();
        this.addNum = in.readInt();
        this.isPayAdd = in.readInt();
        this.payTime = in.readLong();
        this.isAppointment = in.readInt();
        this.isValid = in.readInt();
        this.expiredTime = in.readInt();
        this.needRefund = in.readInt();
    }

    public static final Creator<EmergencyCall> CREATOR = new Creator<EmergencyCall>() {
        public EmergencyCall createFromParcel(Parcel source) {
            return new EmergencyCall(source);
        }

        public EmergencyCall[] newArray(int size) {
            return new EmergencyCall[size];
        }
    };

    public String getBookTime() {
        return getCreatedAt().substring(0, getCreatedAt().length() - 3);
    }
}
