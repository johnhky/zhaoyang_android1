package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.event.ModifyQuestionsEvent;
import com.doctor.sun.event.RefreshQuestionsEvent;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.pager.QTemplatesInventoryPagerAdapter;
import com.squareup.otto.Subscribe;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 9/9/2016.
 * 补充问卷类
 */

public class TemplatesInventoryActivity extends TabActivity {

    private boolean isChanged = false;

    public static Intent intentFor(Context context, String appointmentId) {
        Intent intent = new Intent(context, TemplatesInventoryActivity.class);
        intent.putExtra(Constants.DATA, appointmentId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.vp.setOffscreenPageLimit(3);
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        return new QTemplatesInventoryPagerAdapter(getSupportFragmentManager(), getAppointmentId());
    }

    @Override
    public void finish() {
        super.finish();
        if (isChanged) {
            EventHub.post(new RefreshQuestionsEvent(getAppointmentId()));
        }
    }

    @Subscribe
    public void onEventMainThread(ModifyQuestionsEvent e) {
        if (e.getId().equals(getAppointmentId())) {
            isChanged = true;
        }
    }

    public String getAppointmentId() {
        return getStringExtra(Constants.DATA);
    }
}
