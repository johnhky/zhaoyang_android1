package com.doctor.sun.event;

import com.doctor.sun.entity.MedicalRecord;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 15/11/2016.
 */

public class SelectMedicalRecordEvent implements Event {
    private final String TAG;
    private final MedicalRecord record;

    public SelectMedicalRecordEvent(String tag, MedicalRecord record) {
        TAG = tag;
        this.record = record;
    }

    public MedicalRecord getRecord() {
        return record;
    }
}
