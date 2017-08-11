package com.doctor.sun.entity;

/**
 * Created by heky on 17/6/12.
 */

public class Consult  {
    public int type = 0;
    public String consult_fee = "0.00";
    public String consult_num = "0";
    public String consult_explain = "";

    @Override
    public String toString() {
        return "consult{type:"+type+",consult_fee:"+consult_fee+",consult_num:"+consult_num+",consult_explain"+consult_explain+"}";
    }
}
