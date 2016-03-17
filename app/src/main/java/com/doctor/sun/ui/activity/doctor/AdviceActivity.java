package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityAdviceBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.model.HeaderViewModel;

import io.ganguo.library.Config;
import io.ganguo.library.common.ToastHelper;

/**
 * Created by lucas on 1/15/16.
 */
public class AdviceActivity extends BaseActivity2 {
    private ActivityAdviceBinding binding;
    private ProfileModule api = Api.of(ProfileModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, AdviceActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_advice);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("建议反馈").setRightTitle("发送");
        binding.setHeader(header);
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
        switch (Config.getInt(Constants.USER_TYPE, -1)) {
            case AuthModule.PATIENT_TYPE:
                api.setPatientFeedback(binding.etFeedback.getText().toString()).enqueue(new ApiCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {

                    }

                    @Override
                    protected void handleApi(ApiDTO<String> body) {
                        ToastHelper.showMessage(AdviceActivity.this, "发送成功");
                        finish();
                    }
                });
                break;
            case AuthModule.DOCTOR_PASSED:
                api.setDoctorFeedback(binding.etFeedback.getText().toString()).enqueue(new ApiCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {

                    }

                    @Override
                    protected void handleApi(ApiDTO<String> body) {
                        ToastHelper.showMessage(AdviceActivity.this, "发送成功");
                        finish();
                    }
                });
        }
    }
}