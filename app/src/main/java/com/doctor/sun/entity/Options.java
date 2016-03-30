package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.entity.handler.OptionsHandler;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 11/24/15.
 */
public class Options implements LayoutId {
    public static final OptionsHandler handler = new OptionsHandler();
    @JsonProperty("option_type")
    private String optionType;
    @JsonProperty("option_content")
    private String optionContent;
    @JsonProperty("option_mark")
    private int optionMark;
    @JsonIgnore
    private String optionInput;

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
    }

    public void setOptionMark(int optionMark) {
        this.optionMark = optionMark;
    }

    public String getOptionType() {
        return optionType;
    }

    public String getOptionContent() {
        return optionContent;
    }

    public int getOptionMark() {
        return optionMark;
    }

    public String getOptions(){
        return getOptionType()+"."+getOptionContent();
    }

    public String getOptionInput() {
        return optionInput;
    }

    public void setOptionInput(String optionInput) {
        this.optionInput = optionInput;
    }

    @Override
    public String toString() {
        return "Options{" +
                "optionType='" + optionType + '\'' +
                ", optionContent='" + optionContent + '\'' +
                ", optionMark=" + optionMark +
                '}';
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_options;
    }
}
