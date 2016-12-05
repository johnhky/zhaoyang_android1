package com.doctor.sun.ui.handler;

import android.content.Context;
import android.content.Intent;

import com.doctor.sun.entity.AppointmentBuilder;
import com.doctor.sun.event.SelectMedicalRecordEvent;
import com.doctor.sun.ui.activity.patient.PickDateActivity;
import com.doctor.sun.event.UnregisterMedicalRecordHandlerEvent;
import com.squareup.otto.Subscribe;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 15/11/2016.
 */
public class MedicalRecordEventHandler {
    private Context context;
    private final AppointmentBuilder builder;
    private boolean isRegister = false;

    public MedicalRecordEventHandler(AppointmentBuilder builder) {
        this.builder = builder;
    }

    public void registerTo(Context context) {
        this.context = context;
        isRegister = true;
        EventHub.register(this);
    }

    public void unregister() {
        context = null;
        isRegister = false;
        EventHub.unregister(this);
    }

    public boolean isRegister() {
        return isRegister;
    }

    @Subscribe
    public void onEventMainThread(SelectMedicalRecordEvent e) {
        if (context != null) {
            builder.setRecord(e.getRecord());
            Intent intent = PickDateActivity.makeIntent(context, builder);
            context.startActivity(intent);
        }
    }

    @Subscribe
    public void onEventMainThread(UnregisterMedicalRecordHandlerEvent e) {
        unregister();
    }
}
