package com.doctor.sun.entity.constans;

import android.support.annotation.StringDef;

import static com.doctor.sun.entity.constans.RelationshipStatus.APPLIED;
import static com.doctor.sun.entity.constans.RelationshipStatus.APPLYING;
import static com.doctor.sun.entity.constans.RelationshipStatus.UNAPPLY;

/**
 * Created by rick on 29/8/2016.
 */

@StringDef({APPLYING, APPLIED, UNAPPLY})
public @interface RelationshipStatus {
    String APPLYING = "applying";
    String UNAPPLY = "unapply";
    String APPLIED = "apply";
}
