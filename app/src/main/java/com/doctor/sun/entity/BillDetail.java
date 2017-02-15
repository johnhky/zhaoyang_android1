package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by rick on 15/2/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BillDetail {
    public String consultation_fee;
    public String detail_consult_explain;
    public String detail_consult_fee;
    public String detail_consult_num;
    public String introduction;
    public String other_subsidy;
    public String prescription_subsidy;
    public String prescription_subsidy_explain;
    public String simple_consult_explain;
    public String simple_consult_fee;
    public String simple_consult_num;
    public String total_fee;
    public String total_subsidy;
}
