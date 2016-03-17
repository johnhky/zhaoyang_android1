package com.doctor.sun.ui.activity.patient.handler;

import android.content.Intent;
import android.view.View;

import com.doctor.sun.entity.Patient;
import com.doctor.sun.ui.activity.doctor.SettingActivity;
import com.doctor.sun.ui.activity.patient.DocumentActivity;
import com.doctor.sun.ui.activity.patient.EditPatientInfoActivity;
import com.doctor.sun.ui.activity.patient.HistoryActivity;
import com.doctor.sun.ui.activity.patient.RechargeActivity;
import com.doctor.sun.ui.activity.patient.RecordListActivity;

/**
 * Created by lucas on 1/4/16.
 */
public class MeHandler {
    private Patient data;

    public MeHandler(Patient patient) {
        data = patient;
    }

    public void info(View view) {
        Intent intent = EditPatientInfoActivity.makeIntent(view.getContext(), data);
        view.getContext().startActivity(intent);
    }

    public void History(View view) {
        Intent intent = HistoryActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void Record(View view) {
        Intent intent = RecordListActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void Document(View view) {
        Intent intent = DocumentActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void Recharge(View view) {
        Intent intent = RechargeActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void Setting(View view) {
        Intent intent = SettingActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void setData(Patient data) {
        this.data = data;
    }
}
