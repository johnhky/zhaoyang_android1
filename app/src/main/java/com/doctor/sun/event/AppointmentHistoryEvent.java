package com.doctor.sun.event;

import com.doctor.sun.immutables.Appointment;

import io.ganguo.library.core.event.Event;

/**
 * Created by kb on 16-10-19.
 */

public class AppointmentHistoryEvent implements Event {

    private final boolean historyList;
    private Appointment data;

    public AppointmentHistoryEvent(Appointment data,boolean historyList) {
        this.data = data;
        this.historyList = historyList;
    }

    public Appointment getData() {
        return data;
    }

    public boolean isHistoryList() {
        return historyList;
    }
}
