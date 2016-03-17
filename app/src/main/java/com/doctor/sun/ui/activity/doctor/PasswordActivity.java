package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityPasswordBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.util.MD5;

import io.ganguo.library.common.ToastHelper;

/**
 * Created by lucas on 12/22/15.
 */
public class PasswordActivity extends BaseActivity2 {
    private ActivityPasswordBinding binding;
    private ProfileModule api = Api.of(ProfileModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, PasswordActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("修改密码").setRightTitle("确定");
        binding.setHeader(header);
    }

    @Override
    public void onMenuClicked() {

        super.onMenuClicked();
        api.resetPassword(MD5.getMessageDigest(binding.etOld.getText().toString().getBytes()), MD5.getMessageDigest(binding.etNew.getText().toString().getBytes()), MD5.getMessageDigest(binding.etConfirm.getText().toString().getBytes())).enqueue(new ApiCallback<String>() {
            @Override
            protected void handleResponse(String response) {

            }

            @Override
            protected void handleApi(ApiDTO<String> body) {
                ToastHelper.showMessage(PasswordActivity.this, "修改密码成功");
                finish();
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);

            }
        });
    }
}
