package com.doctor.sun.ui.activity.patient.handler;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.constans.Gender;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.activity.AfterServiceHistoryActivity;
import com.doctor.sun.ui.activity.patient.EditRecordActivity;

/**
 * Created by lucas on 1/7/16.
 */
public class MedicalRecordHandler {
    private MedicalRecord data;

    public MedicalRecordHandler(MedicalRecord medicalRecord) {
        data = medicalRecord;
    }

    public void updateRecord(Context context) {
        Intent intent = EditRecordActivity.makeIntent(context, data);
        context.startActivity(intent);
    }
//
//    public void applyAppointment(View view) {
//        Intent intent = UrgentCallActivity.makeIntent(view.getContext(), data);
//        view.getContext().startActivity(intent);
//    }

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

    public void afterServiceHistory(Context context) {
        Intent intent = AfterServiceHistoryActivity.intentFor(context, data.getMedicalRecordId());
        context.startActivity(intent);
    }

    public void allowToApply(int doctorId) {
        AfterServiceModule of = Api.of(AfterServiceModule.class);
        //注意canFollowUp 跟canApplyFollowUp别弄错了.
        final int target = data.allowToApply.equals("1") ? 0 : 1;
        of.allow(doctorId, target, data.getMedicalRecordId()).enqueue(new SimpleCallback<Void>() {
            @Override
            protected void handleResponse(Void response) {
                data.allowToApply = String.valueOf(target);
            }
        });
    }
}
