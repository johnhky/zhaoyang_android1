package com.doctor.sun.event;

import io.ganguo.library.core.event.Event;

/**
 * Created by kb on 16/12/2016.
 */

public class SelectAppointmentTypeEvent implements Event{

    private int type;

    public SelectAppointmentTypeEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
