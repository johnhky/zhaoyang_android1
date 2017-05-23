package com.doctor.sun.entity.constans;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rick on 24/11/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TimeType.TYPE_UNDEFINE, TimeType.TYPE_DETAIL, TimeType.TYPE_QUICK, TimeType.TYPE_BREAK})
public @interface TimeType {
    int TYPE_UNDEFINE = 0;
    int TYPE_DETAIL = 1;
    int TYPE_QUICK = 2;
    int TYPE_BREAK = 3;
    int TYPY_FACE = 4;
}
