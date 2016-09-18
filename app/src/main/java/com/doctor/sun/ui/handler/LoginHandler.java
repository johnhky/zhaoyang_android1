package com.doctor.sun.ui.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.doctor.sun.entity.Token;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.activity.doctor.RegisterActivity;
import com.doctor.sun.ui.fragment.ResetPswFragment;
import com.doctor.sun.util.MD5;
import com.umeng.analytics.MobclickAgent;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.util.StringsUtils;
import retrofit2.Call;

/**
 * Created by rick on 11/17/15.
 */
public class LoginHandler {
    public static final String TAG = LoginHandler.class.getSimpleName();

    private AuthModule api = Api.of(AuthModule.class);

    public String phone = "";
    public String password = "";


    public LoginHandler() {
    }

    public boolean loginAction(Context context, String phone, String password) {
        login(context, phone, password);
        return false;
    }

    public void login(final Context context, String phone, String password) {
        if (!StringsUtils.isMobile(phone)) {
            Toast.makeText(context, "手机号码格式错误", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingHelper.showMaterLoading(context, "正在登录");
        api.login(phone, MD5.getMessageDigest(password.getBytes())).enqueue(new ApiCallback<Token>() {
            @Override
            protected void handleResponse(Token response) {
                LoadingHelper.hideMaterLoading();
                TokenCallback.handleToken(response);
                TokenCallback.checkToken((Activity) context);
                MobclickAgent.onProfileSignIn(String.valueOf(response.getAccount().getUserId()));
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                LoadingHelper.hideMaterLoading();
                super.onFailure(call, t);
            }
        });
    }

    public void registerDoctor(Context context) {
        Intent i = RegisterActivity.makeIntent(context, AuthModule.DOCTOR_TYPE);
        context.startActivity(i);
    }

    public void registerPatient(Context context) {
        Intent i = RegisterActivity.makeIntent(context, AuthModule.PATIENT_TYPE);
        context.startActivity(i);
    }

    public void resetPassword(Context context) {
        ResetPswFragment.startFrom(context);
    }

}
