package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.event.AppointmentHistoryEvent;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.pager.DoctorAfterServicePA;
import com.doctor.sun.ui.pager.PatientAfterServicePA;
import com.doctor.sun.util.HistoryEventHandler;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 3/6/2016.
 */
public class AfterServiceDoingActivity extends TabActivity implements Prescription.UrlToLoad {

    private HistoryEventHandler eventHandler;

    public static Intent intentFor(Context context, String id, String recordId, int position) {
        Intent intent = new Intent(context, AfterServiceDoingActivity.class);
        intent.putExtra(Constants.DATA, id);
        intent.putExtra(Constants.PARAM_RECORD_ID, recordId);
        intent.putExtra(Constants.POSITION, position);
        return intent;
    }

    public String getData() {
        return getStringExtra(Constants.DATA);
    }

    public String getRecordId() {
        return getStringExtra(Constants.PARAM_RECORD_ID);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Settings.isDoctor()) {
            View historyButton = LayoutInflater.from(this).inflate(R.layout.item_history_button, binding.flContainer, false);
            historyButton.findViewById(R.id.btn_appointment_history).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventHub.post(new AppointmentHistoryEvent());
                }
            });
            binding.flContainer.addView(historyButton);
        }

        eventHandler = HistoryEventHandler.register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HistoryEventHandler.ungister(eventHandler);
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        if (Settings.isDoctor()) {
            return new DoctorAfterServicePA(getSupportFragmentManager(), getData());
        } else {
            return new PatientAfterServicePA(getSupportFragmentManager(), getData());
        }
    }

    @Override
    public String url() {
        return "drug/record-last?recordId=" + getRecordId();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getActiveFragment(binding.vp, binding.vp.getCurrentItem());
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
