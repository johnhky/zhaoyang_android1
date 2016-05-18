package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.bean.Province;
import com.doctor.sun.databinding.ActivityLoginBinding;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.ui.handler.LoginHandler;

import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;


/**
 * Created by rick on 10/23/15.
 */
public class LoginActivity extends BaseActivity2 implements LoginHandler.LoginInput {

    private ActivityLoginBinding binding;
    private LoginHandler handler;

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        handler = new LoginHandler(this);
        binding.setHandler(handler);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler = null;
    }

    @Override
    public String getPhone() {
        return binding.etPhone.getText().toString();
    }

    @Override
    public String getPassword() {
        return binding.etPassword.getText().toString();
    }
}
