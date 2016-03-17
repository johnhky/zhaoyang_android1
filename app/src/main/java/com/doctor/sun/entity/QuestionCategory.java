package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 11/24/15.
 */
public class QuestionCategory implements LayoutId{

    /**
     * id : 7170
     * question_category_id : 3
     * category_name : 抑郁
     * formula : $total * 1.25
     * questions_total : 20
     * is_default : 1
     * section :
     */

    @JsonProperty("id")
    private int id;
    @JsonProperty("question_category_id")
    private int questionCategoryId;
    @JsonProperty("category_name")
    private String categoryName;
    @JsonProperty("formula")
    private String formula;
    @JsonProperty("questions_total")
    private int questionsTotal;
    @JsonProperty("is_default")
    private int isDefault;
    @JsonProperty("section")
    private String section;


    public void setId(int id) {
        this.id = id;
    }

    public void setQuestionCategoryId(int questionCategoryId) {
        this.questionCategoryId = questionCategoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public int getQuestionCategoryId() {
        return questionCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public void setQuestionsTotal(int questionsTotal) {
        this.questionsTotal = questionsTotal;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getFormula() {
        return formula;
    }

    public int getQuestionsTotal() {
        return questionsTotal;
    }

    public int getIsDefault() {
        return isDefault;
    }
    @Override
    public int getItemLayoutId() {
        return R.layout.item_question_category;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSection() {
        return section;
    }
}
