package com.doctor.sun.entity.constans;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rick on 17/8/2016.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface QuestionsPath {
    //普通问卷
    String NORMAL = "smartQuestionnaires";
    //量表
    String SCALES = "smartScales";
    //随访问卷
    String FOLLOW_UP = "smartFollowUpQuestionnaires";
}
