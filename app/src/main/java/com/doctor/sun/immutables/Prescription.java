package com.doctor.sun.immutables;

import com.doctor.sun.R;
import com.doctor.sun.vm.BaseItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

/**
 * Created by rick on 27/10/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Immutable
@Value.Style(jdkOnly = true)
@JsonSerialize(as = ImmutablePrescription.class)
@JsonDeserialize(as = ImmutablePrescription.class)
@Value.Modifiable
public abstract class Prescription extends BaseItem {

    public abstract String getDrug_name();

    @Value.Default
    public String getScientific_name() {
        return "";
    }

    @Value.Default
    public String getFrequency() {
        return "";
    }

    @Value.Default
    public String getMorning() {
        return "";
    }

    @Value.Default
    public String getNoon() {
        return "";
    }

    @Value.Default
    public String getNight() {
        return "";
    }

    @Value.Default
    public String getBefore_sleep() {
        return "";
    }

    @Value.Default
    public String getDrug_unit() {
        return "";
    }

    @Value.Default
    public String getRemark() {
        return "";
    }

    @Value.Default
    public String getTake_medicine_days() {
        return "28";
    }

    @Value.Default
    public String getUnits() {
        return "";
    }

    @Value.Default
    public String getSpecification() {
        return "";
    }

    @Value.Default
    public String getDrug_id() {
        return "";
    }

    @Value.Default
    public String getDose_units() {
        return "";
    }

    @Value.Default
    public double getTotal_num() {
        return 0D;
    }

    @Value.Default
    public double getTotal_fee() {
        return 0D;
    }

    @JsonIgnore
    @Override
    public int getItemLayoutId() {
        if (super.getItemLayoutId() == -1) {
            return R.layout.item_prescription;
        }
        return super.getItemLayoutId();
    }

    @JsonIgnore
    @Override
    public int getLayoutId() {
        if (super.getItemLayoutId() == -1) {
            return R.layout.item_prescription3;
        }
        return super.getLayoutId();
    }

}
