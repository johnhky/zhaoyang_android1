package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

/**
 * Created by rick on 11/24/15.
 * 问题
 */
public class Question implements LayoutId {
    @Override
    public int getItemLayoutId() {
        return R.layout.item_empty;
    }
//    public static final String TYPE_FILL = "fill";
//    public static final String TYPE_RADIO = "radio";
//    public static final String TYPE_CHECKBOX = "checkbox";
//    public static final String TYPE_UPLOADS = "uploads";
//    public static final String TYPE_PILLS = "fills";
//    public static final String TYPE_TIME = "dtime";
//    public static final String TYPE_SEL = "sel";
//    public static final String TYPE_DROP_DOWN = "asel";
//    public static final String TYPE_FURTHER_CONSULTATION = "keepon";
//    public static final String REMINDER = "alarmclock";
//
//    @JsonIgnore
//    public boolean isSelected = false;
//    @JsonProperty("is_public")
//    private int isPublic;
//    @JsonProperty("question_category_id")
//    private int questionCategoryId;
//    @JsonProperty("is_library")
//    private int isLibrary;
//    @JsonProperty("is_enable")
//    private int isEnable;
//    @JsonProperty("id")
//    private int id;
//    @JsonProperty("question_type")
//    private String questionType;
//    @JsonProperty("question_content")
//    private String questionContent;
//    @JsonProperty("doctor_id")
//    private String doctorId;
//    @JsonProperty("clear_option")
//    private String clearOption;
//    @JsonProperty("options")
//    private List<Options> options;
//    private QuestionHandler handler = new QuestionHandler(this);
//
//    public boolean getIsSelected() {
//        return isSelected;
//    }
//
//    public void setIsSelected(boolean isSelected) {
//        this.isSelected = isSelected;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getQuestionType() {
//        return questionType;
//    }
//
//    public void setQuestionType(String questionType) {
//        this.questionType = questionType;
//    }
//
//    public String getQuestionContent() {
//        return questionContent;
//    }
//
//    public void setQuestionContent(String questionContent) {
//        this.questionContent = questionContent;
//    }
//
//    public List<Options> getOptions() {
//        return options;
//    }
//
//    public void setOptions(List<Options> options) {
//        this.options = options;
//    }
//
//    public QuestionHandler getHandler() {
//        return handler;
//    }
//
//    public void setHandler(QuestionHandler handler) {
//        this.handler = handler;
//    }
//
//    @Override
//    public String toString() {
//        return "Question{" +
//                "id=" + id +
//                ", questionType='" + questionType + '\'' +
//                ", questionContent='" + questionContent + '\'' +
//                ", options=" + options +
//                '}';
//    }
//
//    @Override
//    public int getItemLayoutId() {
//        return R.layout.item_question;
//    }
//
//    public String getDoctorId() {
//        return doctorId;
//    }
//
//    public void setDoctorId(String doctorId) {
//        this.doctorId = doctorId;
//    }
//
//    public int getIsPublic() {
//        return isPublic;
//    }
//
//    public void setIsPublic(int isPublic) {
//        this.isPublic = isPublic;
//    }
//
//    public int getQuestionCategoryId() {
//        return questionCategoryId;
//    }
//
//    public void setQuestionCategoryId(int questionCategoryId) {
//        this.questionCategoryId = questionCategoryId;
//    }
//
//    public int getIsLibrary() {
//        return isLibrary;
//    }
//
//    public void setIsLibrary(int isLibrary) {
//        this.isLibrary = isLibrary;
//    }
//
//    public int getIsEnable() {
//        return isEnable;
//    }
//
//    public void setIsEnable(int isEnable) {
//        this.isEnable = isEnable;
//    }
//
//    public String getClearOption() {
//        return clearOption;
//    }
//
//    public void setClearOption(String clearOption) {
//        this.clearOption = clearOption;
//    }
//
//    public String contents() {
//
//        return "." + getQuestionContent();
//    }
//
//    @Retention(RetentionPolicy.SOURCE)
//    @StringDef({TYPE_FILL, TYPE_CHECKBOX, TYPE_RADIO, TYPE_PILLS, TYPE_UPLOADS})
//    public @interface TYPE {
//
//    }
}
