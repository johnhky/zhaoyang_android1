package com.doctor.sun;

import io.ganguo.library.Config;

/**
 * Created by rick on 22/9/2016.
 */

public class TestConfig {
    public static final String PHONE_NUM_TO_CHANGE_PSW = "13922311301";
    public static final String CHANGED_PSW = "a123456";

    public static final String PSW = "a1234567";
    public static final String PATIENT_PHONE_NUM = "13922228114";
    public static final String DOCTOR_PHONE_NUM = "13922221313";
    public static final String CAPTCHA = "123456";

    public static String getPatientPhoneNum() {
        return Config.getString("PATIENT_PHONE_NUM", PATIENT_PHONE_NUM);
    }
}
