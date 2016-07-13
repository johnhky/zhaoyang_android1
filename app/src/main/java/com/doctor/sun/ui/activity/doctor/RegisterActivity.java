package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityRegisterBinding;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.handler.RegisterHandler;
import com.doctor.sun.ui.model.HeaderViewModel;


/**
 * Created by rick on 11/17/15.
 */
public class RegisterActivity extends BaseActivity2 implements RegisterHandler.RegisterInput {

    private ActivityRegisterBinding binding;
    private HeaderViewModel header;
    private RegisterHandler handler;

    public static Intent makeIntent(Context context, int data) {
        Intent i = new Intent(context, RegisterActivity.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }


    private int getData() {
        return getIntent().getIntExtra(Constants.DATA, -1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        header = new HeaderViewModel(this);
        header.setEnable(false);
        int type = getData();
        header.setRightTitle("下一步");
        switch (type) {
            case AuthModule.DOCTOR_TYPE: {
                header.setMidTitle("医生注册");
                break;
            }

            case AuthModule.PATIENT_TYPE: {
                header.setMidTitle("公众注册");
                break;
            }

            case AuthModule.FORGOT_PASSWORD: {
                header.setMidTitle("重置密码");
                header.setRightTitle("确定");
//                binding.llyEmail.setVisibility(View.VISIBLE);
                binding.llyPolicy.setVisibility(View.GONE);
                header.setEnable(true);
                break;
            }
            default:
        }
        binding.setHeader(header);
        handler = new RegisterHandler(this, type);
        binding.setHandler(handler);
    }

    @Override
    public void onMenuClicked() {
        int type = getData();
        if (type == AuthModule.FORGOT_PASSWORD) {
            handler.resetPassword(null);
        } else {
            handler.register();
        }
    }

    @Override
    public String getEmail() {
        return binding.etEmail.getText().toString();
    }

    @Override
    public String getPhone() {
        return binding.etPhone.getText().toString();
    }

    @Override
    public String getCaptcha() {
        return binding.etCaptcha.getText().toString();
    }

    @Override
    public String getPassword() {
        return binding.etPasswd.getText().toString();
    }

    @Override
    public String getPassword2() {
        return binding.etPasswd2.getText().toString();
    }

    @Override
    public boolean isDoctor() {
        return getData() == AuthModule.DOCTOR_TYPE;
    }
}
