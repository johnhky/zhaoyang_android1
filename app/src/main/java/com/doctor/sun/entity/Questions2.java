package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Created by rick on 28/7/2016.
 */

public class Questions2 implements SortedItem {


    /**
     * option : [{"base_option_content":"保存密码","base_option_id":"1468916105WaJyrupXco","base_option_type":"A","clear_rule":0,"reply_content":"","selected":0}]
     * base_question_content : 要让360浏览器保存你的密码吗？
     * base_question_id : 1468916105DQZJhKSiga
     * base_question_type : radio
     * fill_content :
     * old_question_id : 1468305806tx8YKhpITn
     * refill : 0
     */

    @JsonIgnore
    public int position;
    @JsonProperty("base_question_id")
    public String baseQuestionId;
    @JsonProperty("base_question_type")
    public String baseQuestionType;
    @JsonProperty("base_question_content")
    public String baseQuestionContent;
    @JsonProperty("fill_content")
    public String fillContent;
    @JsonProperty("old_question_id")
    public String oldQuestionId;
    @JsonProperty("refill")
    public int refill;

    @JsonProperty("option")
    public List<Options2> option;

    //不懂找颜升
    @JsonProperty("array_content")
    public List<Map<String, String>> wtfContent;

    public String positionString() {
        return String.valueOf(position / QuestionsModel.PADDING + 1);
    }


    @Override
    public int getLayoutId() {
        return R.layout.item_question2;
    }

    @Override
    public long getCreated() {
        return -position;
    }

    @Override
    public String getKey() {
        return baseQuestionId;
    }
}
