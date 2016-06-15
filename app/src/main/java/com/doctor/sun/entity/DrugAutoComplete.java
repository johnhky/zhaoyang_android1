package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 14/6/2016.
 */
public class DrugAutoComplete implements LayoutId {

    /**
     * drug_name : 奥氮平片
     * product_name : 再普乐
     */

    @JsonProperty("drug_name")
    public String drugName;
    @JsonProperty("product_name")
    public String productName;

    @Override
    public int getItemLayoutId() {
        return R.layout.item_drug_name;
    }

    @Override
    public String toString() {
        return drugName + "(" + productName + ")";
    }
}
