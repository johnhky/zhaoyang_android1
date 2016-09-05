package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 5/9/2016.
 */

public class QTemplate2 implements LayoutId {

    /**
     * custom_amount : 0
     * default_type : 0
     * independent_amount : 3
     * scale_amount : 0
     * template_id : 14683081932d4Jz0ikQ3
     * template_name : 东方美方面
     */

    @JsonProperty("custom_amount")
    public int customAmount;
    @JsonProperty("default_type")
    public String defaultType;
    @JsonProperty("independent_amount")
    public int independentAmount;
    @JsonProperty("scale_amount")
    public String scaleAmount;
    @JsonProperty("template_id")
    public String templateId;
    @JsonProperty("template_name")
    public String templateName;

    public int getQuestionCount() {
        return customAmount + independentAmount;
    }


    @Override
    public int getItemLayoutId() {
        return R.layout.item_template2;
    }
}
