package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 14/6/2016.
 */
public class DrugAutoComplete {

    /**
     * drug_name : 奥氮平片
     * product_name : 再普乐
     */

    @JsonProperty("drug_name")
    public String drugName;
    @JsonProperty("product_name")
    public String productName;

    @Override
    public String toString() {
        if (productName!=null && !productName.equals("")) {
            return drugName + "(" + productName + ")";
        }else {
            return drugName;
        }
    }
}
