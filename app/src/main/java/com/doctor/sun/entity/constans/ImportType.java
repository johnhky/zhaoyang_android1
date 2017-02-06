package com.doctor.sun.entity.constans;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rick on 20/4/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({ImportType.ALL, ImportType.DIAGNOSIS, ImportType.ADVICE_AND_PRESCRIPTION})
public @interface ImportType {
    int ALL = 1;
    int DIAGNOSIS = 2;
    int ADVICE_AND_PRESCRIPTION = 3;
}
