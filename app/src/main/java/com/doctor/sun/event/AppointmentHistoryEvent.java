package com.doctor.sun.event;

import android.support.v4.app.FragmentManager;

import com.doctor.sun.entity.Appointment;

import io.ganguo.library.core.event.Event;

/**
 * Created by kb on 16-10-19.
 */

public class AppointmentHistoryEvent implements Event {

    private Appointment data;
    private FragmentManager fragmentManager;

    public AppointmentHistoryEvent(Appointment data, FragmentManager fragmentManager) {
        this.data = data;
        this.fragmentManager = fragmentManager;
    }

    public Appointment getData() {
        return data;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }
}
