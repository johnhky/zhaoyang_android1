package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.vo.BaseItem;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 7/9/2016.
 */

public class ScalesResult extends BaseItem {


    /**
     * scale_count_result : 好
     * scale_point : 90
     * scale_rule_name : 规则三
     */

    @JsonProperty("scale_count_result")
    public String scaleCountResult;
    @JsonProperty("scale_point")
    public String scalePoint = "";
    @JsonProperty("scale_rule_name")
    public String scaleRuleName;

    @Override
    public int getItemLayoutId() {
        return R.layout.item_scales_result;
    }
}
