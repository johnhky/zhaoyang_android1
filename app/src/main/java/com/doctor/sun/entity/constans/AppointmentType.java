package com.doctor.sun.entity.constans;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rick on 20/4/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({AppointmentType.DETAIL, AppointmentType.QUICK})
public @interface AppointmentType {
    int DETAIL = 1;
    int QUICK = 2;
    int AFTER_SERVICE = 3;
}
