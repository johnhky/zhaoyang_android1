package com.doctor.sun;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.entity.RecentAppointment;
import com.doctor.sun.entity.constans.ReviewStatus;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.util.JacksonUtils;
import com.google.common.base.Strings;

import io.ganguo.library.Config;

/**
 * 设置选项
 * <p/>
 * Created by Tony on 10/24/15.
 */
public class Settings {

    private static final String SETTING_FIRST_OPEN = "setting_first_open";
    private static final String SETTING_LAST_VERSION = "setting_last_version";

    /**
     * 是否首次打开应用
     *
     * @return
     */
    public static boolean isFirstOpen() {
        return Config.getBoolean(SETTING_FIRST_OPEN, true);
    }

    /**
     * 设置是否首次打开
     *
     * @param isFirstOpen
     */
    public static void setFirstOpen(boolean isFirstOpen) {
        Config.putBoolean(SETTING_FIRST_OPEN, isFirstOpen);
    }

    /**
     * 获取上一次版本号
     *
     * @return
     */
    public static int getLastVersion() {
        return Config.getInt(SETTING_LAST_VERSION, BuildConfig.VERSION_CODE);
    }

    /**
     * 设置上一次版本号
     */
    public static void setLastVersion(int version) {
        Config.putInt(SETTING_LAST_VERSION, version);
    }

    public static boolean isDoctor() {
        int userType = Config.getInt(Constants.USER_TYPE, -1);
        return userType != AuthModule.PATIENT_TYPE;
    }

    public static String getToken() {
        return Config.getString(Constants.TOKEN);
    }

    public static PatientDTO getPatientDTO() {
        String json = Config.getString(Constants.PATIENT_PROFILE);
        if (json == null || json.equals("")) {
            return null;
        }
        PatientDTO patient = JacksonUtils.fromJson(json, PatientDTO.class);
        return patient;
    }

    public static Patient getPatientProfile() {
        String json = Config.getString(Constants.PATIENT_PROFILE);
        if (json == null || json.equals("")) {
            return null;
        }
        PatientDTO patient = JacksonUtils.fromJson(json, PatientDTO.class);
        return patient != null ? patient.getInfo() : null;
    }

    public static boolean haveSelfName() {
        Patient patientProfile = Settings.getPatientProfile();
        if (patientProfile == null) {
            return false;
        }
        return !Strings.isNullOrEmpty(patientProfile.getName());
    }

    public static RecentAppointment getRecentAppointment() {
        String json = Config.getString(Constants.PATIENT_PROFILE);
        if (json == null) {
            return null;
        }
        PatientDTO patient = JacksonUtils.fromJson(json, PatientDTO.class);
        return patient != null ? patient.getRecent_appointment() : null;
    }

    public static Doctor getDoctorProfile() {
        String json = Config.getString(Constants.DOCTOR_PROFILE);
        if (json == null) {
            return new Doctor();
        }
        return JacksonUtils.fromJson(json, Doctor.class);
    }

    public static String getPhone() {
        if (isDoctor()) {
            return getDoctorProfile().getPhone();
        } else {

            Patient patientProfile = getPatientProfile();
            if (patientProfile == null) {
                return "";
            }
            return patientProfile.getPhone();
        }
    }

    public static boolean isLogin() {
        String token = getToken();
        return token != null && !token.equals("");
    }

    public static void setPatientProfile(PatientDTO response) {
        String value = null;
        try {
            value = JacksonUtils.toJson(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Config.putString(Constants.PATIENT_PROFILE, value);
    }


    /**
     * 医生信息审核状态弹窗 上一次显示弹窗时候的状态
     */
    public static String lastDoctorStatus() {
        return Config.getString(Constants.LAST_DOCTOR_STATUS, ReviewStatus.STATUS_PASS);
    }

    public static void setLastDoctorStatus(String status) {
        Config.putString(Constants.LAST_DOCTOR_STATUS, status);
    }

}
