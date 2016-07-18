package com.doctor.sun.entity;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.BR;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.ui.activity.patient.ApplyAppointmentActivity;
import com.doctor.sun.ui.activity.patient.PickDateActivity;
import com.doctor.sun.ui.activity.patient.SearchDoctorActivity;
import com.doctor.sun.ui.widget.SelectRecordDialog;

/**
 * Created by rick on 6/7/2016.
 */

public class AppointmentBuilder extends BaseObservable implements Parcelable {
    private int type = AppointmentType.DETAIL;
    private int duration = 15;
    private Time time;
    private boolean isToday;

    private Doctor doctor;
    private MedicalRecord record;

    public void setIsPremium(boolean isPremium) {
        if (isPremium) {
            setType(AppointmentType.DETAIL);
        }
    }

    public void setIsNormal(boolean isNormal) {
        if (isNormal) {
            setType(AppointmentType.QUICK);
        }
    }

    @Bindable
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        notifyChange();
    }

    @Bindable
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
        notifyPropertyChanged(BR.duration);
    }

    @Bindable
    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
        notifyPropertyChanged(BR.time);
    }

    @Bindable
    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        notifyPropertyChanged(BR.doctor);
    }


    @Bindable
    public MedicalRecord getRecord() {
        return record;
    }

    public boolean canIncrement() {
        return duration < 60;
    }

    public boolean canDecrement() {
        return duration > 15;
    }

    public void incrementDuration() {
        duration += 15;
        notifyChange();
    }

    public void decrementDuration() {
        duration -= 15;
        notifyChange();
    }

    public void setRecord(MedicalRecord record) {
        this.record = record;
        notifyPropertyChanged(BR.record);
    }

    public void searchDoctor(Context context, int type) {
        Intent intent = SearchDoctorActivity.makeIntent(context, type);
        context.startActivity(intent);
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
