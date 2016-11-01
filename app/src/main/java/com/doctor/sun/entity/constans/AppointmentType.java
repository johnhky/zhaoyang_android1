package com.doctor.sun.entity.constans;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rick on 20/4/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({AppointmentType.PREMIUM, AppointmentType.STANDARD, AppointmentType.FollowUp})
public @interface AppointmentType {
    int PREMIUM = 1;
    int STANDARD = 2;
    int FollowUp = 3;
}
