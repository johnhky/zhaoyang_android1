package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.vo.BaseItem;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 15/8/2016.
 */

public class Scales extends BaseItem{

    /**
     * scale_id : 14689161052OQbeOFbJq
     * scale_name : 焦虑
     */

    @JsonProperty("scale_id")
    public String scaleId;
    @JsonProperty("scale_name")
    public String scaleName;

    @Override
    public int getItemLayoutId() {
        return R.layout.item_empty;
    }
}
