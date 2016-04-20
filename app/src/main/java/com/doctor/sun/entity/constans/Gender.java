package com.doctor.sun.entity.constans;

import android.support.annotation.IntDef;

/**
 * Created by rick on 20/4/2016.
 */
@IntDef({Gender.MALE, Gender.FEMALE})
public @interface Gender {
    int MALE = 1;
    int FEMALE = 0;
}
