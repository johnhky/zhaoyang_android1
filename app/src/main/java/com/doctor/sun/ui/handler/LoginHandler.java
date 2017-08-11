package com.doctor.sun.ui.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.AppContext;
import com.doctor.sun.BuildConfig;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Token;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.activity.LoginActivity;
import com.doctor.sun.ui.activity.ShortcutLoginActivity;
import com.doctor.sun.ui.activity.WebBrowserActivity;
import com.doctor.sun.ui.activity.patient.MedicineStoreActivity;
import com.doctor.sun.ui.fragment.RegisterFragment;
import com.doctor.sun.ui.fragment.ResetPswFragment;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.util.CountDownUtil;
import com.doctor.sun.util.MD5;
import com.doctor.sun.util.ShowCaseUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import io.ganguo.library.Config;
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
    public String captcha = "";

    public LoginHandler(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public LoginHandler(String phone) {
        this.phone = phone;
    }

    public LoginHandler() {

    }

    public boolean loginAction(Context context, String phone, String password) {
        login(context, phone, password);
        return false;
    }

    public void login(final Context context, final String phone, final String password) {
        if (!StringsUtils.isMobile(phone)) {
            Toast.makeText(context, "手机号码格式错误", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingHelper.showMaterLoading(context, "正在登录");
        HashMap<String, String> options = new HashMap<>();
        options.put("clientModel", getClientModel());
        options.put("clientVersion", getClientVersion());
        options.put("installVersion", getInstallVersion());
        options.put("userType", BuildConfig.USER_TYPE);

        String encodedPassword = MD5.getMessageDigest(password.getBytes());
        api.login(phone, encodedPassword, "", options).enqueue(new SimpleCallback<Token>() {
            @Override
            protected void handleResponse(Token response) {
                Config.putString(Constants.MYPHONE, phone);
                Config.putString(Constants.MYPASSWORD, password);
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

    public void getCaptcha(Context context, String phone, final View view) {
        if (!StringsUtils.isMobile(phone)) {
            Toast.makeText(context, "手机号码格式错误!", Toast.LENGTH_SHORT).show();
            return;
        }
        AuthModule profileModule = Api.of(AuthModule.class);

        profileModule.sendCaptcha(phone, "login").enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                CountDownUtil.countDown((TextView) view, "重新获取(%d)", "获取验证码", 60);
            }
        });
    }


    public void showLogin(Context context) {
        Intent i = new Intent();
        i.setClass(context, LoginActivity.class);
        context.startActivity(i);
        Intent toIntent = new Intent();
        toIntent.setAction(Constants.FINISH_SHORT);
        context.sendBroadcast(toIntent);
    }

    public void showShortcutLogin(Context context) {
        Intent i = new Intent();
        i.setClass(context, ShortcutLoginActivity.class);
        context.startActivity(i);
        Intent toIntent = new Intent();
        toIntent.setAction(Constants.FINISH_LOGIN);
        context.sendBroadcast(toIntent);
    }

    public void askService(final Context context) {
        TwoChoiceDialog.show(context, "020-4008352600", "取消", "呼叫", new TwoChoiceDialog.Options() {
            @Override
            public void onApplyClick(MaterialDialog dialog) {
                try {
                    Uri uri = Uri.parse("tel:4008352600");
                    Intent intent = new Intent(Intent.ACTION_CALL, uri);
                    context.startActivity(intent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelClick(MaterialDialog dialog) {

            }
        });
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

    public void viewRegistrationPolicy(Context context) {
        String str = "";
        if (BuildConfig.USER_TYPE.equals("patient")) {
            str = "readme/registration-policy?client=patient";
        } else {
            str = "readme/registration-policy?client=doctor";
        }
        String url = BuildConfig.BASE_URL + str;
        Intent i = WebBrowserActivity.intentFor(context, url, "注册须知");
        context.startActivity(i);
    }

}
