package com.doctor.sun.event;

import com.doctor.sun.entity.Doctor;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 12/1/2017.
 */
public class DoctorProfileChangedEvent implements Event {

    private final Doctor data;

    public DoctorProfileChangedEvent(Doctor data) {
        this.data = data;
    }

    public Doctor getData() {
        return data;
    }
}
