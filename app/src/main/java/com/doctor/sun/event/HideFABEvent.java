package com.doctor.sun.event;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 2/12/2016.
 */
public class HideFABEvent implements Event {

    private String appointmentId;

    public HideFABEvent() {}

    public HideFABEvent(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }
}
