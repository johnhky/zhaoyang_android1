package com.doctor.sun.ui.activity.doctor;

import android.content.Intent;
import android.os.Build;

import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.model.FooterViewModel;

/**
 * Created by rick on 11/30/15.
 */
public class BaseDoctorActivity extends BaseActivity2 implements FooterViewModel.FooterView {
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

    private void startActivity(Class<?> cls) {
        Intent i = new Intent(this, cls);
        startActivity(i);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
        overridePendingTransition(0, 0);
    }
}
