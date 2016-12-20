package com.doctor.sun.event;

import com.doctor.sun.dto.PatientDTO;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 20/12/2016.
 */
public class PatientProfileChangedEvent implements  Event{

    private final PatientDTO response;

    public PatientProfileChangedEvent(PatientDTO response) {
        this.response = response;
    }

    public PatientDTO getResponse() {
        return response;
    }
}
