package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.pager.AnswerPagerAdapter;

/**
 * Created by rick on 1/8/2016.
 */

public class AppointmentDetailActivity extends TabActivity {
    @Override
    protected PagerAdapter createPagerAdapter() {
        Appointment parcelableExtra = getParcelableExtra(Constants.DATA);
        return new AnswerPagerAdapter(getSupportFragmentManager(), parcelableExtra);
    }

    @Override
    protected HeaderViewModel createHeaderViewModel() {
        return null;
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
