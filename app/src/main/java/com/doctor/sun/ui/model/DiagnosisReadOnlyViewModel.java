package com.doctor.sun.ui.model;

import android.databinding.BaseObservable;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.DiagnosisInfo;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.entity.Reminder;
import com.doctor.sun.entity.Symptom;
import com.doctor.sun.entity.SymptomFactory;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.vo.ItemTextInput;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.Config;

/**
 * 历史纪录 医生记录 viewmodel
 * <p/>
 * Created by Lynn on 1/14/16.
 */
public class DiagnosisReadOnlyViewModel extends BaseObservable {
    private Description labelSymptom;
    private Symptom perception;
    private Symptom thinking;
    private Symptom pipedream;
    private Symptom emotion;
    private Symptom memory;
    private Symptom insight;

    private Description labelConsultation;
    private Symptom currentStatus;
    private Symptom recovered;
    private Symptom treatment;
    private Symptom sideEffect;

    private Description labelAdvice;
    private ItemTextInput adviceContent;
    private ItemTextInput description;
    private Description labelAssess;
    private ItemTextInput diagnosis;

    private List<Reminder> reminderList = new ArrayList<>();
    private List<Prescription> prescriptions = new ArrayList<>();

    private Doctor doctor;
    private Reminder furtherConsultation;

    public DiagnosisReadOnlyViewModel() {
        if (!isPatient()) {
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
        description = new ItemTextInput(R.layout.item_text_option_display, "");
        adviceContent = new ItemTextInput(R.layout.item_text_option_display, "");
        diagnosis = new ItemTextInput(R.layout.item_text_option_display, "");

        //两个端都有
        labelAdvice = new Description(R.layout.item_time_category, "医嘱");
        furtherConsultation = new Reminder();
    }

    public List<Reminder> getReminderList() {
        return reminderList;
    }

    public void setReminderList(List<Reminder> reminderList) {
        this.reminderList = reminderList;
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


    public ItemTextInput getDescription() {
        return description;
    }

    public void setDescription(ItemTextInput description) {
        this.description = description;
    }

    public ItemTextInput getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(ItemTextInput diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void cloneFromDiagnosisInfo(DiagnosisInfo response) {
        if (!isPatient()) {
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
        }
        adviceContent.setInput(response.getDoctorAdvince());
        description.setInput(response.getDescription());
        diagnosis.setInput(response.getDiagnosisRecord());
        List<Reminder> reminderList = response.reminderList;
        if (reminderList != null) {
            this.reminderList.addAll(reminderList);
        }
        ArrayList<Prescription> prescription = response.getPrescription();
        if (prescription != null) {
            prescriptions.addAll(prescription);
        }
        doctor = response.getDoctorInfo();
        furtherConsultation.content = getReturnTypeAndInterval(response);
        furtherConsultation.time = response.getDate();
    }

    private boolean isPatient() {
        //true - 病人端 / false - 医生端
        return Config.getInt(Constants.USER_TYPE, -1) == AuthModule.PATIENT_TYPE;
    }

    public ArrayList<LayoutId> toList() {

        ArrayList<LayoutId> result = new ArrayList<>();
        if (!isPatient()) {
            result.add(labelSymptom);
            result.add(perception);
            result.add(thinking);
            result.add(pipedream);
            result.add(emotion);
            result.add(memory);
            result.add(insight);

            result.add(description);
            result.add(labelConsultation);
            result.add(diagnosis);

            result.add(labelAssess);
            result.add(currentStatus);
            result.add(recovered);
            result.add(treatment);
            result.add(sideEffect);
        }

        result.add(labelAdvice);
        result.add(adviceContent);
        if (!prescriptions.isEmpty()) {
            result.add(new Description(R.layout.item_time_category, "建议用药"));
            result.addAll(prescriptions);
        }
        if (furtherConsultation != null) {
            result.add(new Description(R.layout.item_time_category, "专属咨询/留言咨询/转诊"));
            result.add(furtherConsultation);
            if (doctor != null) {
                result.add(doctor);
            }
        }


        if (!reminderList.isEmpty()) {
            result.add(new Description(R.layout.item_time_category, "其它事项"));
            result.addAll(reminderList);
        }
        return result;
    }

    private String getReturnTypeAndInterval(DiagnosisInfo response) {
        if (response.getReturnX() == 1) {
            return getDiagnosisType(response.getReturnType());
        }
        return "无需复诊";
    }

    private String getDiagnosisType(int returnType) {
        String type = "";
        switch (returnType) {
            case 1:
                //专属就诊
                type = "专属就诊";
                break;
            case 2:
                //专属就诊
                type = "留言咨询";
                break;
            case 3:
                //转诊
                type = "转诊";
                break;
        }
        return type;
    }
}
