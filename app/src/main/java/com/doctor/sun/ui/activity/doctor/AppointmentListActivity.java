package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.pager.AppointmentPagerAdapter;


/**
 * Created by rick on 11/20/15.
 */
public class AppointmentListActivity extends TabActivity {

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, AppointmentListActivity.class);
        return i;
    }

    @Override
    public void onMenuClicked() {
        Intent intent = UrgentListActivity.makeIntent(this);
        startActivity(intent);
    }

    @NonNull
    protected HeaderViewModel createHeaderViewModel() {
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("已预约患者")
                .setRightTitle("紧急咨询");
        return header;
    }

    @NonNull
    protected AppointmentPagerAdapter createPagerAdapter() {
        return new AppointmentPagerAdapter(getSupportFragmentManager());
    }
}
