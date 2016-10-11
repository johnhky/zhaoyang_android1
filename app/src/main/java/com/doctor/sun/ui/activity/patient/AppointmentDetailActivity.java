package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.pager.AnswerPagerAdapter;

/**
 * Created by rick on 1/8/2016.
 */

public class AppointmentDetailActivity extends TabActivity {
    public static final int POSITION_ANSWER = 0;
    public static final int POSITION_SUGGESTION = 1;
    public static final int POSITION_SUGGESTION_READONLY = 2;

    public static Intent makeIntent(Context context, Appointment data, int position) {
        Intent i = intentFor(context, data);
        i.putExtra(Constants.POSITION, position);
        return i;
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        Appointment parcelableExtra = getParcelableExtra(Constants.DATA);
        return new AnswerPagerAdapter(getSupportFragmentManager(), parcelableExtra);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment activeFragment = getActiveFragment(binding.vp, binding.vp.getCurrentItem());
        activeFragment.onActivityResult(requestCode, resultCode, data);
    }

    public static Intent intentFor(Context context, Appointment data) {
        Intent intent = new Intent(context, AppointmentDetailActivity.class);
        intent.putExtra(Constants.DATA, data);
        return intent;
    }
}
