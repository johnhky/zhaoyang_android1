package com.doctor.sun;

import android.databinding.Observable;
import android.widget.Toast;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.entity.RecentAppointment;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.entity.constans.ReviewStatus;
import com.doctor.sun.event.DoctorProfileChangedEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.vm.ItemSwitch;
import com.google.common.base.Strings;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MsgService;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;
import retrofit2.Call;

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

    public static boolean isEnablePush() {
        return getDoctorProfile().push_enable == IntBoolean.TRUE;
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

    public static void loadPatientProfile(final Runnable callback) {
        ProfileModule api = Api.of(ProfileModule.class);
        api.patientProfile().enqueue(new SimpleCallback<PatientDTO>() {
            @Override
            protected void handleResponse(PatientDTO response) {
                Settings.setPatientProfile(response);
                callback.run();
            }
        });
    }

    public static ItemSwitch pushSwitch() {
        final ItemSwitch itemSwitch = new ItemSwitch(R.layout.item_switch);
        boolean enablePush = isEnablePush();
        itemSwitch.setChecked(enablePush);
        if (enablePush) {
            itemSwitch.setContent("已开启后台推送");
        } else {
            itemSwitch.setContent("已关闭后台推送");
        }
        itemSwitch.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {

            private Call<ApiDTO<HashMap<String, String>>> apiDTOCall;

            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (i == BR.isChecked) {
                    if (apiDTOCall != null) {
                        if (!apiDTOCall.isExecuted() && !apiDTOCall.isCanceled()) {
                            apiDTOCall.cancel();
                        }
                    }
                    if (itemSwitch.isChecked()) {
                        apiDTOCall = toggleEnablePush(IntBoolean.TRUE, itemSwitch);
                    } else {
                        apiDTOCall = toggleEnablePush(IntBoolean.FALSE, itemSwitch);
                    }
                }
            }
        });
        return itemSwitch;
    }

    public static Call<ApiDTO<HashMap<String, String>>> toggleEnablePush(final int enabled, final ItemSwitch itemSwitch) {
        ProfileModule api = Api.of(ProfileModule.class);
        HashMap<String, String> fields = new HashMap<>();
        fields.put("push_enable", "" + enabled);
        Call<ApiDTO<HashMap<String, String>>> config = api.config(fields);
        config.enqueue(new SimpleCallback<HashMap<String, String>>() {
            @Override
            protected void handleResponse(HashMap<String, String> response) {
                //TODO response to the user
                Doctor doctorProfile = getDoctorProfile();
                doctorProfile.push_enable = enabled;
                setDoctorProfile(doctorProfile);
                if (enabled == IntBoolean.TRUE) {
                    Toast.makeText(AppContext.me(), "成功开启后台推送", Toast.LENGTH_SHORT).show();
                    itemSwitch.setContent("已开启后台推送");
                } else {
                    Toast.makeText(AppContext.me(), "成功关闭后台推送", Toast.LENGTH_SHORT).show();
                    itemSwitch.setContent("已关闭后台推送");
               /*     NIMClient.getService(MixPushService.class).enable(false).setCallback(new RequestCallback<Void>(){
                        @Override
                        public void onSuccess(Void aVoid) {

                        }

                        @Override
                        public void onException(Throwable throwable) {

                        }

                        @Override
                        public void onFailed(int i) {

                        }
                    }); */               }
            }
        });
        return config;
    }

    public static void loadDoctorProfile() {
        ProfileModule api = Api.of(ProfileModule.class);
        api.doctorProfile().enqueue(new DoctorApiCallback());
    }

    private static class DoctorApiCallback extends ApiCallback<Doctor> {
        @Override
        protected void handleResponse(Doctor response) {
            setDoctorProfile(response);
            EventHub.post(new DoctorProfileChangedEvent(response));
        }
    }

    private static void setDoctorProfile(Doctor response) {
        Config.putString(Constants.DOCTOR_PROFILE, JacksonUtils.toJson(response));
    }

    public static boolean is(String key) {
        return Config.getBoolean(key, true);
    }

    public static void set(String key, boolean value) {
        Config.putBoolean(key, value);
    }
}
