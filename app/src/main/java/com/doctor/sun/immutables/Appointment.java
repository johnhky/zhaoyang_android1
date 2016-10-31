package com.doctor.sun.immutables;

import com.doctor.sun.R;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.Tags;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.vo.LayoutId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

import java.util.List;

/**
 * Created by rick on 21/10/2016.
 */
@Value.Immutable
@Value.Style(jdkOnly = true)
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

    @Value.Default
    public String getVisit_time() {
        return "";
    }

    public abstract String getEnd_time();

    public abstract String getBook_time();


    public abstract String getDoctor_id();

    public abstract String getRecord_id();



    public abstract List<Tags> getSelect_tags();

    @Value.Default
    public int getCan_edit() {
        return IntBoolean.NOT_GIVEN;
    }

    @Value.Default
    public String getYunxin_accid() {
        return "";
    }


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
