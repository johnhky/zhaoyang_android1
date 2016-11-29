package com.doctor.sun.entity.constans;

import android.support.annotation.StringDef;

/**
 * Created by kb on 16-11-29.
 */

@StringDef(value = {"uncertain", "certain"})
public @interface DrugStatus {
    String UNCERTAIN = "uncertain";
    String CERTAIN = "certain";
}
