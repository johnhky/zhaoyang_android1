package com.doctor.sun.entity;


import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.handler.AppointmentHandler;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 11/25/15.
 */
public class AppointMent implements LayoutId, Parcelable {
    public static final int NOT_PAID = 0;
    public static final int PAID = 1;
    /**
     * id : 25
     * record_id : 1
     * patient_name : 大明
     * type : 详细就诊
     * book_time : 2015-08-21 18:02－18:32
     * status : 1
     * has_pay : 1
     * progress : 0/0
     * medical_record : {"patient_id":11,"name":"大明","relation":"本人","gender":1,"birthday":"1991-01","city":"广州","province":"广东","address":"天河","identity_number":"111111111111111111","patient_name":"大明","age":24,"medicalRecordId":1}
     * return_list_id : 1
     * patient_name : fim
     * return_list_time : 2015-09-28 12:46:15
     * appointment_id : 1
     * money : 100
     * name : fim
     * relation : 本人
     * gender : 1
     * birthday : 1990-07
     * id : 38
     * record_id : 1
     * created_at : 2015-09-01 15:17:46
     * pay_time : 1441092544
     * money : 2000
     * is_pay : 1
     * add_money : 500
     * is_pay_add : 0
     * is_valid : 1
     * real_add_money : 0
     * unpay_money : 500
     * progress : 0/0
     * medicalRecord : {"patient_id":11,"name":"大明","relation":"本人","gender":1,"birthday":"1991-01","province":"广东","city":"广州","address":"天河","identity_number":"111111111111111111","patient_name":"大明","age":24,"medicalRecordId":1}
     */

    @JsonProperty("id")
    private int id;
    @JsonProperty("record_id")
    private int recordId;
    @JsonProperty("progress")
    private String progress;
    @JsonProperty("return_list_id")
    private int returnListId;
    @JsonProperty("return_list_time")
    private String returnListTime;
    @JsonProperty("appointment_id")
    private int appointmentId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("relation")
    private String relation;
    @JsonProperty("gender")
    private int gender;
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("patient_name")
    private String patientName;
    @JsonProperty("type")
    private String type;
    @JsonProperty("book_time")
    private String bookTime;
    @JsonProperty("status")
    private int status;
    @JsonProperty("has_pay")
    private int hasPay;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("pay_time")
    private int payTime;
    @JsonProperty("is_pay")
    private int isPay;
    @JsonProperty("add_money")
    private int addMoney;
    @JsonProperty("is_pay_add")
    private int isPayAdd;
    @JsonProperty("is_valid")
    private int isValid;
    @JsonProperty("real_add_money")
    private int realAddMoney;
    @JsonProperty("unpay_money")
    private int unpayMoney;
    @JsonProperty("medical_record")
    private MedicalRecord medicalRecord;
    @JsonProperty("doctor")
    private Doctor doctor;
    @JsonProperty("money")
    private String money;
    @JsonProperty("is_finish")
    private int isFinish;
    @JsonProperty("patient_point")
    private double patientPoint;
    @JsonProperty("voipAccount")
    private String voipAccount;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("return_info")
    private ReturnInfo returnInfo;
    @JsonProperty("record")
    private MedicalRecord urgentRecord;
    @JsonProperty("record_name")
    private String recordName;

    @JsonProperty("doctor_point")
    private double doctorPoint;

    public double getDoctorPoint() {
        return doctorPoint;
    }

    public int changeDoctorPoint() {
        return (int) doctorPoint;
    }

    public void setDoctorPoint(double doctorPoint) {
        this.doctorPoint = doctorPoint;
    }

    private int itemLayoutId;

    private AppointmentHandler handler = new AppointmentHandler(this);

    public AppointMent() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public int getReturnListId() {
        return returnListId;
    }

    public void setReturnListId(int returnListId) {
        this.returnListId = returnListId;
    }

    public String getReturnListTime() {
        return returnListTime;
    }

    public void setReturnListTime(String returnListTime) {
        this.returnListTime = returnListTime;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHasPay() {
        return hasPay;
    }

    public void setHasPay(int hasPay) {
        this.hasPay = hasPay;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getPayTime() {
        return payTime;
    }

    public void setPayTime(int payTime) {
        this.payTime = payTime;
    }

    public int getIsPay() {
        return isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public int getAddMoney() {
        return addMoney;
    }

    public void setAddMoney(int addMoney) {
        this.addMoney = addMoney;
    }

    public int getIsPayAdd() {
        return isPayAdd;
    }

    public void setIsPayAdd(int isPayAdd) {
        this.isPayAdd = isPayAdd;
    }

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    public int getRealAddMoney() {
        return realAddMoney;
    }

    public void setRealAddMoney(int realAddMoney) {
        this.realAddMoney = realAddMoney;
    }

    public int getUnpayMoney() {
        return unpayMoney;
    }

    public void setUnpayMoney(int unpayMoney) {
        this.unpayMoney = unpayMoney;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public double getPatientPoint() {
        return patientPoint;
    }

    public void setPatientPoint(double patientPoint) {
        this.patientPoint = patientPoint;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ReturnInfo getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(ReturnInfo returnInfo) {
        this.returnInfo = returnInfo;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_appointment;
    }


    public MedicalRecord getUrgentRecord() {
        return urgentRecord;
    }

    public void setUrgentRecord(MedicalRecord urgentRecord) {
        this.urgentRecord = urgentRecord;
    }

    public void setItemLayoutId(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    public AppointmentHandler getHandler() {
        return handler;
    }

    public void setHandler(AppointmentHandler handler) {
        this.handler = handler;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }


    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", recordId=" + recordId +
                ", progress='" + progress + '\'' +
                ", returnListId=" + returnListId +
                ", returnListTime='" + returnListTime + '\'' +
                ", appointmentId=" + appointmentId +
                ", name='" + name + '\'' +
                ", relation='" + relation + '\'' +
                ", gender=" + gender +
                ", birthday='" + birthday + '\'' +
                ", patientName='" + patientName + '\'' +
                ", type='" + type + '\'' +
                ", bookTime='" + bookTime + '\'' +
                ", status=" + status +
                ", hasPay=" + hasPay +
                ", createdAt='" + createdAt + '\'' +
                ", payTime=" + payTime +
                ", isPay=" + isPay +
                ", addMoney=" + addMoney +
                ", isPayAdd=" + isPayAdd +
                ", isValid=" + isValid +
                ", realAddMoney=" + realAddMoney +
                ", unpayMoney=" + unpayMoney +
                ", medicalRecord=" + medicalRecord +
                ", doctor=" + doctor +
                ", money='" + money + '\'' +
                ", isFinish='" + isFinish + '\'' +
                ", patientPoint=" + patientPoint +
                ", voipAccount='" + voipAccount + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar='" + avatar + '\'' +
                ", returnInfo=" + returnInfo +
                ", urgentRecord=" + urgentRecord +
                ", recordName='" + recordName + '\'' +
                ", doctorPoint=" + doctorPoint +
                ", itemLayoutId=" + itemLayoutId +
                ", handler=" + handler +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.recordId);
        dest.writeString(this.progress);
        dest.writeInt(this.returnListId);
        dest.writeString(this.returnListTime);
        dest.writeInt(this.appointmentId);
        dest.writeString(this.name);
        dest.writeString(this.relation);
        dest.writeInt(this.gender);
        dest.writeString(this.birthday);
        dest.writeString(this.patientName);
        dest.writeString(this.type);
        dest.writeString(this.bookTime);
        dest.writeInt(this.status);
        dest.writeInt(this.hasPay);
        dest.writeString(this.createdAt);
        dest.writeInt(this.payTime);
        dest.writeInt(this.isPay);
        dest.writeInt(this.addMoney);
        dest.writeInt(this.isPayAdd);
        dest.writeInt(this.isValid);
        dest.writeInt(this.realAddMoney);
        dest.writeInt(this.unpayMoney);
        dest.writeParcelable(this.medicalRecord, 0);
        dest.writeParcelable(this.doctor, 0);
        dest.writeString(this.money);
        dest.writeInt(this.isFinish);
        dest.writeDouble(this.patientPoint);
        dest.writeString(this.voipAccount);
        dest.writeString(this.phone);
        dest.writeString(this.avatar);
        dest.writeParcelable(this.returnInfo, 0);
        dest.writeParcelable(this.urgentRecord, 0);
        dest.writeString(this.recordName);
        dest.writeDouble(this.doctorPoint);
        dest.writeInt(this.itemLayoutId);
    }

    protected AppointMent(Parcel in) {
        this.id = in.readInt();
        this.recordId = in.readInt();
        this.progress = in.readString();
        this.returnListId = in.readInt();
        this.returnListTime = in.readString();
        this.appointmentId = in.readInt();
        this.name = in.readString();
        this.relation = in.readString();
        this.gender = in.readInt();
        this.birthday = in.readString();
        this.patientName = in.readString();
        this.type = in.readString();
        this.bookTime = in.readString();
        this.status = in.readInt();
        this.hasPay = in.readInt();
        this.createdAt = in.readString();
        this.payTime = in.readInt();
        this.isPay = in.readInt();
        this.addMoney = in.readInt();
        this.isPayAdd = in.readInt();
        this.isValid = in.readInt();
        this.realAddMoney = in.readInt();
        this.unpayMoney = in.readInt();
        this.medicalRecord = in.readParcelable(MedicalRecord.class.getClassLoader());
        this.doctor = in.readParcelable(Doctor.class.getClassLoader());
        this.money = in.readString();
        this.isFinish = in.readInt();
        this.patientPoint = in.readDouble();
        this.voipAccount = in.readString();
        this.phone = in.readString();
        this.avatar = in.readString();
        this.returnInfo = in.readParcelable(ReturnInfo.class.getClassLoader());
        this.urgentRecord = in.readParcelable(MedicalRecord.class.getClassLoader());
        this.recordName = in.readString();
        this.doctorPoint = in.readDouble();
        this.itemLayoutId = in.readInt();
    }

    public static final Creator<AppointMent> CREATOR = new Creator<AppointMent>() {
        public AppointMent createFromParcel(Parcel source) {
            return new AppointMent(source);
        }

        public AppointMent[] newArray(int size) {
            return new AppointMent[size];
        }
    };
}
