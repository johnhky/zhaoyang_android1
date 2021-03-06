package com.doctor.sun.entity.constans;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rick on 12/7/2016.
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({CommunicationType.PHONE_CALL, CommunicationType.VIDEO_CALL})
public @interface CommunicationType {
    int PHONE_CALL = 1;
    int VIDEO_CALL = 2;
}
