package com.doctor.sun.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.vo.BaseItem;

/**
 * Created by kb on 16-9-16.
 * 医生信息
 */

public class DoctorInfo extends BaseItem implements LayoutId, Parcelable {

    private String name;
    private String hospital;
    private String level;
    private String specialist;
    private String title;

    public DoctorInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_doctor_info;
    }

    protected DoctorInfo(Parcel in) {
        name = in.readString();
        hospital = in.readString();
        level = in.readString();
        specialist = in.readString();
        title = in.readString();
    }

    public static final Creator<DoctorInfo> CREATOR = new Creator<DoctorInfo>() {
        @Override
        public DoctorInfo createFromParcel(Parcel in) {
            return new DoctorInfo(in);
        }

        @Override
        public DoctorInfo[] newArray(int size) {
            return new DoctorInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(hospital);
        dest.writeString(level);
        dest.writeString(specialist);
        dest.writeString(title);
    }
}
