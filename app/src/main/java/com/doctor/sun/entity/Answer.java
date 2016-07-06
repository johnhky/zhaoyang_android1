package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.entity.handler.AnswerHandler;
import com.doctor.sun.vo.BaseItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rick on 11/24/15.
 */
public class Answer extends BaseItem {
    public static final AnswerHandler handler = new AnswerHandler();

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

    private int position;
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
    @JsonProperty("is_fill")
    private int isFill = 0;
    @JsonProperty("template_id")
    private int templateId;
    @JsonProperty("question_category_id")
    private int questionCategoryId;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("question")
    private Question question;
    //保存病人填写答案需要的辅助字段
    @JsonIgnore
    public boolean isDrugInit = false;
    @JsonIgnore
    private List<Integer> index;
    @JsonIgnore
    private List<Prescription> prescriptions = new ArrayList<>();
    @JsonIgnore
    private List<String> imageUrls = new ArrayList<>();
    private boolean editMode = false;
    private HashMap<String, String> selectedOptions;
    @JsonIgnore
    private String drugPath = "";


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
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

    public void setAnswerMark(int answerMark) {
        this.answerMark = answerMark;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public int getNeedRefill() {
        return needRefill;
    }

    public void setNeedRefill(int needRefill) {
        this.needRefill = needRefill;
    }

    public int getIsFill() {
        return isFill;
    }

    public void setIsFill(int isFill) {
        this.isFill = isFill;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public int getQuestionCategoryId() {
        return questionCategoryId;
    }

    public void setQuestionCategoryId(int questionCategoryId) {
        this.questionCategoryId = questionCategoryId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<Integer> getIndex() {
        return index;
    }

    public void setIndex(List<Integer> index) {
        this.index = index;
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


    public HashMap<String, String> getSelectedOptions() {
        if (selectedOptions == null) {
            selectedOptions = new HashMap<>();

            try {
                List<String> types = (List<String>) getAnswerType();
                List<String> contents = (List<String>) getAnswerContent();
                for (int i = 0; i < types.size(); i++) {
                    selectedOptions.put(types.get(i), contents.get(i));
                }
            } catch (ClassCastException ignore) {
            }
        }
        return selectedOptions;
    }

    public void setSelectedOptions(HashMap<String, String> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public boolean hasSelectedOptions() {
        return !getSelectedOptions().isEmpty();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_answer;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public String getDrugPath() {
        return drugPath;
    }

    public void setDrugPath(String drugPath) {
        this.drugPath = drugPath;
    }
}
