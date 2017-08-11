package com.doctor.sun.entity;

import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.vm.BaseItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 15/2/2017.
 */

public class BillDetail extends BaseItem {
    public String consultation_fee = "0.00";
    public String introduction;
    public String other_subsidy = "0.00";
    public String prescription_subsidy = "0.00";
    public String prescription_subsidy_explain;
    public String total_fee = "0";
    public String total_subsidy = "0";
    public String time ="";
    public List<Consult>consult = new ArrayList<>();
    @Override
    public String toString() {
        return "data{consultation_fee:" + consultation_fee + ",consult:"+consult.toString()+"}";
    }


    public String getDetailFee(){
        Consult data =new Consult();
        for (int i = 0 ; i < consult.size();i++){
            if (consult.get(i).type== AppointmentType.PREMIUM){
                data = consult.get(i);
            }
        }
        return data.consult_explain+"";
    }


    public String getSimpleFee(){
        Consult data =new Consult();
        for (int i = 0 ; i < consult.size();i++){
            if (consult.get(i).type==AppointmentType.STANDARD){
                data = consult.get(i);
            }
        }
        return data.consult_explain+"";
    }
    public String getSurfaceFee(){
        Consult data =new Consult();
        for (int i = 0 ; i < consult.size();i++){
            if (consult.get(i).type==AppointmentType.FACE){
                data = consult.get(i);
            }
        }
        return data.consult_explain+"";
    }
}
