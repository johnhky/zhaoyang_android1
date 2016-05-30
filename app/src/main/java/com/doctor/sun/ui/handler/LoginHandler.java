package com.doctor.sun.ui.handler;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.sun.entity.Token;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.activity.doctor.RegisterActivity;
import com.doctor.sun.util.MD5;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.util.Strings;
import retrofit2.Call;

/**
 * Created by rick on 11/17/15.
 */
public class LoginHandler extends BaseHandler {
    public static final String TAG = LoginHandler.class.getSimpleName();

    private AuthModule api = Api.of(AuthModule.class);

    private final LoginInput mInput;

    public LoginHandler(Activity context) {
        super(context);
        try {
            mInput = (LoginInput) context;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("The host Activity must implement LoginInput");
        }

        if (TokenCallback.isLogin()) {
            TokenCallback.checkToken(getContext());
        }
    }

    public void login(View view) {
        String password = mInput.getPassword();
        String phone = mInput.getPhone();
        if (!Strings.isMobile(phone)) {
            Toast.makeText(getContext(), "手机号码格式错误", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingHelper.showMaterLoading(getContext(), "正在登录");
        api.login(phone, MD5.getMessageDigest(password.getBytes())).enqueue(new ApiCallback<Token>() {
            @Override
            protected void handleResponse(Token response) {
                Log.d(TAG, "login: " + response.getAccount().getVoipAccount());
                LoadingHelper.hideMaterLoading();
                TokenCallback.handleToken(response);
                TokenCallback.checkToken(getContext());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                LoadingHelper.hideMaterLoading();
                super.onFailure(call, t);
            }
        });
    }

    public void registerDoctor(View view) {
        Intent i = RegisterActivity.makeIntent(getContext(), AuthModule.DOCTOR_TYPE);
        getContext().startActivity(i);
    }

    public void registerPatient(View view) {
        Intent i = RegisterActivity.makeIntent(getContext(), AuthModule.PATIENT_TYPE);
        getContext().startActivity(i);
    }

    public void resetPassword(View view) {
        Intent i = RegisterActivity.makeIntent(getContext(), AuthModule.FORGOT_PASSWORD);
        getContext().startActivity(i);
    }

    public TextView.OnEditorActionListener loginAction() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login(v);
                }
                return false;
            }
        };
    }


    public interface LoginInput {
        String getPhone();

        String getPassword();
    }

}
