package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 2/2/2016.
 */
public class QuestionStats implements LayoutId {

    /**
     * question_category_id : 1
     * sum_mark : 0
     * mark_status : 正常
     * result_mark : 0
     */

    @JsonProperty("question_category_id")
    private int questionCategoryId;
    @JsonProperty("sum_mark")
    private String sumMark;
    @JsonProperty("mark_status")
    private String markStatus;
    @JsonProperty("result_mark")
    private int resultMark;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuestionCategoryId(int questionCategoryId) {
        this.questionCategoryId = questionCategoryId;
    }

    public void setSumMark(String sumMark) {
        this.sumMark = sumMark;
    }

    public void setMarkStatus(String markStatus) {
        this.markStatus = markStatus;
    }

    public void setResultMark(int resultMark) {
        this.resultMark = resultMark;
    }

    public int getQuestionCategoryId() {
        return questionCategoryId;
    }

    public String getSumMark() {
        return sumMark;
    }

    public String getMarkStatus() {
        return markStatus;
    }

    public int getResultMark() {
        return resultMark;
    }

    public String getDescription() {
       return name+"得分:" + getSumMark() + "/"+ getMarkStatus();
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_question_stats;
    }
}
