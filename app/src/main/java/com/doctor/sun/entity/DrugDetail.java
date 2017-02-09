package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Strings;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rick on 7/2/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugDetail extends RealmObject {


    @PrimaryKey
    private String id;
    @Index
    private String drug_name;
    private String specification;
    private String units;
    private String per_units;
    private String size;
    private String price;
    private String dose_units;
    private String in_bulk;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDrug_name() {
        return drug_name;
    }

    public void setDrug_name(String drug_name) {
        this.drug_name = drug_name;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getPer_units() {
        return per_units;
    }

    public void setPer_units(String per_units) {
        this.per_units = per_units;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDose_units() {
        return dose_units;
    }

    public void setDose_units(String dose_units) {
        this.dose_units = dose_units;
    }

    public String getIn_bulk() {
        return in_bulk;
    }

    public void setIn_bulk(String in_bulk) {
        this.in_bulk = in_bulk;
    }

    @Override
    public String toString() {
        return drug_name;
    }

    public ArrayList<String> drugUnit() {
        ArrayList<String> drugUnit = new ArrayList<>();
        for (DrugDetail e : Realm.getDefaultInstance().where(DrugDetail.class)
                .equalTo("drug_name", drug_name).findAll()) {
            if (!Strings.isNullOrEmpty(e.units)) {
                drugUnit.add(e.units);
            }
            if (!Strings.isNullOrEmpty(e.per_units)) {
                drugUnit.add(e.per_units);
            }
        }
        return drugUnit;
    }
}
