package com.doctor.sun.event;

import com.doctor.sun.entity.constans.ImportType;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 4/2/2017.
 */

public class ImportDiagnosisEvent implements Event {
    public final String formId;
    public final String toId;
    public final int appointmentType;
    public final int type;


    public ImportDiagnosisEvent(String formId, String toId, @ImportType int type,int appointmentType) {
        this.formId = formId;
        this.toId = toId;
        this.type = type;
        this.appointmentType = appointmentType;
    }
}
