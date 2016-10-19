package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;

import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.pager.DoctorAfterServiceDonePA;
import com.doctor.sun.ui.pager.PatientAfterServiceDonePA;

/**
 * Created by rick on 3/6/2016.
 */
public class AfterServiceDoneActivity extends TabActivity {
    public static Intent intentFor(Context context, String orderId, int position) {
        Intent intent = new Intent(context, AfterServiceDoneActivity.class);
        intent.putExtra(Constants.DATA, orderId);
        intent.putExtra(Constants.POSITION, position);
        return intent;
    }

    public String getData() {
        return getStringExtra(Constants.DATA);
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        if (Settings.isDoctor()) {
            return new DoctorAfterServiceDonePA(getSupportFragmentManager(), getData());
        } else {
            return new PatientAfterServiceDonePA(getSupportFragmentManager(), getData());
        }
    }


}
