package com.doctor.sun.ui.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import com.doctor.sun.ui.activity.patient.PConsultingActivity;
import com.doctor.sun.ui.activity.patient.PMainActivity2;
import com.doctor.sun.ui.activity.patient.PMeActivity;

/**
 * Created by rick on 4/1/2016.
 */
public class PatientFooterView implements FooterViewModel.FooterView {
    private Activity activity;

    private static PatientFooterView instance;

    public static PatientFooterView getInstance(Activity activity) {
        if (instance == null) {
            instance = new PatientFooterView(activity);
        } else {
            instance.activity = null;
            instance.activity = activity;
        }
        return instance;
    }

    public PatientFooterView(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void gotoTabOne() {
        startActivity(PMainActivity2.class);
    }

    @Override
    public void gotoTabTwo() {
        startActivity(PConsultingActivity.class);
    }

    @Override
    public void gotoTabThree() {
        startActivity(PMeActivity.class);
    }

    protected void startActivity(Class<?> cls) {
        Intent i = new Intent(activity, cls);
        activity.startActivity(i);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.finishAffinity();
        }
        activity.overridePendingTransition(0, 0);
    }
}
