package com.doctor.sun.http.callback;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.entity.RecentAppointment;
import com.doctor.sun.entity.Token;
import com.doctor.sun.http.Api;
import com.doctor.sun.im.Messenger;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.doctor.EditDoctorInfoActivity;
import com.doctor.sun.ui.activity.doctor.MainActivity;
import com.doctor.sun.ui.activity.doctor.RegisterActivity;
import com.doctor.sun.ui.activity.doctor.ReviewResultActivity;
import com.doctor.sun.util.JacksonUtils;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.ganguo.library.Config;
import io.ganguo.library.common.LoadingHelper;

/**
 * Created by rick on 11/18/15.
 */
public class TokenCallback {
    public static final String TAG = TokenCallback.class.getSimpleName();
    public static final int ISFIRSTTIME = 1;
    public static final int NOTFIRSTTIME = 2;

    public static void checkToken(final Activity context) {
        if (Config.getString(Constants.TOKEN) != null) {
            switch (Config.getInt(Constants.USER_TYPE, -1)) {
                case AuthModule.DOCTOR_TYPE: {

                    loadDoctorProfile(context);

                    break;
                }
                case AuthModule.DOCTOR_PASSED: {

                    Intent i = com.doctor.sun.ui.activity.doctor.MainActivity.makeIntent(context);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        context.finishAffinity();
                    } else {
                        context.finish();
                    }
                    context.startActivity(i);

                    break;
                }
                case AuthModule.PATIENT_TYPE: {

                    Patient patientProfile = getPatientProfile();
                    if (patientProfile == null) {
                        loadPatientProfile(context);
                    } else {
                        Intent i = com.doctor.sun.ui.activity.patient.MainActivity.makeIntent(context);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            context.finishAffinity();
                        } else {
                            context.finish();
                        }
                        context.startActivity(i);
                        context.finish();
                    }
                    break;
                }
                default: {

                    break;
                }

            }
        }
    }

    private static void loadDoctorProfile(final Activity context) {
        ProfileModule profileModule = Api.of(ProfileModule.class);
        LoadingHelper.showMaterLoading(context, "正在加载个人信息");
        profileModule.doctorProfile().enqueue(new ApiCallback<Doctor>() {
            @Override
            protected void handleResponse(Doctor response) {
                LoadingHelper.hideMaterLoading();
                Doctor data = response;
                Log.e(TAG, "handleResponse: " + data);
                Config.putString(Constants.DOCTOR_PROFILE, JacksonUtils.toJson(data));
                if (data == null) {
                    Intent i = RegisterActivity.makeIntent(context, AuthModule.DOCTOR_TYPE);
                    context.startActivity(i);
                    context.finish();
                } else switch (data.getStatus()) {
                    case Doctor.STATUS_PASS: {
//                        Log.e(TAG, "firstTime: " + Config.getInt(Constants.PASSFIRSTTIME, -1));
                        Config.putInt(Constants.USER_TYPE, AuthModule.DOCTOR_PASSED);
                        Intent i = MainActivity.makeIntent(context);
                        context.startActivity(i);
                        context.finish();
                        break;
                    }
                    case Doctor.STATUS_PENDING: {
                        Intent i = ReviewResultActivity.makeIntent(context, data);
                        Config.putInt(Constants.PASSFIRSTTIME, ISFIRSTTIME);
                        context.startActivity(i);
                        context.finish();
                        break;
                    }
                    case Doctor.STATUS_REJECT: {
                        Intent i = ReviewResultActivity.makeIntent(context, data);
                        Config.putInt(Constants.PASSFIRSTTIME, ISFIRSTTIME);
                        context.startActivity(i);
                        context.finish();
                        break;
                    }
                    default: {
                        Config.putInt(Constants.USER_TYPE, AuthModule.DOCTOR_PASSED);
                        Intent i = RegisterActivity.makeIntent(context, AuthModule.DOCTOR_TYPE);
                        context.startActivity(i);
                        context.finish();
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                LoadingHelper.hideMaterLoading();
                Toast.makeText(context, "医生未填写个人资料", Toast.LENGTH_SHORT).show();
                Intent intent = EditDoctorInfoActivity.makeIntent(context, null);
                context.startActivity(intent);
            }
        });
    }

    public static void loadPatientProfile(final Activity context) {
        ProfileModule profileModule = Api.of(ProfileModule.class);
        LoadingHelper.showMaterLoading(context, "正在加载个人信息");
        profileModule.patientProfile().enqueue(new ApiCallback<PatientDTO>() {
            @Override
            protected void handleResponse(PatientDTO response) {
                LoadingHelper.hideMaterLoading();
                Log.e(TAG, "handleResponse: " + response.toString());
                String value = null;
                try {
                    value = JacksonUtils.toJson(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Config.putString(Constants.PATIENT_PROFILE, value);
                if (response == null) {
                    Intent i = RegisterActivity.makeIntent(context, AuthModule.PATIENT_TYPE);
                    context.startActivity(i);
                    context.finish();
                } else {
                    Intent i = com.doctor.sun.ui.activity.patient.MainActivity.makeIntent(context);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        context.finishAffinity();
                    } else {
                        context.finish();
                    }
                    context.startActivity(i);
                    context.finish();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                t.printStackTrace();
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    public static void handleToken(Token token) {
        Config.putString(Constants.TOKEN, token.getToken());
        Config.putInt(Constants.USER_TYPE, token.getType());
        Config.putString(Constants.VOIP_ACCOUNT, JacksonUtils.toJson(token.getAccount()));
        JPushInterface.setAlias(AppContext.me(), String.valueOf(token.getAccount().getUserId()), new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
            }
        });
        Messenger.getInstance().login(token.getAccount());
    }

    public static String getToken() {
        return Config.getString(Constants.TOKEN);
    }

    public static Patient getPatientProfile() {
        String json = Config.getString(Constants.PATIENT_PROFILE);
        if (json == null) {
            return null;
        }
        PatientDTO patient = JacksonUtils.fromJson(json, PatientDTO.class);
        return patient != null ? patient.getInfo() : null;
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
        Doctor doctor = JacksonUtils.fromJson(json, Doctor.class);
        return doctor;
    }

    public static String getPhone() {
        if (AppContext.isDoctor()) {
            return getDoctorProfile().getPhone();
        } else {

            Patient patientProfile = getPatientProfile();
            if (patientProfile == null) {
                return "";
            }
            return patientProfile.getPhone();
        }
    }

}
