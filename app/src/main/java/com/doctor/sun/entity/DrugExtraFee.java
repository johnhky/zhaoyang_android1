package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 31/10/2016.
 */

public class DrugExtraFee {

    //    1表示其他收费(存在多条收费情况)，没有为空
    @JsonProperty("1")
    public List<String> extraFee = new ArrayList<>();
    //    2表示寄药优惠，没有为空
    @JsonProperty("2")
    public List<String> discount = new ArrayList<>();
    //    3表示平台服务费，没有为空
    @JsonProperty("3")
    public List<String> commission = new ArrayList<>();


    @Override
    public String toString() {
        return "extraFee{extraFee:"+extraFee+",discount:"+discount+",commission:"+commission+"}";
    }
}
