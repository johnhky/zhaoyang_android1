package com.doctor.sun.ui.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.sun.BuildConfig;
import com.doctor.sun.R;
import com.doctor.sun.bean.MobEventId;
import com.doctor.sun.entity.Token;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.DoNothingCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.activity.WebBrowserActivity;
import com.doctor.sun.ui.activity.doctor.EditDoctorInfoActivity;
import com.doctor.sun.ui.activity.patient.PMainActivity;
import com.doctor.sun.util.MD5;
import com.umeng.analytics.MobclickAgent;

import io.ganguo.library.util.Strings;

/**
 * Created by rick on 11/17/15.
 */
public class RegisterHandler extends BaseHandler {
    private static final int ONE_SECOND = 1000;
    private AuthModule api = Api.of(AuthModule.class);
    private RegisterInput mInput;
    private Handler handler;
    private int remainTime;

    private int registerType;

    private static final long DOUBLE_PRESS_INTERVAL = 600;
    private long lastPressTime = 0;

    public RegisterHandler(Activity context, int registerType) {
        super(context);
        this.registerType = registerType;
        handler = new Handler(Looper.myLooper());

        try {
            mInput = (RegisterInput) context;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("The host Activity must implement RegisterInput");
        }
    }

    public void sendCaptcha(View view) {
        String phone = mInput.getPhone();
        if (!Strings.isMobile(phone)) {
            Toast.makeText(getContext(), "手机号码格式错误", Toast.LENGTH_SHORT).show();
            return;
        }


        long pressTime = System.currentTimeMillis();
        if (pressTime - lastPressTime >= DOUBLE_PRESS_INTERVAL) {
            countDown(view);
            api.sendCaptcha(phone).enqueue(new DoNothingCallback());
        }
        lastPressTime = pressTime;
    }

    public void register() {
        if (TokenCallback.isLogin()) {
            Intent i = EditDoctorInfoActivity.makeIntent(getContext(), null);
            getContext().startActivity(i);
        }
        String phone = mInput.getPhone();
        if (notValid()) return;

        api.register(
                registerType, phone,
                mInput.getCaptcha(), getPasswordMd5()).enqueue(new ApiCallback<Token>() {
            @Override
            protected void handleResponse(Token response) {
                if (registerType == AuthModule.DOCTOR_TYPE) {
                    registerDoctorSuccess(response);
                    MobclickAgent.onEvent(getContext(), MobEventId.DOCTOR_REGISTRATION);
                } else if (registerType == AuthModule.PATIENT_TYPE) {
                    registerPatientSuccess(response);
                    MobclickAgent.onEvent(getContext(), MobEventId.PATIENT_REGISTRATION);
                }
            }
        });
    }

    private void registerPatientSuccess(Token response) {
        if (response != null) {
            TokenCallback.handleToken(response);
            Intent i = PMainActivity.makeIntent(getContext());
            getContext().startActivity(i);
        }
    }

    private void registerDoctorSuccess(Token response) {
        if (response == null) {
            //TODO
            Intent i = EditDoctorInfoActivity.makeIntent(getContext(), null);
            getContext().startActivity(i);
        } else {
            TokenCallback.handleToken(response);
            Intent i = EditDoctorInfoActivity.makeIntent(getContext(), null);
            getContext().startActivity(i);
        }
    }


    private void countDown(View view) {
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
                    textView.setText(getContext().getResources().getString(R.string.get_captcha, remainTime));
                    textView.setEnabled(false);
                    parent.setEnabled(false);
                    handler.postDelayed(this, ONE_SECOND);
                }
            }
        };
        handler.postDelayed(runnable, ONE_SECOND);
    }

    public void resetPassword(View view) {
        if (notValid()) return;
        api.reset(mInput.getPhone(), getPasswordMd5(), mInput.getCaptcha()).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                Toast.makeText(getContext(), "重置密码成功", Toast.LENGTH_SHORT).show();
                getContext().finish();
            }
        });
    }

    private String getPasswordMd5() {
        return MD5.getMessageDigest(mInput.getPassword().getBytes());
    }

    private boolean notValid() {
        if (!Strings.isMobile(mInput.getPhone())) {
            Toast.makeText(getContext(), "手机号码格式错误", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (mInput.getPassword() == null || mInput.getPassword().equals("")) {
            Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (!Strings.isEquals(mInput.getPassword(), mInput.getPassword2())) {
            Toast.makeText(getContext(), "两次输入的密码不相同", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (mInput.getPassword().length() < 6) {
            Toast.makeText(getContext(), "密码不能少于6个字", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    public void viewRegistrationPolicy() {
        String url = BuildConfig.BASE_URL + "readme/registration-policy";
        Intent i = WebBrowserActivity.intentFor(getContext(), url);
        getContext().startActivity(i);
    }

    public interface RegisterInput {
        String getEmail();

        String getPhone();

        String getCaptcha();

        String getPassword();

        String getPassword2();
    }

}
