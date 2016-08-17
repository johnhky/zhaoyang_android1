package com.doctor.sun.entity.constans;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rick on 17/8/2016.
 */

@Retention(RetentionPolicy.SOURCE)
public @interface QTemplateType {
    String NORMAL = "smartQuestionnaires";
    String SCALES = "smartScales";
}
