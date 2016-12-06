package com.doctor.sun.ui.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.BuildConfig;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Token;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.fragment.RegisterFragment;
import com.doctor.sun.ui.fragment.ResetPswFragment;
import com.doctor.sun.util.MD5;
import com.umeng.analytics.MobclickAgent;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.util.StringsUtils;
import io.ganguo.library.util.Systems;
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

        api.login(phone, MD5.getMessageDigest(password.getBytes()),
                getClientModel(), getClientVersion(), getInstallVersion()).enqueue(new SimpleCallback<Token>() {
            @Override
            protected void handleResponse(Token response) {
                LoadingHelper.hideMaterLoading();
                TokenCallback.handleToken(response);
                TokenCallback.loadDoctorProfile((Activity) context);
                MobclickAgent.onProfileSignIn(String.valueOf(response.getAccount().getUserId()));
            }

            @Override
            public void onFailure(Call<ApiDTO<Token>> call, Throwable t) {
                LoadingHelper.hideMaterLoading();
                super.onFailure(call, t);
            }
        });

        if (BuildConfig.DEV_MODE) {
            Log.e(TAG, "clientVersion:" + getClientVersion());
            Log.e(TAG, "clientModel:" + getClientModel());
            Log.e(TAG, "installVersion:" + getInstallVersion());
        }
    }

    public void registerDoctor(Context context) {
        Intent i = RegisterFragment.intentFor(context);
        context.startActivity(i);
    }

    public void resetPassword(Context context) {
        ResetPswFragment.startFrom(context);
    }

    private String getClientVersion() {
        return "android:" + Build.VERSION.RELEASE + " sdk:" + Build.VERSION.SDK_INT;
    }

    private String getClientModel() {
        return Build.MANUFACTURER + Build.MODEL;
    }

    private String getInstallVersion() {
        return Systems.getVersionName(AppContext.me()) + "";
    }
}
