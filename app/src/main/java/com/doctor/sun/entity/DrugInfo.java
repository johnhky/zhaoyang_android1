package com.doctor.sun.entity;

import java.util.List;

/**
 * Created by heky on 17/7/24.
 */

public class DrugInfo {
    String drug_name;
    String specification;
    List<String> drug_unit;

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

    public List<String> getDrug_unit() {
        return drug_unit;
    }

    public void setDrug_unit(List<String> drug_unit) {
        this.drug_unit = drug_unit;
    }


}
