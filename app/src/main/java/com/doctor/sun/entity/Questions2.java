package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.vo.BaseItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 28/7/2016.
 */

public class Questions2 extends BaseItem {


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
    public int answerCount = 0;
    @JsonProperty("question_id")
    public String questionId;
    @JsonProperty("question_type")
    public String questionType;
    @JsonProperty("question_content")
    public String questionContent;
    @JsonProperty("fill_content")
    public String fillContent;
    @JsonProperty("old_question_id")
    public String oldQuestionId;
    @JsonProperty("refill")
    public int refill;
    @JsonProperty("extend_type")
    public int extendType = 0;

    @JsonProperty("option")
    public List<Options2> option;

    //不懂找颜升
    @JsonProperty("array_content")
    public List<Map<String, String>> arrayContent;

    public String positionString() {
        return String.valueOf(getPosition() / QuestionsModel.PADDING + 1);
    }


    @Override
    public int getLayoutId() {
        return R.layout.item_question2;
    }


    @Override
    public String getKey() {
        return questionId;
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        return null;
    }

    public String getOptionContent(int position) {
        if (option == null || option.size() < position) {
            return "";
        }
        return option.get(position).optionContent;
    }

    public String getOptionID(int position) {
        if (option == null || option.size() < position) {
            return "";
        }
        return option.get(position).optionId;
    }

    public boolean isAnswered(SortedListAdapter adapter, BaseViewHolder vh) {
        if (option != null) {
            for (Options2 options2 : option) {
                if (options2.getSelected()) {
                    return true;
                }
            }
        }
        if (answerCount > 0) {
            return true;
        }
        int i = adapter.inBetweenItemCount(vh.getAdapterPosition(), questionId + questionType);
        if (Math.abs(i) > 1) {
            return true;
        }

        if (questionType.equals(QuestionType.asel)) {
            return true;
        }

        return false;
    }
}
