package com.doctor.sun.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.BuildConfig;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityShortcutLoginBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Token;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.activity.doctor.SettingActivity;
import com.doctor.sun.ui.handler.LoginHandler;
import com.doctor.sun.util.AddressTask;
import com.doctor.sun.util.CountDownUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import io.ganguo.library.Config;
import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.util.StringsUtils;
import io.ganguo.library.util.Systems;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heky on 17/5/24.
 */

public class ShortcutLoginActivity extends BaseFragmentActivity2 {

    ActivityShortcutLoginBinding binding;
    LoginHandler handler;
    private AuthModule api = Api.of(AuthModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, ShortcutLoginActivity.class);
        return i;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.FINISH_SHORT);
        registerReceiver(receiver, intentFilter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shortcut_login);
        if (null != Config.getString(Constants.MYPHONE)) {
            handler = new LoginHandler(Config.getString(Constants.MYPHONE));
        } else {
            handler = new LoginHandler();
        }
        binding.setHandler(handler);

        binding.flLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOnCaptcha(v.getContext(), binding.etPhone.getText().toString(), binding.etPassword.getText().toString());
            }
        });
        binding.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCaptcha(v.getContext(), binding.etPhone.getText().toString(), binding.btnCheck);
            }
        });
    }

    public void loginOnCaptcha(final Context context, final String phone, String captcha) {
        if (!StringsUtils.isMobile(phone)) {
            Toast.makeText(context, "手机号码格式错误!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringsUtils.isEmpty(captcha)) {
            Toast.makeText(context, "验证码不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingHelper.showMaterLoading(context, "正在登录");
        HashMap<String, String> options = new HashMap<>();
        options.put("clientModel", getClientModel());
        options.put("clientVersion", getClientVersion());
        options.put("installVersion", getInstallVersion());
        options.put("userType", BuildConfig.USER_TYPE);
        api.login(phone, captcha, options).enqueue(new SimpleCallback<Token>() {
            @Override
            protected void handleResponse(Token response) {
                initialization();
                Config.putString(Constants.MYPHONE, phone);
                LoadingHelper.hideMaterLoading();
                TokenCallback.handleToken(response);
                int isDoctor = BuildConfig.IS_DOCTOR;
                switch (isDoctor) {
                    case IntBoolean.TRUE:
                        TokenCallback.loadDoctorProfile((Activity) context);
                        break;
                    case IntBoolean.FALSE:
                        TokenCallback.loadPatientProfile((Activity) context);
                        break;
                    case IntBoolean.NOT_GIVEN:
                        TokenCallback.checkToken((Activity) context);
                        break;
                    default: {
                        TokenCallback.checkToken((Activity) context);
                        break;
                    }
                }
                MobclickAgent.onProfileSignIn(String.valueOf(response.getAccount().getUserId()));
            }

            @Override
            public void onFailure(Call<ApiDTO<Token>> call, Throwable t) {
                super.onFailure(call, t);
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    private void initialization() {
        api.initialization("").enqueue(new Callback<ApiDTO>() {
            @Override
            public void onResponse(Call<ApiDTO> call, Response<ApiDTO> response) {

            }

            @Override
            public void onFailure(Call<ApiDTO> call, Throwable t) {

            }
        });

    }

    public void getCaptcha(Context context, String phone, final View view) {
        if (!StringsUtils.isMobile(phone)) {
            Toast.makeText(context, "手机号码格式错误!", Toast.LENGTH_SHORT).show();
            return;
        }
        AuthModule profileModule = Api.of(AuthModule.class);
        LoadingHelper.showMaterLoading(context,"正在加载...");
        profileModule.sendCaptcha(phone, "login").enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                LoadingHelper.hideMaterLoading();
                CountDownUtil.countDown((TextView) view, "重新获取(%d)", "获取验证码", 60);
            }

            @Override
            public void onFailure(Call<ApiDTO<String>> call, Throwable t) {
                super.onFailure(call, t);
                LoadingHelper.hideMaterLoading();
            }
        });
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

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.FINISH_SHORT)) {
                finish();
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
