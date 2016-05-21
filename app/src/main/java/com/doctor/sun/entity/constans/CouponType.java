package com.doctor.sun.entity.constans;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rick on 21/5/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({CouponType.ALL, CouponType.VALID, CouponType.INVALID, CouponType.CAN_USE_NOW})
public @interface CouponType {
    String ALL = "all";
    String VALID = "valid";
    String INVALID = "invalid";
    String CAN_USE_NOW = "can_pay";
}
