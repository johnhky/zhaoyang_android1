package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

/**
 * Created by rick on 25/7/2016.
 */

public class OptionsPair {


    @JsonProperty("key")
    private String key;
    @JsonProperty("val")
    private String value;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OptionsPair that = (OptionsPair) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return value;
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("value", value);
        return result;
    }
}
