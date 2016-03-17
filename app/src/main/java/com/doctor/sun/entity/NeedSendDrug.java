package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by lucas on 2/16/16.
 */
public class NeedSendDrug {

    /**
     * need : 1(0不需要寄药 1需要寄药)
     */

    @JsonProperty("need")
    private String need;

    public void setNeed(String need) {
        this.need = need;
    }

    public String getNeed() {
        return need;
    }

    @Override
    public String toString() {
        return "NeedSendDrug{" +
                "need='" + need + '\'' +
                '}';
    }
}
