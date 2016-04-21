package com.doctor.sun.ui.activity.patient.handler;

import android.content.Intent;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.entity.constans.Gender;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.ui.activity.patient.MedicalRecordDetailActivity;
import com.doctor.sun.ui.activity.patient.UrgentCallActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.OnItemClickListener;

/**
 * Created by lucas on 1/7/16.
 */
public class MedicalRecordHandler {
    private MedicalRecord data;

    public MedicalRecordHandler(MedicalRecord medicalRecord) {
        data = medicalRecord;
    }

    public OnItemClickListener updateRecord() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {
                Intent intent = MedicalRecordDetailActivity.makeIntent(view.getContext(), data);
                view.getContext().startActivity(intent);
            }
        };
    }

    public void applyAppointment(View view) {
        Intent intent = UrgentCallActivity.makeIntent(view.getContext(), data);
        view.getContext().startActivity(intent);
    }

    public void select(View view) {
        view.setSelected(!view.isSelected());
    }

    public String getGenderResult() {
        String gender = "";
        switch (data.getGender()) {
            case 1:
                gender = "（男）";
                break;
            case 2:
                gender = "（女）";
                break;
        }
        return gender;
    }

    public String getGenderRecord() {
        String gender = "";
        switch (data.getGender()) {
            case 1:
                gender = "男";
                break;
            case 2:
                gender = "女";
                break;
        }
        return gender;
    }

    public String getLocate() {
        String locate;
        locate = data.getProvince() + data.getCity();
        return locate;
    }

    public String getRecord() {
        return "（" + getGenderRecord() + "/" + data.getAge() + "岁）";
    }

    public String getRecordDetail() {
        return data.getName() + "（" + getGenderRecord() + "/" + data.getAge() + "岁）";
    }

    public int getDefaultAvatar() {
        if (data.getGender() == Gender.MALE) {
            return R.drawable.male_patient_avatar;
        } else {
            return R.drawable.female_patient_avatar;
        }
    }
}