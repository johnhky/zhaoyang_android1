package com.doctor.sun.entity;

import com.doctor.sun.immutables.Titration;

import java.util.List;

/**
 * Created by heky on 17/6/7.
 */

public class Contents {
    private String specification;
    private List<Titration>titration;
    private String units;
    private String key;
    private String val;

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public List<Titration> getTitration() {
        return titration;
    }

    public void setTitration(List<Titration> titration) {
        this.titration = titration;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
