package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by lucas on 1/8/16.
 */
public class PatientMoney {

    /**
     * money : 0.00
     */

    @JsonProperty("money")
    private String money;

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMoney() {
        return money;
    }
}
