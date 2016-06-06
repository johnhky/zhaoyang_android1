package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by rick on 6/6/2016.
 */
public class Area implements LayoutId, CharSequence {

    /**
     * id : 1
     * name : 广州市惠爱医院
     */

    @JsonProperty("id")
    public int id;
    @JsonProperty("name")
    public String name = "";
    @JsonProperty("child")
    public List<Area> child;

    @Override
    public int getItemLayoutId() {
        return R.layout.item_area;
    }


    @Override
    public int length() {
        return name.length();
    }


    @Override
    public char charAt(int index) {
        return name.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return name.subSequence(start, end);
    }

    @Override
    public String toString() {
        return name;
    }
}
