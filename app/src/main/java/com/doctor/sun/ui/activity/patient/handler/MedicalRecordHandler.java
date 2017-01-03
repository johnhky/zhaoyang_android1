package com.doctor.sun.ui.activity.patient.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.constans.Gender;
import com.doctor.sun.entity.constans.ReviewStatus;
import com.doctor.sun.event.SelectMedicalRecordEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.activity.AfterServiceHistoryActivity;
import com.doctor.sun.ui.activity.BundlesTabActivity;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.activity.patient.HistoryActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.doctor.sun.ui.fragment.EditRecordFragment;
import com.doctor.sun.ui.fragment.NewMedicalRecordFragment;

import java.util.List;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by lucas on 1/7/16.
 */
public class MedicalRecordHandler {


    private static MedicalRecordHandler instance;

    public static MedicalRecordHandler getInstance() {
        if (instance == null) {
            instance = new MedicalRecordHandler();
        }
        return instance;
    }

    public void updateRecord(Context context, MedicalRecord data) {
        Intent intent = SingleFragmentActivity.intentFor(context, "病历详情", EditRecordFragment.getArgs(data));
        context.startActivity(intent);
    }


    public String getGenderResult(MedicalRecord data) {
        return "(" + getGenderRecord(data) + ")";
    }


    public String getGenderRecord(MedicalRecord data) {
        String gender = "";
        switch (data.getGender()) {
            case Gender.MALE:
                gender = "男";
                break;
            case Gender.FEMALE:
                gender = "女";
                break;
        }
        return gender;
    }


    public String getRecord(MedicalRecord data) {
        return "（" + getGenderRecord(data) + "/" + data.getAge() + "岁）";
    }

    public String getRecordDetail(MedicalRecord data) {
        return data.getRecordName() + "（" + getGenderRecord(data) + "/" + data.getAge() + "岁）";
    }

    public void afterServiceHistory(Context context, MedicalRecord data) {
        Intent intent = AfterServiceHistoryActivity.intentFor(context, data.getMedicalRecordId());
        context.startActivity(intent);
    }

    public void allowToApply(SimpleAdapter adapter, MedicalRecord data) {
        allowToApply(getDoctorId(adapter), data);
    }

    public int getDoctorId(SimpleAdapter adapter) {
        return adapter.getInt(AdapterConfigKey.DOCTOR_ID);
    }

    public void allowToApply(int doctorId, final MedicalRecord data) {
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

    public String applyingStatus(MedicalRecord data) {

        switch (data.canFollowUp) {
            case "1": {
                return "可以申请随访";
            }
            case "-1": {
                return "没有建立随访关系";
            }
            default: {
                return data.canFollowUp;
            }
        }
    }

    public static boolean hasSelfRecord(List<MedicalRecord> records) {
        if (records == null) {
            return false;
        }
        for (int i = 0; i < records.size(); i++) {
            if ("本人".equals(records.get(i).getRelation())) {
                return true;
            }
        }
        return false;
    }
    public static void newRecord(Context context, List<MedicalRecord> response) {
        Bundle otherRecord = NewMedicalRecordFragment.newOtherRecord();
        if (MedicalRecordHandler.hasSelfRecord(response)) {
            Intent intent = SingleFragmentActivity.intentFor(context, "新建病历", otherRecord);
            context.startActivity(intent);
        } else {
            Bundle selfRecord = NewMedicalRecordFragment.newSelfRecord();
            Intent intent = BundlesTabActivity.intentFor(context, selfRecord, otherRecord);
            context.startActivity(intent);
        }
    }

    public static String getPatientNameRelation(Context context, MedicalRecord record) {
        return context.getString(R.string.patient_name_relation, record.getPatientName(), record.getRecordName(), record.getRelation());
    }

    public static String getGenderAndBirthday(Context context, MedicalRecord record) {
        return context.getString(R.string.gender_birth, record.getGender() == Gender.MALE ? "Male" : "Female", record.getBirthday());
    }

    public static String getPatientAddress(Context context, MedicalRecord record) {
        return context.getString(R.string.patient_address, record.getProvince(), record.getCity());
    }

    public static void onRecordSelected(MedicalRecord data) {
        EventHub.post(new SelectMedicalRecordEvent(data.getItemId(), data));
    }

    public static boolean isPending(MedicalRecord data) {
        return ReviewStatus.STATUS_PENDING.equals(data.reviewStatus);
    }

    public static void showRecordHistory(Context context, MedicalRecord data) {
        Intent i = HistoryActivity.makeIntent(context, data.getMedicalRecordId());
        context.startActivity(i);
    }
}
