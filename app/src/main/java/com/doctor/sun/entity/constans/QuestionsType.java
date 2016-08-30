package com.doctor.sun.entity.constans;

import android.support.annotation.StringDef;

import static com.doctor.sun.entity.constans.QuestionsType.DOCTOR_R_PATIENT_QUESTIONS;
import static com.doctor.sun.entity.constans.QuestionsType.DOCTOR_W_DOCTOR_QUESTIONS;
import static com.doctor.sun.entity.constans.QuestionsType.PATIENT_R_DOCTOR_QUESTIONS;
import static com.doctor.sun.entity.constans.QuestionsType.PATIENT_W_PATIENT_QUESTIONS;

/**
 * Created by rick on 30/8/2016.
 * <p>
 * <p>
 * String PATIENT_W_PATIENT_QUESTIONS = "1";
 */
@StringDef({PATIENT_W_PATIENT_QUESTIONS, PATIENT_R_DOCTOR_QUESTIONS, DOCTOR_W_DOCTOR_QUESTIONS, DOCTOR_R_PATIENT_QUESTIONS})
public @interface QuestionsType {
    //患者读医生建议,已完成的时候, todo: 改个好名字
    String PATIENT = "4";
    @Deprecated
    String DOCTOR = "3";


    //患者填写患者问卷
    String PATIENT_W_PATIENT_QUESTIONS = "1";
    //患者读医生问卷
    String PATIENT_R_DOCTOR_QUESTIONS = "2";

    //医生读患者问卷
    String DOCTOR_R_PATIENT_QUESTIONS = "1";
    //医生写医生建议
    String DOCTOR_W_DOCTOR_QUESTIONS = "2";
}
