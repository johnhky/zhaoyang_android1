package com.doctor.sun.ui.model;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.DiagnosisInfo;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.entity.Symptom;
import com.doctor.sun.entity.SymptomFactory;
import com.doctor.sun.module.AuthModule;

import java.util.HashMap;
import java.util.List;

import io.ganguo.library.Config;

/**
 * 历史纪录 医生建议 viewmodel
 * <p/>
 * Created by Lynn on 1/14/16.
 */
public class DiagnosisReadOnlyViewModel {
    private Symptom perception;
    private Symptom thinking;
    private Symptom pipedream;
    private Symptom emotion;
    private Symptom memory;
    private Symptom insight;

    private Symptom currentStatus;
    private Symptom recovered;
    private Symptom treatment;
    private Symptom sideEffect;

    private Description labelSymptom;
    private Description labelConsultation;
    private Description labelAdvice;
    private Description labelAssess;
//    private Description labelPay;

    private String description;
    private String diagnosis;

    public DiagnosisReadOnlyViewModel() {
        if (!getUserType()) {
            //只有医生端显示
            perception = SymptomFactory.perceptionSymptom();
            thinking = SymptomFactory.thinkingSymptom();
            pipedream = SymptomFactory.pipedreamSymptom();
            emotion = SymptomFactory.emotionSymptom();
            memory = SymptomFactory.memorySymptom();
            insight = SymptomFactory.insightSymptom();

            currentStatus = SymptomFactory.currentStatus();
            recovered = SymptomFactory.recovered();
            treatment = SymptomFactory.treatment();
            sideEffect = SymptomFactory.sideEffect();

            labelSymptom = new Description(R.layout.item_time_category, "症状");
            labelConsultation = new Description(R.layout.item_time_category, "诊断");
            labelAssess = new Description(R.layout.item_time_category, "评估");
        }
//        else {
//            只有病人端显示
//            labelPay = new Description(R.layout.item_time_category, "支付方式");
//        }
        //两个端都有
        labelAdvice = new Description(R.layout.item_time_category, "医嘱");
    }

    public Description getLabelSymptom() {
        return labelSymptom;
    }

    public void setLabelSymptom(Description labelSymptom) {
        this.labelSymptom = labelSymptom;
    }

    public Description getLabelConsultation() {
        return labelConsultation;
    }

    public void setLabelConsultation(Description labelConsultation) {
        this.labelConsultation = labelConsultation;
    }

    public Description getLabelAdvice() {
        return labelAdvice;
    }

    public void setLabelAdvice(Description labelAdvice) {
        this.labelAdvice = labelAdvice;
    }

    public Description getLabelAssess() {
        return labelAssess;
    }

    public void setLabelAssess(Description labelAssess) {
        this.labelAssess = labelAssess;
    }

    public Symptom getPerception() {
        return perception;
    }

    public void setPerception(Symptom perception) {
        this.perception = perception;
    }

    public Symptom getThinking() {
        return thinking;
    }

    public void setThinking(Symptom thinking) {
        this.thinking = thinking;
    }

    public Symptom getPipedream() {
        return pipedream;
    }

    public void setPipedream(Symptom pipedream) {
        this.pipedream = pipedream;
    }

    public Symptom getEmotion() {
        return emotion;
    }

    public void setEmotion(Symptom emotion) {
        this.emotion = emotion;
    }

    public Symptom getMemory() {
        return memory;
    }

    public void setMemory(Symptom memory) {
        this.memory = memory;
    }

    public Symptom getInsight() {
        return insight;
    }

    public void setInsight(Symptom insight) {
        this.insight = insight;
    }

    public Symptom getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Symptom currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Symptom getRecovered() {
        return recovered;
    }

    public void setRecovered(Symptom recovered) {
        this.recovered = recovered;
    }

    public Symptom getTreatment() {
        return treatment;
    }

    public void setTreatment(Symptom treatment) {
        this.treatment = treatment;
    }

    public Symptom getSideEffect() {
        return sideEffect;
    }

    public void setSideEffect(Symptom sideEffect) {
        this.sideEffect = sideEffect;
    }
//
//    public Description getLabelPay() {
//        return labelPay;
//    }
//
//    public void setLabelPay(Description labelPay) {
//        this.labelPay = labelPay;
//    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void cloneFromDiagnosisInfo(DiagnosisInfo response) {
        if (!getUserType()) {
            perception.setStates(response.getPerception());
            thinking.setStates(response.getThinking());
            pipedream.setStates(response.getPipedream());
            emotion.setStates(response.getEmotion());
            memory.setStates(response.getMemory());
            insight.setStates(response.getInsight());

            currentStatus.setSelectedItem(response.getCurrentStatus());
            recovered.setSelectedItem(response.getRecovered());
            treatment.setSelectedItem(response.getTreatment());
            sideEffect.setSelectedItem(response.getEffect());

            description = response.getDescription();
            diagnosis = response.getDiagnosisRecord();
        }
    }

    private boolean getUserType() {
        //true - 病人端 / false - 医生端
        return Config.getInt(Constants.USER_TYPE, -1) == AuthModule.PATIENT_TYPE;
    }
}
