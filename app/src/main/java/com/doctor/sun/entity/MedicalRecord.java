package com.doctor.sun.entity;

import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.entity.constans.ReviewStatus;
import com.doctor.sun.vm.BaseItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 11/20/15.
 */
public class MedicalRecord extends BaseItem implements Parcelable {
    /**
     * can_follow_up : 1
     * 病历列表项数据
     * "patient_id": 1,
     * "name": "大明",
     * "email": "",
     * "relation": "本人",
     * "gender": 1,
     * "age": "2010-02",
     * "province": "广东",
     * "city": "广州",
     * "address": "天河",
     * "identity_number": "422873199009123220",
     * "patient_name": "大明",
     * "age": 5,
     * "medicalRecordId": 1,
     * "appointment_id": [
     * 1,
     * 2,
     * 6,
     * 173,
     * 179,
     * 180,
     * 182,
     * 184,
     * 185,
     * 195,
     * 207,
     * 633
     * ]
     *
     * @return
     */
    @JsonProperty("patient_id")
    private int patientId;
    @JsonProperty("id")
    private int medicalRecordId;
    @JsonProperty("age")
    private int age;
    @JsonProperty("gender")
    private int gender;
    @JsonProperty("record_name")
    private String recordName;
    @JsonProperty("relation")
    private String relation;
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("city")
    private String citys;
    @JsonProperty("province")
    private String province;
    @JsonProperty("address")
    private String address;
    @JsonProperty("identity_number")
    private String identityNumber;
    @JsonProperty("patient_name")
    private String patientName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("tid")
    private int tid = 0;
    @JsonProperty("yunxin_accid")
    private String yunxinAccid = "";
    @JsonProperty("appointment_id")
    private List<Integer> appointmentId;
    @JsonProperty("can_follow_up")
    public String canFollowUp;
    @JsonProperty("can_apply_follow_up")
    public String allowToApply;
    @JsonProperty("patient_avatar")
    public String patientAvatar;
    @JsonProperty("review_status")
    public String reviewStatus = "";


    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Bindable
    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
        notifyPropertyChanged(BR.gender);
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setCity(String city) {
        this.citys = city;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setMedicalRecordId(int medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getRecordName() {
        return recordName;
    }

    public String getRelation() {
        return relation;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getCity() {
        return citys;
    }

    public String getProvince() {
        return province;
    }

    public String getAddress() {
        return address;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public String getPatientName() {
        return patientName;
    }

    public int getAge() {
        return age;
    }

    public int getMedicalRecordId() {
        return medicalRecordId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getYunxinAccid() {
        return yunxinAccid;
    }

    public void setYunxinAccid(String yunxinAccid) {
        this.yunxinAccid = yunxinAccid;
    }

    public List<Integer> getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(List<Integer> appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientAvatar() {
        return patientAvatar;
    }

    public void setPatientAvatar(String patientAvatar) {
        this.patientAvatar = patientAvatar;
    }


    @Override
    public int getItemLayoutId() {
        return R.layout.item_r_medical_record;
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "patientId=" + patientId +
                ", name='" + recordName + '\'' +
                ", relation='" + relation + '\'' +
                ", gender=" + gender +
                ", age='" + birthday + '\'' +
                ", city='" + citys + '\'' +
                ", province='" + province + '\'' +
                ", address='" + address + '\'' +
                ", identityNumber='" + identityNumber + '\'' +
                ", patientName='" + patientName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", medicalRecordId=" + medicalRecordId +
                ", id=" + appointmentId +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.patientId);
        dest.writeString(this.recordName);
        dest.writeString(this.relation);
        dest.writeInt(this.gender);
        dest.writeString(this.birthday);
        dest.writeString(this.citys);
        dest.writeString(this.province);
        dest.writeString(this.address);
        dest.writeString(this.identityNumber);
        dest.writeString(this.patientName);
        dest.writeInt(this.age);
        dest.writeString(this.email);
        dest.writeInt(this.medicalRecordId);
        dest.writeList(this.appointmentId);
        dest.writeInt(this.tid);
        dest.writeString(this.yunxinAccid);
        dest.writeString(this.reviewStatus);
    }

    public MedicalRecord() {
    }

    protected MedicalRecord(Parcel in) {
        this.patientId = in.readInt();
        this.recordName = in.readString();
        this.relation = in.readString();
        this.gender = in.readInt();
        this.birthday = in.readString();
        this.citys = in.readString();
        this.province = in.readString();
        this.address = in.readString();
        this.identityNumber = in.readString();
        this.patientName = in.readString();
        this.age = in.readInt();
        this.email = in.readString();
        this.medicalRecordId = in.readInt();
        this.appointmentId = new ArrayList<Integer>();
        in.readList(this.appointmentId, List.class.getClassLoader());
        this.tid = in.readInt();
        this.yunxinAccid = in.readString();
        this.reviewStatus = in.readString();
    }


    public static final Creator<MedicalRecord> CREATOR = new Creator<MedicalRecord>() {
        public MedicalRecord createFromParcel(Parcel source) {
            return new MedicalRecord(source);
        }

        public MedicalRecord[] newArray(int size) {
            return new MedicalRecord[size];
        }
    };

    public String getRecord(MedicalRecord data){
        if (data.getRelation().equals("本人")){
            return data.getRecordName()+"("+data.getRelation()+")";
        }else{
            return data.getRecordName();
        }
    }

}
