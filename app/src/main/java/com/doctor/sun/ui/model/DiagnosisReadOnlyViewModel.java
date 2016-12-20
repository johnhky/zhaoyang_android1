package com.doctor.sun.ui.model;

import android.databinding.BaseObservable;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.DiagnosisInfo;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Reminder;
import com.doctor.sun.entity.Symptom;
import com.doctor.sun.entity.SymptomFactory;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.vm.ItemTextInput;
import com.doctor.sun.vm.LayoutId;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.Config;

/**
 * 历史纪录 医生记录 viewmodel
 * <p/>
 * Created by Lynn on 1/14/16.
 */
public class DiagnosisReadOnlyViewModel extends BaseObservable {
    public Description labelSymptom;
    public Symptom perception;
    public Symptom thinking;
    public Symptom pipedream;
    public Symptom emotion;
    public Symptom memory;
    public Symptom insight;

    public Description labelConsultation;
    public Symptom currentStatus;
    public Symptom recovered;
    public Symptom treatment;
    public Symptom sideEffect;

    public Description labelAdvice;
    public ItemTextInput adviceContent;
    public ItemTextInput description;
    public Description labelAssess;
    public ItemTextInput diagnosis;

    public List<Reminder> reminderList = new ArrayList<>();
    public List<Prescription> prescriptions = new ArrayList<>();

    public Doctor doctor;
    public Reminder furtherConsultation;

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

            labelSymptom = new Description(R.layout.item_description, "症状");
            labelConsultation = new Description(R.layout.item_description, "诊断");
            labelAssess = new Description(R.layout.item_description, "评估");
        }
//        else {
//            只有病人端显示
//            labelPay = new Description(R.layout.item_time_category, "支付方式");
//        }
        description = new ItemTextInput(R.layout.item_text_option_display, "");
        adviceContent = new ItemTextInput(R.layout.item_text_option_display, "");
        diagnosis = new ItemTextInput(R.layout.item_text_option_display, "");

        //两个端都有
        labelAdvice = new Description(R.layout.item_description, "嘱咐");
        furtherConsultation = new Reminder();
    }


    public void cloneFromDiagnosisInfo(DiagnosisInfo response) {
        if (response == null) {
            return;
        }
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
            for (Prescription data : prescription) {
                prescriptions.add(data);
            }
        }
        doctor = response.getDoctorInfo();
        furtherConsultation.content = getReturnTypeAndInterval(response);
        if (response.getReturnX() == 1) {
            furtherConsultation.time = response.getDate();
        }
        if (response.getReturnType() == 3) {
            furtherConsultation.time = "";
        }
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

            /**
             * 诊断
             */
            result.add(description);
            result.add(labelConsultation);
            result.add(diagnosis);

            result.add(labelAssess);
            result.add(currentStatus);
            result.add(recovered);
            result.add(treatment);
            result.add(sideEffect);
        }

        /**
         * 嘱咐
         */
        result.add(labelAdvice);
        result.add(adviceContent);
        if (!prescriptions.isEmpty()) {
            result.add(new Description(R.layout.item_description, "建议处方"));
            result.addAll(prescriptions);
        }
        if (furtherConsultation != null && !"".equals(furtherConsultation.content)) {
            result.add(new Description(R.layout.item_description, "专属咨询/闲时咨询/转诊"));
            result.add(furtherConsultation);
            if (doctor != null) {
                result.add(doctor);
            }
        }


        if (!reminderList.isEmpty()) {
            result.add(new Description(R.layout.item_description, "其它事项"));
            result.addAll(reminderList);
        }
        return result;
    }

    private String getReturnTypeAndInterval(DiagnosisInfo response) {
        if (response.getReturnX() == 1) {
            return getDiagnosisType(response.getReturnType());
        }
//        return "无需复诊";
        return "";
    }

    private String getDiagnosisType(int returnType) {
        String type = "";
        switch (returnType) {
            case 1:
                //专属咨询
                type = "专属咨询";
                break;
            case 2:
                //闲时咨询
                type = "闲时咨询";
                break;
            case 3:
                //转诊
                type = "转诊";
                break;
        }
        return type;
    }

    private boolean isPatient() {
        //true - 病人端 / false - 医生端
        return Config.getInt(Constants.USER_TYPE, -1) == AuthModule.PATIENT_TYPE;
    }
}
