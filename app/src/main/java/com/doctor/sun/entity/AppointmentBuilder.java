package com.doctor.sun.entity;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.ui.activity.patient.ApplyAppointmentActivity;
import com.doctor.sun.ui.activity.patient.PickDateActivity;
import com.doctor.sun.ui.widget.SelectRecordDialog;

/**
 * Created by rick on 6/7/2016.
 */

public class AppointmentBuilder implements Parcelable {
    private int type;
    private int duration;
    private Time time;
    private boolean isToday;

    private Doctor doctor;
    private MedicalRecord record;

    public int getType() {
        return type;
    }

    public AppointmentBuilder setType(int type) {
        this.type = type;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public AppointmentBuilder setDuration(int duration) {
        this.duration = duration;
        return this;
    }


    public Doctor getDoctor() {
        return doctor;
    }

    public AppointmentBuilder setDoctor(Doctor doctor) {
        this.doctor = doctor;
        return this;
    }

    public MedicalRecord getRecord() {
        return record;
    }

    public AppointmentBuilder setRecord(MedicalRecord record) {
        this.record = record;
        return this;
    }

    public Time getTime() {
        return time;
    }

    public AppointmentBuilder setTime(Time time) {
        this.time = time;
        return this;
    }

    public void pickDate(final Context context) {
        SelectRecordDialog.showRecordDialog(context, new SelectRecordDialog.SelectRecordListener() {
            @Override
            public void onSelectRecord(SelectRecordDialog dialog, MedicalRecord record) {
                setRecord(record);
                Intent intent = PickDateActivity.makeIntent(context, AppointmentBuilder.this);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    public void latestAvailableTime(final Context context, int doctorId, int duration) {
        TimeModule timeModule = Api.of(TimeModule.class);
        timeModule.latestAvailableTime(doctorId, duration, getTime().getDate()).enqueue(new SimpleCallback<Time>() {
            @Override
            protected void handleResponse(Time response) {
                if (response == null) {
                    return;
                }
                setTime(response);
                Intent intent = ApplyAppointmentActivity.makeIntent(context, AppointmentBuilder.this);
                context.startActivity(intent);
            }
        });
    }

    public int money() {
        switch (getType()) {
            case AppointmentType.QUICK:
                return doctor.getSecondMoney();
            case AppointmentType.DETAIL:
                int scalar = getDuration() / 15;
                return doctor.getMoney() * scalar;
            default:
                return 0;
        }
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public AppointmentBuilder() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeInt(this.duration);
        dest.writeParcelable(this.time, flags);
        dest.writeByte(isToday ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.doctor, flags);
        dest.writeParcelable(this.record, flags);
    }

    protected AppointmentBuilder(Parcel in) {
        this.type = in.readInt();
        this.duration = in.readInt();
        this.time = in.readParcelable(Time.class.getClassLoader());
        this.isToday = in.readByte() != 0;
        this.doctor = in.readParcelable(Doctor.class.getClassLoader());
        this.record = in.readParcelable(MedicalRecord.class.getClassLoader());
    }

    public static final Creator<AppointmentBuilder> CREATOR = new Creator<AppointmentBuilder>() {
        @Override
        public AppointmentBuilder createFromParcel(Parcel source) {
            return new AppointmentBuilder(source);
        }

        @Override
        public AppointmentBuilder[] newArray(int size) {
            return new AppointmentBuilder[size];
        }
    };
}
