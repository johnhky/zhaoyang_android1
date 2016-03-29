package com.doctor.sun.ui.handler;

import android.content.Intent;
import android.view.View;

import com.doctor.sun.entity.Doctor;
import com.doctor.sun.ui.activity.doctor.BreakTimeActivity;
import com.doctor.sun.ui.activity.doctor.EditDoctorInfoActivity;
import com.doctor.sun.ui.activity.doctor.FeeActivity;
import com.doctor.sun.ui.activity.doctor.SettingActivity;
import com.doctor.sun.ui.activity.doctor.TemplateActivity;
import com.doctor.sun.ui.activity.doctor.TimeActivity;

/**
 * Created by lucas on 12/4/15.
 */
public class MeHandler {
    private Doctor data;

    public MeHandler(Doctor doctor) {
        data = doctor;
    }

    public void head(View view){
        Intent intent = EditDoctorInfoActivity.makeIntent(view.getContext(),data);
        view.getContext().startActivity(intent);
    }

    public void Time(View view) {
        Intent intent = TimeActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void Price(View view){
        Intent intent = FeeActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void Disturb(View view){
        Intent intent = BreakTimeActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void Template(View view){
        Intent intent = TemplateActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void Setting(View view){
        Intent intent = SettingActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }
}
