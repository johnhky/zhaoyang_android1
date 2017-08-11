package com.doctor.sun.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.BuildConfig;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityLoginBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Token;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.handler.LoginHandler;
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
 * Created by rick on 10/23/15.
 */
public class LoginActivity extends BaseFragmentActivity2 {

    private ActivityLoginBinding binding;
    private LoginHandler handler;
    private AuthModule api = Api.of(AuthModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        if (null != Config.getString(Constants.MYPHONE)) {
            handler = new LoginHandler(Config.getString(Constants.MYPHONE), Config.getString(Constants.MYPASSWORD));
        } else {
            handler = new LoginHandler();
        }

        binding.setHandler(handler);
        initLisitener();
        initData();
    }

    public void initData() {

    }

    public void initLisitener() {
        binding.tvShortLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.llShortLogin.setVisibility(View.VISIBLE);
                binding.llLogin.setVisibility(View.GONE);
                binding.llLoginBg.setBackgroundResource(R.drawable.short_login_bg);
                binding.llRead.setVisibility(View.VISIBLE);
                binding.tvForgotPassword.setVisibility(View.GONE);
                binding.tvShortLogin.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                binding.tvLogin.setTextColor(getResources().getColor(R.color.main_txt_color));
            }
        });
        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.llLogin.setVisibility(View.VISIBLE);
                binding.llShortLogin.setVisibility(View.GONE);
                binding.llLoginBg.setBackgroundResource(R.drawable.login_bg);
                binding.llRead.setVisibility(View.GONE);
                binding.tvForgotPassword.setVisibility(View.VISIBLE);
                binding.tvLogin.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                binding.tvShortLogin.setTextColor(getResources().getColor(R.color.main_txt_color));
            }
        });
        binding.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCaptcha(v.getContext(), binding.etPhone1.getText().toString(), binding.btnCheck);
            }
        });
        binding.flLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOnCaptcha(v.getContext(), binding.etPhone1.getText().toString(), binding.etPassword1.getText().toString());
            }
        });
    }

    public void getCaptcha(Context context, String phone, final View view) {
        if (!StringsUtils.isMobile(phone)) {
            Toast.makeText(context, "手机号码格式错误!", Toast.LENGTH_SHORT).show();
            return;
        }
        AuthModule profileModule = Api.of(AuthModule.class);
        LoadingHelper.showMaterLoading(context, "正在加载...");
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

}
