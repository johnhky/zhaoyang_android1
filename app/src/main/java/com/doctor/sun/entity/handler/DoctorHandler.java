package com.doctor.sun.entity.handler;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.doctor.sun.dto.DoctorDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.ui.activity.patient.DoctorDetailActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.OnItemClickListener;

/**
 * Created by lucas on 1/8/16.
 */
public class DoctorHandler implements Parcelable {
    private Doctor data;
    private boolean isSelected;

    public DoctorHandler(Doctor doctorDTO) {
        data = doctorDTO;
    }

    public OnItemClickListener select() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {
                setSelected(!isSelected());
                view.setSelected(isSelected());
            }
        };
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected() {
        return isSelected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.data, 0);
    }

    protected DoctorHandler(Parcel in) {
        this.data = in.readParcelable(DoctorDTO.class.getClassLoader());
    }

    public static final Creator<DoctorHandler> CREATOR = new Creator<DoctorHandler>() {
        public DoctorHandler createFromParcel(Parcel source) {
            return new DoctorHandler(source);
        }

        public DoctorHandler[] newArray(int size) {
            return new DoctorHandler[size];
        }
    };

    public void detail(View view) {
        Intent intent = DoctorDetailActivity.makeIntent(view.getContext(), data, Appointment.DETAIL);
        view.getContext().startActivity(intent);
    }
}
