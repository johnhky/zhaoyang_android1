package com.doctor.sun.immutables;

import com.doctor.sun.R;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.vo.LayoutId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

/**
 * Created by rick on 21/10/2016.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableAppointment.class)
@JsonDeserialize(as = ImmutableAppointment.class)
public abstract class Appointment implements LayoutId {

    public abstract String getId();

    public abstract String getTid();


    public abstract int getStatus();

    public abstract int getHas_pay();

    public abstract double getMoney();

    public abstract double getNeed_pay();


    public abstract int getType();

    public abstract String getDisplay_type();

    public abstract String getDisplay_status();


    public abstract String getCancel_reason();


    public abstract String getProgress();

    public abstract double getTake_time();

    public abstract String getVisit_time();

    public abstract String getEnd_time();

    public abstract String getBook_time();


    public abstract String getDoctor_id();

    public abstract String getRecord_id();

    public abstract int getCan_edit();
    public abstract String getYunxin_accid();
    public abstract String getRecord_name();
    public abstract String getRelation();


    @Value.Default
    public Doctor getDoctor() {
        return new Doctor();
    }

    @Value.Default
    public MedicalRecord getRecord() {
        return new MedicalRecord();
    }


    @Value.Default
    public int getItemLayoutId() {
        return R.layout.item_appointment;
    }
}
