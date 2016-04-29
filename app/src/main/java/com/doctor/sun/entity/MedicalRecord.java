package com.doctor.sun.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.R;
import com.doctor.sun.entity.constans.Gender;
import com.doctor.sun.ui.activity.patient.handler.MedicalRecordHandler;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 11/20/15.
 */
public class MedicalRecord implements Parcelable, LayoutId {
    /**
     * 病历列表项数据
     * "patient_id": 1,
     * "name": "大明",
     * "email": "",
     * "relation": "本人",
     * "gender": 1,
     * "birthday": "2010-02",
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
    @JsonProperty("medicalRecordId")
    private int medicalRecordId;
    @JsonProperty("age")
    private int age;
    @JsonProperty("gender")
    private int gender;
    @JsonProperty("name")
    private String name;
    @JsonProperty("relation")
    private String relation;
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("city")
    private String city;
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

    MedicalRecordHandler handler = new MedicalRecordHandler(this);


    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getName() {
        return name;
    }

    public String getRelation() {
        return relation;
    }

    @Gender
    public int getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getCity() {
        return city;
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

    public MedicalRecordHandler getHandler() {
        if (handler == null) {
            handler = new MedicalRecordHandler(this);
        }
        return handler;
    }

    public void setHandler(MedicalRecordHandler handler) {
        this.handler = handler;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_text;
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "patientId=" + patientId +
                ", name='" + name + '\'' +
                ", relation='" + relation + '\'' +
                ", gender=" + gender +
                ", birthday='" + birthday + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", address='" + address + '\'' +
                ", identityNumber='" + identityNumber + '\'' +
                ", patientName='" + patientName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", medicalRecordId=" + medicalRecordId +
                ", appointmentId=" + appointmentId +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.patientId);
        dest.writeString(this.name);
        dest.writeString(this.relation);
        dest.writeInt(this.gender);
        dest.writeString(this.birthday);
        dest.writeString(this.city);
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
    }

    public MedicalRecord() {
    }

    protected MedicalRecord(Parcel in) {
        this.patientId = in.readInt();
        this.name = in.readString();
        this.relation = in.readString();
        this.gender = in.readInt();
        this.birthday = in.readString();
        this.city = in.readString();
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
    }


    public static final Creator<MedicalRecord> CREATOR = new Creator<MedicalRecord>() {
        public MedicalRecord createFromParcel(Parcel source) {
            return new MedicalRecord(source);
        }

        public MedicalRecord[] newArray(int size) {
            return new MedicalRecord[size];
        }
    };

}
