package com.doctor.sun.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.FragmentDiagnosisBinding;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.DiagnosisInfo;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Symptom;
import com.doctor.sun.entity.SymptomFactory;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.fragment.EditPrescriptionsFragment;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.vm.ItemButton;
import com.doctor.sun.vm.ItemPickDSchedule;
import com.doctor.sun.vm.ItemPickTime;
import com.doctor.sun.vm.ItemRadioGroup;
import com.doctor.sun.vm.ItemReminderList;
import com.doctor.sun.vm.ItemTextInput;
import com.google.common.base.Strings;

import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by rick on 25/12/2015.
 */
public class DiagnosisModel {


    public static final String DETAIL = "专属咨询";
    public static final String QUICK = "闲时咨询";
    private final Context context;

    private String advice = "";

    private Symptom perception;
    private Symptom thinking;
    private Symptom pipedream;
    private Symptom emotion;
    private Symptom memory;
    private Symptom insight;
    private ItemTextInput description;
    private ItemTextInput diagnosisRecord;
    private Symptom currentStatus;
    private Symptom recovered;
    private Symptom treatment;
    private Symptom sideEffect;

    private ItemPickDSchedule date;
    private ItemPickTime time;
    private ItemTextInput money;

    private ItemRadioGroup returnType;

    private final Description labelEval;
    private Description labelSymptom;
    private Description labelConsultation;
    private Description labelAllCanSee;
    private ItemButton btnGotoTabOne;
    private ItemButton chooseDoctor;
    private Doctor doctor;

    public ItemReminderList reminderList;

    public DiagnosisModel(final Activity context) {
        this.context = context;
        perception = SymptomFactory.perceptionSymptom();
        thinking = SymptomFactory.thinkingSymptom();
        pipedream = SymptomFactory.pipedreamSymptom();
        emotion = SymptomFactory.emotionSymptom();
        memory = SymptomFactory.memorySymptom();
        insight = SymptomFactory.insightSymptom();

        description = new ItemTextInput(R.layout.item_description_input, "病情的简单描述");
        diagnosisRecord = new ItemTextInput(R.layout.item_description_input, "请按ICD或DSM规范填写");

        currentStatus = SymptomFactory.currentStatus();
        recovered = SymptomFactory.recovered();
        treatment = SymptomFactory.treatment();
        sideEffect = SymptomFactory.sideEffect();

        date = new ItemPickDSchedule(R.layout.item_pick_doctor_schedule, "复诊日期");
        date.setYear(date.getYear() + 18);
        time = new ItemPickTime(R.layout.item_pick_time, "复诊时间");
        money = new ItemTextInput(R.layout.item_text_input, "复诊诊金(元/次/半小时)");
        returnType = new ItemRadioGroup(R.layout.item_return_type);

        labelSymptom = new Description(R.layout.item_description, "症状");
        labelConsultation = new Description(R.layout.item_description, "诊断");
        labelEval = new Description(R.layout.item_description, "评估");

        labelAllCanSee = new Description(R.layout.item_description, "以下部分为病人可见");
        btnGotoTabOne = new ItemButton(R.layout.item_edit_prescription, "修改处方") {
            @Override
            public void onClick(View view) {
                View focusCurrent = ((Activity) view.getContext()).getWindow().getCurrentFocus();
                if (focusCurrent != null) {
                    focusCurrent.clearFocus();
                }
                Bundle args = EditPrescriptionsFragment.getArgs(null, false);
                Intent intent = SingleFragmentActivity.intentFor(context, "添加/编辑用药", args);
                context.startActivityForResult(intent, Constants.PRESCRITION_REQUEST_CODE);
            }
        };
        reminderList = new ItemReminderList();
        reminderList.adapter(context);
    }

    public void setReturnType(String type) {
        date.setTitle(String.format("%s日期", type));
        time.setTitle(String.format("%s时间", type));
        money.setTitle(String.format("%s诊金(元/次/半小时)", type));
    }

    public void cloneFromDiagnosisInfo(DiagnosisInfo response) {


        cloneAll(response);

        money.setInput(String.valueOf(response.getMoney()));
        date.setDate(response.getDate());
        returnType.setSelectedItem(response.getReturnType());
        doctor = response.getDoctorInfo();
        reminderList.addReminders(response.reminderList);
    }

    public void cloneAll(DiagnosisInfo response) {
        cloneDiagnosisRecord(response);

//        time.setTime(response.getTime());
        cloneAdvice(response);
    }

    public void cloneDiagnosisRecord(DiagnosisInfo response) {
        perception.setStates(response.getPerception());
        thinking.setStates(response.getThinking());
        pipedream.setStates(response.getPipedream());
        emotion.setStates(response.getEmotion());
        memory.setStates(response.getMemory());
        insight.setStates(response.getInsight());
        description.setInput(response.getDescription());
        diagnosisRecord.setInput(response.getDiagnosisRecord());

        currentStatus.setSelectedItem(response.getCurrentStatus());
        recovered.setSelectedItem(response.getRecovered());
        treatment.setSelectedItem(response.getTreatment());
        sideEffect.setSelectedItem(response.getEffect());
    }

    public void cloneAdvice(DiagnosisInfo response) {
        advice = response.getDoctorAdvince();
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

    public ItemTextInput getDescription() {
        return description;
    }

    public void setDescription(ItemTextInput description) {
        this.description = description;
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

    public ItemPickDSchedule getDate() {
        return date;
    }

    public void setDate(ItemPickDSchedule date) {
        this.date = date;
    }

    public ItemPickTime getTime() {
        return time;
    }

    public void setTime(ItemPickTime time) {
        this.time = time;
    }

    public ItemTextInput getMoney() {
        return money;
    }

    public void setMoney(ItemTextInput money) {
        this.money = money;
    }


    public ItemRadioGroup getReturnType() {
        return returnType;
    }

    public void setReturnType(ItemRadioGroup returnType) {
        this.returnType = returnType;
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

    public Description getLabelAllCanSee() {
        return labelAllCanSee;
    }

    public void setLabelAllCanSee(Description labelAllCanSee) {
        this.labelAllCanSee = labelAllCanSee;
    }

    public ItemButton getBtnGotoTabOne() {
        return btnGotoTabOne;
    }

    public void setBtnGotoTabOne(ItemButton btnGotoTabOne) {
        this.btnGotoTabOne = btnGotoTabOne;
    }


    public ItemButton getChooseDoctor() {
        return chooseDoctor;
    }

    public void setChooseDoctor(ItemButton chooseDoctor) {
        this.chooseDoctor = chooseDoctor;
    }

    @Override
    public String toString() {
        return "DiagnosisModel{" +
                ", perception=" + perception +
                ", thinking=" + thinking +
                ", pipedream=" + pipedream +
                ", emotion=" + emotion +
                ", memory=" + memory +
                ", insight=" + insight +
                ", description=" + description +
                ", currentStatus=" + currentStatus +
                ", recovered=" + recovered +
                ", treatment=" + treatment +
                ", sideEffect=" + sideEffect +
                ", date=" + date +
                ", time=" + time +
                ", money=" + money +
                ", returnType=" + returnType +
                ", labelSymptom=" + labelSymptom +
                ", labelConsultation=" + labelConsultation +
                ", labelAllCanSee=" + labelAllCanSee +
                ", btnGotoTabOne=" + btnGotoTabOne +
                '}';
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }


    public HashMap<String, String> toHashMap(String appointmentId, String recordId, FragmentDiagnosisBinding binding, String prescriptions) {
        HashMap<String, String> result = new HashMap<>();
        result.put("appointmentId", appointmentId);
        result.put("is_diagnosis", "1");
        result.put("perception", perception.toStates());
        result.put("thinking", thinking.toStates());
        result.put("pipedream", pipedream.toStates());
        result.put("emotion", emotion.toStates());
        result.put("memory", memory.toStates());
        result.put("insight", insight.toStates());
        result.put("description", binding.description.etOthers.getText().toString());
        result.put("diagnosis_record", binding.diagnosisRecord.etOthers.getText().toString());
        result.put("current_status", String.valueOf(currentStatus.getSelectedItem()));
        result.put("recovered", String.valueOf(recovered.getSelectedItem()));
        result.put("treatment", String.valueOf(treatment.getSelectedItem()));
        result.put("effect", String.valueOf(sideEffect.getSelectedItem()));
        if (prescriptions != null && !"".equals(prescriptions)) {
            result.put("prescription", prescriptions);
        }
        result.put("doctor_advince", binding.doctorAdvice.etOthers.getText().toString());
        boolean needReturn = binding.needReturn.switchButton.isChecked();
        result.put("return", needReturn ? "1" : "0");
        if (needReturn) {
            int selectedItem = returnType.getSelectedItem();
            if (selectedItem < 0) {
                selectedItem = 1;
            }
            int returnType = selectedItem;

            result.put("returnType", String.valueOf(returnType));
            result.put("recordId", recordId);

            GregorianCalendar gregorianCalendar = new GregorianCalendar(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), time.getmBeginHour(), time.getmBeginMinute());
            String s = String.valueOf(gregorianCalendar.getTimeInMillis());
            if (s.length() >= 10) {
                result.put("returnTime", s.substring(0, 10));
            } else {
                result.put("returnTime", s);
            }
            if (doctor != null && (returnType == 3)) {
                result.put("doctorRequire", String.valueOf(doctor.getId()));
            }
        }
        result.put("reminder", JacksonUtils.toJson(reminderList.getReminders()));
        return result;
    }

    public ItemTextInput getDiagnosisRecord() {
        return diagnosisRecord;
    }

    public void setDiagnosisRecord(ItemTextInput diagnosisRecord) {
        this.diagnosisRecord = diagnosisRecord;
    }

    public Description getLabelEval() {
        return labelEval;
    }

    public String getAdvice() {
        if (Strings.isNullOrEmpty(advice)) {
            return "坚持治疗,定期复诊";
        }
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}
