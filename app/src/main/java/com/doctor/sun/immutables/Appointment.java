package com.doctor.sun.immutables;

import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Patient;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.CaseFormat;

import org.immutables.value.Value;

/**
 * Created by rick on 21/10/2016.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableAppointment.class)
@JsonDeserialize(as = ImmutableAppointment.class)
public abstract class Appointment {

    public abstract String getTid();

    public abstract String getId();

    public abstract String getDoctor_id();

    public abstract String getRecord_id();

    public abstract String getMoney();

    public abstract String getType();

    public abstract String getNeed_pay();

    public abstract String getDisplay_type();

    public abstract String getDisplay_status();

    public abstract String getStatus();

    public abstract String getCancel_reason();

    public abstract String getHas_pay();

    public abstract String getVisit_time();

    public abstract String getTake_time();

    public abstract String getEnd_time();

    public abstract String getBook_time();

    public abstract String getProgress();

    public abstract Doctor getDoctor();

}
