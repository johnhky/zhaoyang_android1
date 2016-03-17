package com.doctor.sun.entity;

import com.doctor.sun.R;

import java.util.ArrayList;

/**
 * Created by rick on 12/21/15.
 */
public class SymptomFactory {
    public static Symptom createSymptom() {
        Symptom symptom = new Symptom();
        symptom.setItemLayoutId(R.layout.item_symptom);
        return symptom;
    }

    public static Symptom createDiagnosis() {
        Symptom symptom = new Symptom();
        symptom.setItemLayoutId(R.layout.item_diagnosis);
        return symptom;
    }

    public static Symptom perceptionSymptom() {
        Symptom symptom = createSymptom();
        symptom.setTitle("知觉");

        ArrayList<String> values = new ArrayList<>();
        values.add("无");
        values.add("幻听");
        values.add("幻视");
        values.add("幻嗅");
        values.add("幻触");
        values.add("感知综合障碍");

        symptom.setValues(values);
        return symptom;
    }

    public static Symptom thinkingSymptom() {
        Symptom symptom = createSymptom();
        symptom.setTitle("思维");

        ArrayList<String> values = new ArrayList<>();
        values.add("无");
        values.add("奔逸");
        values.add("迟缓");
        values.add("散漫");
        values.add("破裂");
        values.add("逻辑障碍");

        symptom.setValues(values);
        return symptom;
    }

    public static Symptom pipedreamSymptom() {
        Symptom symptom = createSymptom();
        symptom.setTitle("妄想");

        ArrayList<String> values = new ArrayList<>();
        values.add("无");
        values.add("被害");
        values.add("夸大");
        values.add("关系");
        values.add("嫉妒");
        values.add("思维被洞悉");

        symptom.setValues(values);
        return symptom;
    }

    public static Symptom emotionSymptom() {
        Symptom symptom = createSymptom();
        symptom.setTitle("情感");

        ArrayList<String> values = new ArrayList<>();
        values.add("正常");
        values.add("高涨");
        values.add("低落");
        values.add("易激惹");
        values.add("焦虑");
        values.add("淡漠");

        symptom.setValues(values);
        return symptom;
    }

    public static Symptom memorySymptom() {
        Symptom symptom = createSymptom();
        symptom.setTitle("智能记忆");

        ArrayList<String> values = new ArrayList<>();
        values.add("正常 ");
        values.add("增强");
        values.add("减退");


        symptom.setValues(values);
        return symptom;
    }

    public static Symptom insightSymptom() {
        Symptom symptom = createSymptom();
        symptom.setTitle("自知力");

        ArrayList<String> values = new ArrayList<>();
        values.add("存在");
        values.add("部分");
        values.add("无");


        symptom.setValues(values);
        return symptom;
    }


    public static Symptom currentStatus() {
        Symptom symptom = createDiagnosis();
        symptom.setTitle("目前病情的严重程度");

        ArrayList<String> values = new ArrayList<>();
        values.add("正常，完全没症状");
        values.add("正常和异常间，边缘");
        values.add("轻微");
        values.add("中度");
        values.add("较严重");
        values.add("严重");
        values.add("极严重");

        symptom.setValues(values);
        return symptom;
    }

    public static Symptom recovered() {
        Symptom symptom = createDiagnosis();
        symptom.setTitle("对比本次起病初，目前的康复状况");

        ArrayList<String> values = new ArrayList<>();
        values.add("未评");
        values.add("明显好转");
        values.add("好转");
        values.add("稍好转");
        values.add("无变化");
        values.add("稍恶化");
        values.add("恶化");
        values.add("严重恶化");
        symptom.setValues(values);
        return symptom;
    }

    public static Symptom treatment() {
        Symptom symptom = createDiagnosis();
        symptom.setTitle("对比本次起病初，目前的治疗效果");

        ArrayList<String> values = new ArrayList<>();
        values.add("显著有效");
        values.add("有效");
        values.add("稍有效");
        values.add("无效或恶化");

        symptom.setValues(values);
        return symptom;
    }

    public static Symptom sideEffect() {
        Symptom symptom = createDiagnosis();
        symptom.setTitle("患者目前服药的副作用");

        ArrayList<String> values = new ArrayList<>();
        values.add("没有副反应");
        values.add("有点副反应但不影响生活");
        values.add("有副反应并影响生活");
        values.add("有严重的副反应");

        symptom.setValues(values);
        return symptom;
    }
}
