package com.doctor.sun.event;

import com.doctor.sun.immutables.Appointment;

import java.util.List;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 8/11/2016.
 */
public class RefreshAppointmentEvent implements Event {

    private final Appointment data;

    public RefreshAppointmentEvent(Appointment data) {
        this.data = data;
    }

    public Appointment getData() {
        return data;
    }
}
