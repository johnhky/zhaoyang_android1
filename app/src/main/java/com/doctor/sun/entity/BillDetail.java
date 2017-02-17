package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by rick on 15/2/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BillDetail {
    public String consultation_fee = "0";
    public String detail_consult_explain;
    public String detail_consult_fee = "0.00";
    public String detail_consult_num = "0";
    public String introduction;
    public String other_subsidy = "0.00";
    public String prescription_subsidy = "0.00";
    public String prescription_subsidy_explain;
    public String simple_consult_explain;
    public String simple_consult_fee = "0.00";
    public String simple_consult_num = "0";
    public String total_fee = "0";
    public String total_subsidy = "0";
}
