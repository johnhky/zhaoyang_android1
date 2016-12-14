package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by rick on 14/6/2016.
 */
public class DrugAutoComplete {

    public static final String REGEX = "[\\s()（）.。,，]+";
    /**
     * drug_name : 奥氮平片
     * product_name : 再普乐
     */

    @JsonProperty("drug_name")
    public String drugName;
    @JsonProperty("product_name")
    public String productName;
    @JsonProperty("drug_unit")
    public List<String> drugUnit;
    private String drugNameCopy;

    @Override
    public String toString() {
        if (productName != null && !productName.equals("")) {
            return drugName + "(" + productName + ")";
        } else {
            return drugName;
        }
    }

    public boolean contains(String string) {
        return getDrugNameWithOutNonWordChar().contains(string.replaceAll(REGEX, ""));
    }

    public String getDrugNameWithOutNonWordChar() {
        if (drugNameCopy == null) {
            drugNameCopy = drugName.replaceAll(REGEX, "");
        }
        return drugNameCopy;
    }

}
