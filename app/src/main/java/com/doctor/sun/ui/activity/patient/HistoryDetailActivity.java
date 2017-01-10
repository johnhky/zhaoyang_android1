package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.pager.HistoryDetailAdapter;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.util.ShowCaseUtil;

/**
 * 病人端 历史纪录
 * <p/>
 * Created by lucas on 1/8/16.
 */
public class HistoryDetailActivity extends TabActivity {

    public static Intent makeIntent(Context context, Appointment appointment) {
        Intent i = new Intent(context, HistoryDetailActivity.class);
        i.putExtra(Constants.DATA, JacksonUtils.toJson(appointment));
        return i;
    }

    public static Intent makeIntent(Context context, Appointment appointment, int position) {
        Intent i = new Intent(context, HistoryDetailActivity.class);
        i.putExtra(Constants.DATA, JacksonUtils.toJson(appointment));
        i.putExtra(Constants.POSITION, position);
        return i;
    }

    private Appointment getData() {
        String json = getIntent().getStringExtra(Constants.DATA);
        return JacksonUtils.fromJson(json, Appointment.class);
    }

    @Override
    public int getPosition() {
        return getIntent().getIntExtra(Constants.POSITION, AppointmentDetailActivity.POSITION_ANSWER);
    }


    @Override
    protected PagerAdapter createPagerAdapter() {
        return new HistoryDetailAdapter(getSupportFragmentManager(), getData());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getPosition() == AppointmentDetailActivity.POSITION_SUGGESTION_READONLY) {
            binding.vp.setCurrentItem(1);
        }
        showCase();
    }

    private void showCase() {
        View childAt = binding.showcase;
        if (childAt != null) {
            if (Settings.isDoctor()) {
                ShowCaseUtil.showCase2(childAt, "记录病历和给患者建议和调药", 1, 1, 0, true);
            } else {
                ShowCaseUtil.showCase2(childAt, "您可以在这里看到医生的医嘱和处方建议", 1, 1, 0, true);
            }
        }
    }
}
