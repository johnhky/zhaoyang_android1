package com.doctor.sun.ui.activity.doctor;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityModifyNicknameBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ImModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;


import java.util.HashMap;

import retrofit2.Call;

/**
 * 修改备注名称
 * Created by Lynn on 1/15/16.
 */
public class ModifyNicknameActivity extends BaseFragmentActivity2 {
    private ActivityModifyNicknameBinding binding;
    private ImModule api = Api.of(ImModule.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    public void onMenuClicked() {
        if (null == getPatientId()) {
            //病人id为空,所以医生id不为空,因此要修改医生的别名
            modifyDNickName();
        } else {
            modifyPNickName();
        }
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify_nickname);
    }

    private void initData() {
        binding.etModify.setText(getIntent().getStringExtra(Constants.PARAM_NICKNAME));
    }

    private void modifyPNickName() {
        Call<ApiDTO<HashMap<String, String>>> call = api.patientNickname(
                getPatientId(),
                binding.etModify.getText().toString());
        call.enqueue(new ApiCallback<HashMap<String, String>>() {
            @Override
            protected void handleResponse(HashMap<String, String> response) {
                ModifyNicknameActivity.this.setResult(RESULT_OK,
                        new Intent().putExtra(Constants.PARAM_NICKNAME, response.get("nickname")));
                ModifyNicknameActivity.this.finish();
            }
        });
    }

    private void modifyDNickName() {
        Call<ApiDTO<HashMap<String, String>>> call = api.doctorNickname(
                getDoctorId(),
                binding.etModify.getText().toString());
        call.enqueue(new ApiCallback<HashMap<String, String>>() {
            @Override
            protected void handleResponse(HashMap<String, String> response) {
                ModifyNicknameActivity.this.setResult(RESULT_OK,
                        new Intent().putExtra(Constants.PARAM_NICKNAME, response.get("nickname")));
                ModifyNicknameActivity.this.finish();
            }
        });
    }

    private String getPatientId() {
        return getIntent().getStringExtra(Constants.PARAM_PATIENT_ID);
    }

    private int getDoctorId() {
        int stringExtra = getIntent().getIntExtra(Constants.PARAM_DOCTOR_ID, -1);
        Log.e(TAG, "getDoctorId: " + stringExtra);
        return stringExtra;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_post: {
                onMenuClicked();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getMidTitle() {
        return R.string.title_edit_nickname;
    }
}
