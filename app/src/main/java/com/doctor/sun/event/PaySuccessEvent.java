package com.doctor.sun.event;

import com.doctor.sun.entity.Appointment;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 12/10/2016.
 */
public class PaySuccessEvent implements Event {

    private final Appointment data;

    public PaySuccessEvent(Appointment data) {
        this.data = data;
    }

    public PaySuccessEvent() {
        this.data = null;
    }

    public Appointment getData() {
        return data;
    }
}
