package com.doctor.sun.entity.constans;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.doctor.sun.entity.constans.QuestionsPath.FOLLOW_UP;
import static com.doctor.sun.entity.constans.QuestionsPath.NORMAL;
import static com.doctor.sun.entity.constans.QuestionsPath.SCALES;
import static com.doctor.sun.entity.constans.QuestionsPath.TEMPLATE;

/**
 * Created by rick on 17/8/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({NORMAL, SCALES, FOLLOW_UP, TEMPLATE})
public @interface QuestionsPath {
    //普通问卷
    String NORMAL = "smartQuestionnaires";
    //量表
    String SCALES = "smartScales";
    //随访问卷
    String FOLLOW_UP = "smartFollowUpQuestionnaires";
    //随访问卷
    String TEMPLATE = "smartTemplate";
}
