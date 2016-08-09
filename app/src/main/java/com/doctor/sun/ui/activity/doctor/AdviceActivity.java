package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityAdviceBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;

import io.ganguo.library.Config;
import io.ganguo.library.common.ToastHelper;

/**
 * Created by lucas on 1/15/16.
 */
public class AdviceActivity extends BaseFragmentActivity2 {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send: {
                onMenuClicked();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getMidTitle() {
        return R.string.title_advice;
    }

    public void onMenuClicked() {
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