package com.doctor.sun.immutables;

import com.doctor.sun.R;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.Tags;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.vm.LayoutId;
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
    //    [tid, has_pay, need_pay, display_type, display_status, cancel_reason, progress, take_time, doctor_id]
    public abstract String getId();

    @Value.Default
    public String getTid() {
        return "";
    }


    public abstract int getStatus();

    @Value.Default
    public int getHas_pay() {
        return IntBoolean.FALSE;
    }

    public abstract double getMoney();

    @Value.Default
    public double getNeed_pay() {
        return 0D;
    }


    public abstract int getType();

    @Value.Default
    public String getDisplay_type() {
        return "";
    }

    @Value.Default
    public String getDisplay_status() {
        return "";
    }


    @Value.Default
    public String getCancel_reason() {
        return "";
    }


    @Value.Default
    public String getProgress() {
        return "";
    }

    @Value.Default
    public double getTake_time() {
        return 0D;
    }

    @Value.Default
    public String getVisit_time() {
        return "";
    }

    public abstract String getEnd_time();

    public abstract String getBook_time();

    @Value.Default
    public String getTime_bucket() {
        return "";
    }

    @Value.Default
    public int getSelf() {
        return 0;
    }

    @Value.Default
    public String getDoctor_id() {
        return "";
    }

    public abstract String getRecord_id();


    public abstract List<Tags> getSelect_tags();

    @Value.Default
    public double getDoctor_point() {
        return 0D;
    }

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
