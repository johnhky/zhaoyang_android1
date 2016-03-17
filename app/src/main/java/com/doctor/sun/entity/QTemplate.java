package com.doctor.sun.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.handler.TemplateHandler;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 11/26/15.
 */
public class QTemplate implements LayoutId ,Parcelable{


    /**
     * id : 2
     * doctor_id : 1
     * template_name : 模板名称sadasd
     * is_default : 0
     * created_at : 2015-08-06 14:22:02
     * updated_at : 2015-08-06 14:22:02
     * question : [{"id":21,"question_type":"radio","question_content":"请问这是患者第一次找心理或者精神科医生就诊吗?","option":[{"option_type":"A","option_content":"是","option_mark":0},{"option_type":"B","option_content":"否","option_mark":0}]},{"id":22,"question_type":"radio","question_content":"其他医生曾给你的诊断是?","option":[{"option_type":"A","option_content":"fill","option_mark":0},{"option_type":"B","option_content":"不详","option_mark":0}]}]
     */

    @JsonProperty("id")
    private int id;
    @JsonProperty("doctor_id")
    private int doctorId;
    @JsonProperty("template_name")
    private String templateName;
    @JsonProperty("is_default")
    private int isDefault;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("question_count")
    private String questionCount;

    public String getQuestionCount() {
        return questionCount;
    }

    /**
     * id : 21
     * question_type : radio
     * question_content : 请问这是患者第一次找心理或者精神科医生就诊吗?
     * option : [{"option_type":"A","option_content":"是","option_mark":0},{"option_type":"B","option_content":"否","option_mark":0}]
     */

    @JsonProperty("question")
    private List<Question> question;

    public void setId(int id) {
        this.id = id;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setQuestion(List<Question> question) {
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public List<Question> getQuestion() {
        return question;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_question_template;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.doctorId);
        dest.writeString(this.templateName);
        dest.writeInt(this.isDefault);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.questionCount);
        dest.writeList(this.question);
    }

    public QTemplate() {
    }

    private TemplateHandler handler = new TemplateHandler(this);

    public TemplateHandler getHandler() {
        return handler;
    }

    public void setHandler(TemplateHandler handler) {
        this.handler = handler;
    }

    protected QTemplate(Parcel in) {
        this.id = in.readInt();
        this.doctorId = in.readInt();
        this.templateName = in.readString();
        this.isDefault = in.readInt();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.questionCount = in.readString();
        this.question = new ArrayList<Question>();
        in.readList(this.question, List.class.getClassLoader());
    }

    public static final Creator<QTemplate> CREATOR = new Creator<QTemplate>() {
        public QTemplate createFromParcel(Parcel source) {
            return new QTemplate(source);
        }

        public QTemplate[] newArray(int size) {
            return new QTemplate[size];
        }
    };

    @Override
    public String toString() {
        return "QTemplate{" +
                "id=" + id +
                ", doctorId=" + doctorId +
                ", templateName='" + templateName + '\'' +
                ", isDefault=" + isDefault +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", questionCount='" + questionCount + '\'' +
                ", question=" + question +
                ", handler=" + handler +
                '}';
    }

}
