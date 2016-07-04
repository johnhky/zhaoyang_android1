package com.doctor.sun.bean;

import java.util.ArrayList;

/**
 * 常量
 * <p>
 * Created by Tony on 11/10/15.
 */
public class Constants {
    public static final String TOKEN = "TOKEN";

    public static final String DATA = "DATA";
    public static final String EMAIL = "EMAIL";
    public static final String POSITION = "POSITION";
    public static final String EDITSTATUE = "EDITSTATUE";
    public static final String LAYOUT_ID = "LAYOUT_ID";
    public static final String QUESTION_ID = "QUESTION_ID";
    public static final String TEMPLATE_NAME = "TEMPLATE_NAME";
    public static final String URI = "URI";
    public static final String MONEY = "MONEY";
    public static final String EXTRA_FIELD = "EXTRA_FIELD";
    public static final String NUMBER = "NUMBER";

    public static final String PAY_STATUS = "PAY_STATUS";
    public static final String USER = "USER";
    public static final String USER_TYPE = "USER_TYPE";
    public static final String PASSFIRSTTIME = "PASSFIRSTTIME";
    public static final String DOCTOR_PROFILE = "DOCTOR_PROFILE";
    public static final String PATIENT_PROFILE = "PATIENT_PROFILE";
    public static final String VOIP_ACCOUNT = "VOIP_ACCOUNT";

    public static final int DOCTOR_REQUEST_CODE = 11;
    public static final int PRESCRITION_REQUEST_CODE = 12;
    public static final int NICKNAME_REQUEST_CODE = 13;
    public static final int UPLOAD_REQUEST_CODE = 14;
    public static final int PATIENT_PRESCRITION_REQUEST_CODE = 15;

    public static final String PARAM_PATIENT_ID = "patient_id";
    public static final String PARAM_RECORD_ID = "record_id";
    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final String PARAM_DOCTOR_ID = "doctor_id";
    public static final String PARAM_NICKNAME = "nickname";
    public static final String PARAM_APPOINTMENT = "appointment";

    public static final String PARAM_PATIENT = "patient_data";

    public static final String DATE = "DATE";
    public static final String CONSULT_TYPE = "CONSULT_TYPE";

    public static final String DOCTOR = "DOCTOR";
    public static final String BOOKTIME = "BOOKTIME";
    public static final String TYPE = "TYPE";
    public static final String RECORDID = "RECORDID";

    public static final String PASSTIME = "PASSTIME";
    public static final String PAY_METHOD = "PAY_METHOD";

    public static final boolean STATUS_QUESTION_LIST = false;
    public static final boolean STATUS_ANSWER_DETAIL = true;
    public static final boolean STATUS_ANSWER_MODIFY = true;
    public static final String HANDLER = "HANDLER";
    public static final String FINISH_MESSAGE = "温馨提醒：您的预约就诊时间已到，如有疑问，请再次预约！";
    public static final String START_MESSAGE = "就诊开始";
    public static final String START_MSG2 = "随访开始";
    public static final String FINISH_MSG2 = "随访结束";

    public static final ArrayList<String> refreshMsg = new ArrayList<String>();
    public static final String IS_SHOWCASE = "IS_SHOWCASE";

    static {
        refreshMsg.add(FINISH_MESSAGE);
        refreshMsg.add(START_MESSAGE);
        refreshMsg.add(FINISH_MSG2);
        refreshMsg.add(START_MSG2);
    }

    public static final String IS_DONE = "IS_DONE";
}
