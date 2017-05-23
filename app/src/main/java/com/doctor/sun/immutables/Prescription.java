package com.doctor.sun.immutables;

import com.doctor.sun.R;
import com.doctor.sun.vm.BaseItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

import java.util.ArrayList;

/**
 * Created by rick on 27/10/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Immutable
@Value.Style(jdkOnly = true)
@JsonSerialize(as = ImmutablePrescription.class)
@JsonDeserialize(as = ImmutablePrescription.class)
@Value.Modifiable
public class Prescription extends BaseItem {
    @JsonProperty("drug_name")
    public String drug_name;
    @JsonProperty("scientific_name")
    public String scientific_name;
    @JsonProperty("frequency")
    public String frequency;
    @JsonProperty("morning")
    public String morning;
    @JsonProperty("noon")
    public String noon;
    @JsonProperty("night")
    public String night;
    @JsonProperty("before_sleep")
    public String before_sleep;
    @JsonProperty("take_medicine_days")
    public String take_medicine_days;
    @JsonProperty("remark")
    public String remark;
    @JsonProperty("drug_unit")
    public String drug_unit;
    @JsonProperty("specification")
    public String specification;
    @JsonProperty("units")
    public String units;
    @JsonProperty("total_num")
    public String total_num;
    @JsonProperty("total_fee")
    public String total_fee;
    @JsonProperty("dose_units")
    public String dose_units;
    @JsonProperty("titration")
    public ArrayList<Titration> titration;

    public void setDrug_name(String drug_name) {
        this.drug_name = drug_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public void setNoon(String noon) {
        this.noon = noon;
    }

    public void setNight(String night) {
        this.night = night;
    }

    public void setBefore_sleep(String before_sleep) {
        this.before_sleep = before_sleep;
    }

    public void setTake_medicine_days(String take_medicine_days) {
        this.take_medicine_days = take_medicine_days;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setDrug_unit(String drug_unit) {
        this.drug_unit = drug_unit;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public void setDose_units(String dose_units) {
        this.dose_units = dose_units;
    }

    public void setTitration(ArrayList<Titration> titration) {
        this.titration = titration;
    }

    public String getDrug_name() {
        return drug_name;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getMorning() {
        return morning;
    }

    public String getNoon() {
        return noon;
    }

    public String getNight() {
        return night;
    }

    public String getBefore_sleep() {
        return before_sleep;
    }

    public String getTake_medicine_days() {
        return take_medicine_days;
    }

    public String getRemark() {
        return remark;
    }

    public String getDrug_unit() {
        return drug_unit;
    }

    public String getSpecification() {
        return specification;
    }

    public String getUnits() {
        return units;
    }

    public String getTotal_num() {
        return total_num;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public String getDose_units() {
        return dose_units;
    }

    public ArrayList<Titration> getTitration() {
        return titration;
    }

    @JsonIgnore
    @Override
    public int getItemLayoutId() {
        if (super.getItemLayoutId() == -1) {
            return R.layout.item_prescription;
        }
        return super.getItemLayoutId();
    }

    @Override
    public String toString() {
        return "prescription{titration:"+titration+",drug_name:"+drug_name+",drug_unit:"+drug_unit+"}";
    }
}
