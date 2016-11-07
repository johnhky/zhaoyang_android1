package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.event.ActivityResultEvent;
import com.doctor.sun.event.AppointmentHistoryEvent;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.pager.AnswerPagerAdapter;
import com.doctor.sun.util.HistoryEventHandler;
import com.doctor.sun.util.JacksonUtils;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 1/8/2016.
 */

public class AppointmentDetailActivity extends TabActivity {
    public static final int POSITION_ANSWER = 0;
    public static final int POSITION_SUGGESTION = 1;
    public static final int POSITION_SUGGESTION_READONLY = 2;

    private HistoryEventHandler eventHandler;

    public static Intent makeIntent(Context context, Appointment data, int position) {
        Intent i = intentFor(context, data);
        i.putExtra(Constants.POSITION, position);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addHistoryButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventHandler = HistoryEventHandler.register(getSupportFragmentManager());
    }

    private void addHistoryButton() {
        if (Settings.isDoctor()) {
            View historyButton = LayoutInflater.from(this).inflate(R.layout.item_history_button, binding.flContainer, false);
            historyButton.findViewById(R.id.btn_appointment_history).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String json = getIntent().getStringExtra(Constants.DATA);
                    Appointment appointment = JacksonUtils.fromJson(json, com.doctor.sun.immutables.Appointment.class);
                    EventHub.post(new AppointmentHistoryEvent(appointment, false));
                }
            });
            binding.flContainer.addView(historyButton);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        HistoryEventHandler.unregister(eventHandler);
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        String json = getIntent().getStringExtra(Constants.DATA);
        Appointment data = JacksonUtils.fromJson(json, Appointment.class);
        return new AnswerPagerAdapter(getSupportFragmentManager(), data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        EventHub.post(new ActivityResultEvent(requestCode, resultCode, data));
    }

    public static Intent intentFor(Context context, Appointment data) {
        Intent intent = new Intent(context, AppointmentDetailActivity.class);
        intent.putExtra(Constants.DATA, JacksonUtils.toJson(data));
        return intent;
    }
}
