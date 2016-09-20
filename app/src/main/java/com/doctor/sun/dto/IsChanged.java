package com.doctor.sun.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 20/9/2016.
 */

public class IsChanged {
    @JsonProperty("is_change")
    public boolean isChanged;

    @Override
    public String toString() {
        return "IsChanged{" +
                "isChanged=" + isChanged +
                '}';
    }
}
