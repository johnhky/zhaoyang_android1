package com.doctor.sun.ui.activity.doctor;

import android.content.Intent;
import android.os.Build;

import com.doctor.sun.event.MainTabChangedEvent;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.model.FooterViewModel;
import com.squareup.otto.Subscribe;

/**
 * Created by rick on 11/30/15.
 * 医生端基类
 */
public class BaseDoctorActivity extends BaseFragmentActivity2  {

    @Subscribe
    public void onMainTabChangedEvent(MainTabChangedEvent e) {
        switch (e.getPosition()) {
            case 0: {
                startActivity(MainActivity.class);
                break;
            }
            case 1: {
                startActivity(ConsultingActivity.class);
                break;
            }
            case 2: {
                startActivity(MeActivity.class);
                break;
            }
        }
    }

    public void startActivity(Class<?> cls) {
        Intent i = new Intent(this, cls);
        startActivity(i);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
        overridePendingTransition(0, 0);
    }
}
