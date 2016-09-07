package com.doctor.sun.entity;


import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.R;
import com.doctor.sun.entity.handler.AppointmentHandler;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 11/25/15.
 */
public class Appointment implements LayoutId, Parcelable {
    public static final int NOT_PAID = 0;
    public static final int PAID = 1;

    /**
     * id : 25
     * record_id : 1
     * patient_name : 大明
     * type : 专属咨询
     * book_time : 2015-08-21 18:02－18:32
     * status : 1
     * has_pay : 1
     * progress : 0/0
     * medical_record : {"patient_id":11,"name":"大明","relation":"本人","gender":1,"age":"1991-01","city":"广州","province":"广东","address":"天河","identity_number":"111111111111111111","patient_name":"大明","age":24,"medicalRecordId":1}
     * return_list_id : 1
     * patient_name : fim
     * return_list_time : 2015-09-28 12:46:15
     * appointment_id : 1
     * money : 100
     * name : fim
     * relation : 本人
     * gender : 1
     * age : 1990-07
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
     * medicalRecord : {"patient_id":11,"name":"大明","relation":"本人","gender":1,"age":"1991-01","province":"广东","city":"广州","address":"天河","identity_number":"111111111111111111","patient_name":"大明","age":24,"medicalRecordId":1}
     * yunxin_accid : 36
     * cancel_reason :
     * visit_time : 1458353747
     * take_time : 15
     * end_time : 0
     * order_status : 已完成
     */

    private int itemLayoutId;
    @JsonProperty("id")
    private int id;
    @JsonProperty("record_id")
    private int recordId;
    @JsonProperty("return_list_id")
    private int returnListId;
    @JsonProperty("appointment_id")
    private int appointmentId;
    @JsonProperty("gender")
    private int gender;
    @JsonProperty("status")
    private String status;
    @JsonProperty("has_pay")
    private int hasPay;
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
    @JsonProperty("tid")
    private int tid;
    @JsonProperty("unpay_money")
    private int unpayMoney;
    @JsonProperty("is_finish")
    private int isFinish;
    @JsonProperty("patient_point")
    private double patientPoint;
    @JsonProperty("doctor_point")
    private double doctorPoint;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("money")
    private String money;
    @JsonProperty("voipAccount")
    private String voipAccount;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("patient_avatar")
    private String patientAvatar;
    @JsonProperty("return_list_time")
    private String returnListTime;
    @JsonProperty("record_name")
    private String recordName;
    @JsonProperty("yunxin_accid")
    private String yunxinAccid;
    @JsonProperty("age")
    private String birthday;
    @JsonProperty("patient_name")
    private String patientName;
    @JsonProperty("display_type")
    private String displayType = "";
    @JsonProperty("book_time")
    private String bookTime;
    @JsonProperty("name")
    private String name;
    @JsonProperty("relation")
    private String relation;
    @JsonProperty("progress")
    private String progress;
    @JsonProperty("cancel_reason")
    private String cancelReason;
    @JsonProperty("visit_time")
    private String visitTime;
    @JsonProperty("take_time")
    private String takeTime;
    @JsonProperty("end_time")
    private String endTime;
    @JsonProperty("order_status")
    private String orderStatus;
    @JsonProperty("need_pay")
    private String needPay;
    @JsonProperty("medical_record")
    private MedicalRecord medicalRecord;
    @JsonProperty("doctor")
    private Doctor doctor;
    @JsonProperty("return_info")
    private ReturnInfo returnInfo;
    @JsonProperty("record")
    private MedicalRecord urgentRecord;
    @JsonProperty("type")
    private int appointmentType = 0;
    @JsonProperty("display_status")
    private String displayStatus = "";

    private AppointmentHandler handler = new AppointmentHandler(this);


    public Appointment() {
    }

    public double getDoctorPoint() {
        return doctorPoint;
    }

    public int changeDoctorPoint() {
        return (int) doctorPoint;
    }

    public void setDoctorPoint(double doctorPoint) {
        this.doctorPoint = doctorPoint;
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

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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


    public String getYunxinAccid() {
        return yunxinAccid;
    }

    public void setYunxinAccid(String yunxinAccid) {
        this.yunxinAccid = yunxinAccid;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public String getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(String takeTime) {
        this.takeTime = takeTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPatientAvatar() {
        return patientAvatar;
    }

    public void setPatientAvatar(String patientAvatar) {
        this.patientAvatar = patientAvatar;
    }

    public String getNeedPay() {
        return needPay;
    }

    public void setNeedPay(String needPay) {
        this.needPay = needPay;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", recordId=" + recordId +
                ", progress='" + progress + '\'' +
                ", returnListId=" + returnListId +
                ", returnListTime='" + returnListTime + '\'' +
                ", id=" + appointmentId +
                ", name='" + name + '\'' +
                ", relation='" + relation + '\'' +
                ", gender=" + gender +
                ", age='" + birthday + '\'' +
                ", patientName='" + patientName + '\'' +
                ", displayType='" + displayType + '\'' +
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
        dest.writeInt(this.itemLayoutId);
        dest.writeInt(this.id);
        dest.writeInt(this.recordId);
        dest.writeInt(this.returnListId);
        dest.writeInt(this.appointmentId);
        dest.writeInt(this.gender);
        dest.writeString(this.status);
        dest.writeInt(this.hasPay);
        dest.writeInt(this.payTime);
        dest.writeInt(this.isPay);
        dest.writeInt(this.addMoney);
        dest.writeInt(this.isPayAdd);
        dest.writeInt(this.isValid);
        dest.writeInt(this.realAddMoney);
        dest.writeInt(this.tid);
        dest.writeInt(this.unpayMoney);
        dest.writeInt(this.isFinish);
        dest.writeDouble(this.patientPoint);
        dest.writeDouble(this.doctorPoint);
        dest.writeString(this.createdAt);
        dest.writeString(this.money);
        dest.writeString(this.voipAccount);
        dest.writeString(this.phone);
        dest.writeString(this.avatar);
        dest.writeString(this.returnListTime);
        dest.writeString(this.recordName);
        dest.writeString(this.yunxinAccid);
        dest.writeString(this.birthday);
        dest.writeString(this.patientName);
        dest.writeString(this.displayType);
        dest.writeString(this.bookTime);
        dest.writeString(this.name);
        dest.writeString(this.relation);
        dest.writeString(this.progress);
        dest.writeString(this.cancelReason);
        dest.writeString(this.visitTime);
        dest.writeString(this.takeTime);
        dest.writeString(this.endTime);
        dest.writeString(this.orderStatus);
        dest.writeParcelable(this.medicalRecord, flags);
        dest.writeParcelable(this.doctor, flags);
        dest.writeParcelable(this.returnInfo, flags);
        dest.writeParcelable(this.urgentRecord, flags);
        dest.writeString(this.patientAvatar);
        dest.writeString(this.needPay);
        dest.writeInt(this.appointmentType);
        dest.writeString(this.displayStatus);
    }

    protected Appointment(Parcel in) {
        this.itemLayoutId = in.readInt();
        this.id = in.readInt();
        this.recordId = in.readInt();
        this.returnListId = in.readInt();
        this.appointmentId = in.readInt();
        this.gender = in.readInt();
        this.status = in.readString();
        this.hasPay = in.readInt();
        this.payTime = in.readInt();
        this.isPay = in.readInt();
        this.addMoney = in.readInt();
        this.isPayAdd = in.readInt();
        this.isValid = in.readInt();
        this.realAddMoney = in.readInt();
        this.tid = in.readInt();
        this.unpayMoney = in.readInt();
        this.isFinish = in.readInt();
        this.patientPoint = in.readDouble();
        this.doctorPoint = in.readDouble();
        this.createdAt = in.readString();
        this.money = in.readString();
        this.voipAccount = in.readString();
        this.phone = in.readString();
        this.avatar = in.readString();
        this.returnListTime = in.readString();
        this.recordName = in.readString();
        this.yunxinAccid = in.readString();
        this.birthday = in.readString();
        this.patientName = in.readString();
        this.displayType = in.readString();
        this.bookTime = in.readString();
        this.name = in.readString();
        this.relation = in.readString();
        this.progress = in.readString();
        this.cancelReason = in.readString();
        this.visitTime = in.readString();
        this.takeTime = in.readString();
        this.endTime = in.readString();
        this.orderStatus = in.readString();
        this.medicalRecord = in.readParcelable(MedicalRecord.class.getClassLoader());
        this.doctor = in.readParcelable(Doctor.class.getClassLoader());
        this.returnInfo = in.readParcelable(ReturnInfo.class.getClassLoader());
        this.urgentRecord = in.readParcelable(MedicalRecord.class.getClassLoader());
        this.patientAvatar = in.readString();
        this.needPay = in.readString();
        this.appointmentType = in.readInt();
        this.displayStatus = in.readString();
    }

    public static final Creator<Appointment> CREATOR = new Creator<Appointment>() {
        @Override
        public Appointment createFromParcel(Parcel source) {
            return new Appointment(source);
        }

        @Override
        public Appointment[] newArray(int size) {
            return new Appointment[size];
        }
    };

    public void setAppointmentType(int appointmentType) {
        this.appointmentType = appointmentType;
    }

    public int getAppointmentType() {
        return appointmentType;
    }

    public String getIdString() {
        return String.valueOf(id);
    }

    public interface AppointmentId {
        int getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Appointment that = (Appointment) o;

        if (tid != that.tid) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    public int hashCode() {
        int result = tid;
        result = 31 * result + (voipAccount != null ? voipAccount.hashCode() : 0);
        result = 31 * result + (yunxinAccid != null ? yunxinAccid.hashCode() : 0);
        return result;
    }

}
