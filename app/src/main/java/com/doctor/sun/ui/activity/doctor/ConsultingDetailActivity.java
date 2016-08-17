package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.event.SwitchTabEvent;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.activity.patient.AppointmentDetailActivity;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.pager.ConsultingDetailPagerAdapter;
import com.doctor.sun.util.ShowCaseUtil;
import com.squareup.otto.Subscribe;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.extend.OnPageChangeAdapter;


/**
 * Created by rick on 12/16/15.
 */
public class ConsultingDetailActivity extends TabActivity
        implements Appointment.AppointmentId,
        Prescription.UrlToLoad {
    public static final int POSITION_ANSWER = 0;
    public static final int POSITION_SUGGESTION = 1;
    public static final int POSITION_SUGGESTION_READONLY = 2;
    private boolean isReadOnly;

    public static Intent makeIntent(Context context, Appointment data, int position) {
        Intent i = AppointmentDetailActivity.intentFor(context, data);
        i.putExtra(Constants.POSITION, position);
        return i;
    }


    private Appointment getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public int getPosition() {
        return getIntent().getIntExtra(Constants.POSITION, POSITION_ANSWER);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getPosition() == POSITION_SUGGESTION_READONLY) {
            isReadOnly = true;
        } else {
            isReadOnly = false;
        }
        super.onCreate(savedInstanceState);
        switchTab(new SwitchTabEvent(getPosition()));
        showCase();
    }

    private void showCase() {
        View childAt = binding.showcase;
        if (childAt != null) {
            if (Settings.isDoctor()) {
                ShowCaseUtil.showCase(childAt, "记录病历和给患者建议和调药", "diagnosisResult", 1, 0, true);
            } else {
                ShowCaseUtil.showCase(childAt, "您可以在这里看到医生的医嘱和用药建议", "diagnosisResult", 1, 0, true);
            }
        }
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        return new ConsultingDetailPagerAdapter(getSupportFragmentManager(), getData(), isReadOnly);
    }

    @Override
    protected HeaderViewModel createHeaderViewModel() {
        return null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Answer.handler.resetEditMode();
    }


    @Subscribe
    public void switchTab(SwitchTabEvent event) {
        if (event.getPosition() == -1) {
            return;
        }
        binding.vp.setCurrentItem(event.getPosition());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        switchTab(new SwitchTabEvent(isReadOnly ? 1 : -1));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getActiveFragment(binding.vp, binding.vp.getCurrentItem()).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public int getId() {
        return getData().getId();
    }

    @Override
    public String url() {
        return "diagnosis/last-drug?id=" + getId();
    }
}