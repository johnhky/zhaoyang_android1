package com.doctor.sun.ui.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import com.doctor.sun.ui.activity.doctor.ConsultingActivity;
import com.doctor.sun.ui.activity.doctor.MainActivity;
import com.doctor.sun.ui.activity.doctor.MeActivity;

/**
 * Created by rick on 4/1/2016.
 */
public class DoctorFooterView implements FooterViewModel.FooterView {
    private Activity activity;

    private static DoctorFooterView instance;

    public static DoctorFooterView getInstance(Activity activity) {
        if (instance == null) {
            instance = new DoctorFooterView(activity);
        }else {
            instance.activity = null;
            instance.activity = activity;
        }
        return instance;
    }

    public DoctorFooterView(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void gotoTabOne() {
        startActivity(MainActivity.class);
    }

    @Override
    public void gotoTabTwo() {
        startActivity(ConsultingActivity.class);
    }

    @Override
    public void gotoTabThree() {
        startActivity(MeActivity.class);
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
