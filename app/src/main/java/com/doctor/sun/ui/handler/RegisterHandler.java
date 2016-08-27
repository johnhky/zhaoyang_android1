package com.doctor.sun.ui.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.sun.BR;
import com.doctor.sun.BuildConfig;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.MobEventId;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Token;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.activity.WebBrowserActivity;
import com.doctor.sun.ui.activity.patient.PMainActivity2;
import com.doctor.sun.ui.fragment.EditDoctorInfoFragment;
import com.doctor.sun.util.MD5;
import com.umeng.analytics.MobclickAgent;

import io.ganguo.library.util.StringsUtils;

/**
 * Created by rick on 11/17/15.
 */
public class RegisterHandler extends BaseObservable {
    public static final String TAG = RegisterHandler.class.getSimpleName();
    private static final int ONE_SECOND = 1000;
    private AuthModule api = Api.of(AuthModule.class);
    private Handler handler;
    private int remainTime;

    private int registerType = AuthModule.DOCTOR_TYPE;
    private boolean enable;

    public String phone = "";
    public String captcha = "";
    public String password = "";
    public String password2 = "";
    public boolean isDoctor = false;


    private static final long DOUBLE_PRESS_INTERVAL = 600;
    private long lastPressTime = 0;

    public RegisterHandler(int registerType) {
        this.registerType = registerType;
        handler = new Handler(Looper.myLooper());
        isDoctor = registerType == AuthModule.DOCTOR_TYPE;
    }

    public void sendCaptcha(final Context context, final ViewGroup viewGroup) {
        if (!StringsUtils.isMobile(phone)) {
            Toast.makeText(context, "手机号码格式错误", Toast.LENGTH_SHORT).show();
            return;
        }


        long pressTime = System.currentTimeMillis();
        if (pressTime - lastPressTime >= DOUBLE_PRESS_INTERVAL) {
            api.sendCaptcha(phone).enqueue(new SimpleCallback<String>() {
                @Override
                protected void handleResponse(String response) {
                    countDown(context, viewGroup);
                }
            });
        }
        lastPressTime = pressTime;
    }

    public void register(final Context context) {
        if (Settings.isLogin()) {
            editDoctorInfo(context);
        }
        if (notValid(context)) return;

        api.register(
                registerType, phone,
                captcha, getPasswordMd5()).enqueue(new ApiCallback<Token>() {
            @Override
            protected void handleResponse(Token response) {
                if (registerType == AuthModule.DOCTOR_TYPE) {
                    registerDoctorSuccess(context, response);
                    MobclickAgent.onEvent(context, MobEventId.DOCTOR_REGISTRATION);
                } else if (registerType == AuthModule.PATIENT_TYPE) {
                    registerPatientSuccess(context, response);
                    MobclickAgent.onEvent(context, MobEventId.PATIENT_REGISTRATION);
                }
            }
        });
    }



    private void registerPatientSuccess(Context context, Token response) {
        if (response != null) {
            TokenCallback.handleToken(response);
            Intent i = PMainActivity2.intentFor(context);
            context.startActivity(i);
        }
    }

    private void registerDoctorSuccess(Context context, Token response) {
        if (response == null) {
            //TODO
            editDoctorInfo(context);
        } else {
            TokenCallback.handleToken(response);
            editDoctorInfo(context);
        }
    }
    public void editDoctorInfo(Context context) {
        Doctor data = new Doctor();
        data.setPhone(phone);
        Intent intent = EditDoctorInfoFragment.intentFor(context, data);
        context.startActivity(intent);
    }

    private void countDown(Context context, final ViewGroup view) {
        remainTime = 60;
        final ViewGroup parent = (ViewGroup) view;
        final TextView textView = (TextView) parent.getChildAt(0);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                remainTime -= 1;
                if (remainTime < 0) {
                    textView.setEnabled(true);
                    parent.setEnabled(true);
                    textView.setText("重新获取");
                    handler.removeCallbacks(this);
                } else {
                    textView.setText(view.getResources().getString(R.string.get_captcha, remainTime));
                    textView.setEnabled(false);
                    parent.setEnabled(false);
                    handler.postDelayed(this, ONE_SECOND);
                }
            }
        };
        handler.postDelayed(runnable, ONE_SECOND);
    }

    public void resetPassword(final Context context) {
        if (notValid(context)) return;
        api.reset(phone, getPasswordMd5(), captcha).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                Toast.makeText(context, "重置密码成功", Toast.LENGTH_SHORT).show();
                Activity activity = (Activity) context;
                activity.finish();
            }
        });
    }

    private String getPasswordMd5() {
        return MD5.getMessageDigest(password.getBytes());
    }

    private boolean notValid(Context context) {
        if (!StringsUtils.isMobile(phone)) {
            Toast.makeText(context, "手机号码格式错误", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (password == null || password.equals("")) {
            Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (!StringsUtils.isEquals(password, password2)) {
            Toast.makeText(context, "两次输入的密码不相同", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (password.length() < 6) {
            Toast.makeText(context, "密码不能少于6个字", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    public void viewRegistrationPolicy(Context context) {
        String url = BuildConfig.BASE_URL + "readme/registration-policy";
        if (isDoctor) {
            url += "?client=doctor";
        }
        Log.e(TAG, "viewRegistrationPolicy: " + url);
        Intent i = WebBrowserActivity.intentFor(context, url, "注册须知");
        context.startActivity(i);
    }

    @Bindable
    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        notifyPropertyChanged(BR.enable);
    }

    @Bindable
    public int getRegisterType() {
        return registerType;
    }

    public void setRegisterType(int registerType) {
        this.registerType = registerType;
        notifyPropertyChanged(BR.registerType);
    }

}
