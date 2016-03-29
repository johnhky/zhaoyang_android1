package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 11/24/15.
 */
public class Answer implements LayoutId {

    /**
     * id : 50
     * appointment_id : 1
     * question_id : 1
     * answer_type : null
     * answer_content : null
     * answer_mark : 0
     * is_public : 1
     * need_refill : 0
     * created_at : 2015-08-10 17:56:51
     * updated_at : 2015-08-10 17:56:51
     * question : {"id":1,"question_type":"radio","question_content":"请问这是患者第一次找心理或精神科医生就诊吗？","options":[{"option_type":"A","option_content":"是","option_mark":0},{"option_type":"B","option_content":"否","option_mark":0}]}
     */

    /**
     * 增加字段, 被选中CompoundButton 索引
     * index: [1, 2]
     */

    @JsonProperty("id")
    private int id;
    @JsonProperty("appointment_id")
    private int appointmentId;
    @JsonProperty("question_id")
    private int questionId;
    @JsonProperty("answer_type")
    private Object answerType;
    @JsonProperty("answer_content")
    private Object answerContent;
    @JsonProperty("answer_mark")
    private int answerMark;
    @JsonProperty("is_public")
    private int isPublic;
    @JsonProperty("need_refill")
    private int needRefill;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    /**
     * id : 1
     * question_type : radio
     * question_content : 请问这是患者第一次找心理或精神科医生就诊吗？
     * options : [{"option_type":"A","option_content":"是","option_mark":0},{"option_type":"B","option_content":"否","option_mark":0}]
     */

    @JsonProperty("question")
    private Question question;
    /**
     * is_fill : 1
     */

    @JsonProperty("is_fill")
    private int isFill;
    /**
     * template_id : 0
     */

    @JsonProperty("template_id")
    private int templateId;
    /**
     * question_category_id : 0
     */

    @JsonProperty("question_category_id")
    private int questionCategoryId;
    @JsonIgnore
    //保存病人填写答案需要的辅助字段
    private List<Integer> index;
    @JsonIgnore
    private List<Prescription> prescriptions = new ArrayList<>();
    @JsonIgnore
    private List<String> imageUrls = new ArrayList<>();

    public void setId(int id) {
        this.id = id;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }


    public void setAnswerMark(int answerMark) {
        this.answerMark = answerMark;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public void setNeedRefill(int needRefill) {
        this.needRefill = needRefill;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public Object getAnswerType() {
        return answerType;
    }

    public void setAnswerType(Object answerType) {
        this.answerType = answerType;
    }

    public Object getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(Object answerContent) {
        this.answerContent = answerContent;
    }

    public int getAnswerMark() {
        return answerMark;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public int getNeedRefill() {
        return needRefill;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Question getQuestion() {
        return question;
    }

    public List<Integer> getIndex() {
        return index;
    }

    public void setIndex(List<Integer> index) {
        this.index = index;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_answer;
    }

    public void setIsFill(int isFill) {
        this.isFill = isFill;
    }

    public int getIsFill() {
        return isFill;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setQuestionCategoryId(int questionCategoryId) {
        this.questionCategoryId = questionCategoryId;
    }

    public int getQuestionCategoryId() {
        return questionCategoryId;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
