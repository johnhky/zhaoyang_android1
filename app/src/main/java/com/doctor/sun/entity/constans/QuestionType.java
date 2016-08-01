package com.doctor.sun.entity.constans;

import android.support.annotation.StringDef;

import static com.doctor.sun.entity.constans.QuestionType.asel;
import static com.doctor.sun.entity.constans.QuestionType.checkbox;
import static com.doctor.sun.entity.constans.QuestionType.drug;
import static com.doctor.sun.entity.constans.QuestionType.fill;
import static com.doctor.sun.entity.constans.QuestionType.keepon;
import static com.doctor.sun.entity.constans.QuestionType.radio;
import static com.doctor.sun.entity.constans.QuestionType.rectangle;
import static com.doctor.sun.entity.constans.QuestionType.reminder;
import static com.doctor.sun.entity.constans.QuestionType.sDate;
import static com.doctor.sun.entity.constans.QuestionType.sTime;
import static com.doctor.sun.entity.constans.QuestionType.sel;
import static com.doctor.sun.entity.constans.QuestionType.upImg;

/**
 * Created by rick on 30/7/2016.
 */

@StringDef(value = {radio, checkbox, fill, upImg, sDate, sTime, sel, asel, keepon, drug, rectangle, reminder})
public @interface QuestionType {
    String radio = "radio";
    String checkbox = "checkbox";
    String fill = "fill";
    String upImg = "upImg";
    String sDate = "sdate";
    String sTime = "stime";
    String sel = "sel";
    String asel = "asel";
    String keepon = "keepon";
    String drug = "drug";
    String rectangle = "rectangle";
    String reminder = "alarmclock";

}
