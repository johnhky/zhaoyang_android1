package com.doctor.sun.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Lynn on 12/29/15.
 */
public class ContactDetail implements Parcelable {

    /**
     * 病历详情
     "id": 1,
     "name": "大明",
     "avatar": "https://trello-avatars.s3.amazonaws.com/eb8345770e0fd6183d370fc3e2b1f1d3/30.png",
     "age": "2010-02",
     "gender": 1,
     "record_id": 1,
     "record_name": "大明",
     "record_birthday": "2010-02",
     "relation": "本人",
     "record_gender": 1,
     "province": "广东",
     "city": "广州",
     "voipAccount": "8002293600000003",
     "phone": "15918748285",
     "nickname": "但还是会",
     "is_ban": 0,
     "can_call": 1
     */
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("age")
    private  String birthday;
    @JsonProperty("gender")
    private int gender;
    @JsonProperty("record_id")
    private int recordId;
    @JsonProperty("record_name")
    private String recordName;
    @JsonProperty("record_birthday")
    private String record_birthday;
    @JsonProperty("relation")
    private String relation;
    @JsonProperty("record_gender")
    private int recordGender;
    @JsonProperty("province")
    private String province;
    @JsonProperty("city")
    private String city;
    @JsonProperty("voipAccount")
    private String voipAccount;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("is_ban")
    private int is_ban;
    @JsonProperty("can_call")
    private int can_call;
    /**
     * doctor_id : 1
     * level : 高级认证
     * specialist : 精神科
     * title : 1
     * hospital_name : 医院1
     * point : 4.8
     */

    @JsonProperty("doctor_id")
    private int doctorId;
    @JsonProperty("level")
    private String level;
    @JsonProperty("specialist")
    private String specialist;
    @JsonProperty("title")
    private String title;
    @JsonProperty("hospital_name")
    private String hospitalName;
    @JsonProperty("point")
    private float point;

    @JsonProperty("records")
    private List<MedicalRecord> records;

    public List<MedicalRecord> getRecords() {
        return records;
    }

    public void setRecords(List<MedicalRecord> records) {
        this.records = records;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getRecord_birthday() {
        return record_birthday;
    }

    public void setRecord_birthday(String record_birthday) {
        this.record_birthday = record_birthday;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public int getRecordGender() {
        return recordGender;
    }

    public void setRecordGender(int recordGender) {
        this.recordGender = recordGender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getIs_ban() {
        return is_ban;
    }

    public void setIs_ban(int is_ban) {
        this.is_ban = is_ban;
    }

    public int getCan_call() {
        return can_call;
    }

    public void setCan_call(int can_call) {
        this.can_call = can_call;
    }

    @Override
    public String toString() {
        return "ContactDetail{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", age='" + birthday + '\'' +
                ", gender=" + gender +
                ", recordId=" + recordId +
                ", recordName='" + recordName + '\'' +
                ", record_birthday='" + record_birthday + '\'' +
                ", relation='" + relation + '\'' +
                ", recordGender=" + recordGender +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", voipAccount='" + voipAccount + '\'' +
                ", phone='" + phone + '\'' +
                ", nickname='" + nickname + '\'' +
                ", is_ban=" + is_ban +
                ", can_call=" + can_call +
                '}';
    }


    public ContactDetail() {
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getLevel() {
        return level;
    }

    public String getSpecialist() {
        return specialist;
    }

    public String getTitle() {
        return title;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public float getPoint() {
        return point;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
        dest.writeString(this.birthday);
        dest.writeInt(this.gender);
        dest.writeInt(this.recordId);
        dest.writeString(this.recordName);
        dest.writeString(this.record_birthday);
        dest.writeString(this.relation);
        dest.writeInt(this.recordGender);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.voipAccount);
        dest.writeString(this.phone);
        dest.writeString(this.nickname);
        dest.writeInt(this.is_ban);
        dest.writeInt(this.can_call);
        dest.writeInt(this.doctorId);
        dest.writeString(this.level);
        dest.writeString(this.specialist);
        dest.writeString(this.title);
        dest.writeString(this.hospitalName);
        dest.writeFloat(this.point);
    }

    protected ContactDetail(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.avatar = in.readString();
        this.birthday = in.readString();
        this.gender = in.readInt();
        this.recordId = in.readInt();
        this.recordName = in.readString();
        this.record_birthday = in.readString();
        this.relation = in.readString();
        this.recordGender = in.readInt();
        this.province = in.readString();
        this.city = in.readString();
        this.voipAccount = in.readString();
        this.phone = in.readString();
        this.nickname = in.readString();
        this.is_ban = in.readInt();
        this.can_call = in.readInt();
        this.doctorId = in.readInt();
        this.level = in.readString();
        this.specialist = in.readString();
        this.title = in.readString();
        this.hospitalName = in.readString();
        this.point = in.readFloat();
    }

    public static final Creator<ContactDetail> CREATOR = new Creator<ContactDetail>() {
        public ContactDetail createFromParcel(Parcel source) {
            return new ContactDetail(source);
        }

        public ContactDetail[] newArray(int size) {
            return new ContactDetail[size];
        }
    };
}
