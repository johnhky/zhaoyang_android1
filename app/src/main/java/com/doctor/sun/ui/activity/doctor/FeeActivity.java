package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.databinding.ActivityFeeBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;

import java.util.Locale;

/**
 * Created by lucas on 12/8/15.
 * 诊金设置
 */
public class FeeActivity extends BaseFragmentActivity2 {

    private ActivityFeeBinding binding;

    private ProfileModule api = Api.of(ProfileModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, FeeActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fee);
    }

    private void initData() {
        Doctor doctor = Settings.getDoctorProfile();
        binding.etFirst.setText(String.format(Locale.CHINA, "%.0f", doctor.getMoney()));
        binding.etSecond.setText(String.format(Locale.CHINA, "%.0f", doctor.getSecondMoney()));
    }

    private void initListener() {
        binding.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = binding.etFirst.getText().toString();
                String secondMoney = binding.etSecond.getText().toString();
                if (money.isEmpty() || secondMoney.isEmpty()) {
                    Toast.makeText(v.getContext(), "有必填的输入项为空", Toast.LENGTH_SHORT).show();
                } else {
                    api.setFee(money, secondMoney)
                            .enqueue(new ApiCallback<String>() {
                                @Override
                                protected void handleResponse(String response) {
                                }

                                @Override
                                protected void handleApi(ApiDTO<String> body) {
                                    Toast.makeText(FeeActivity.this, "诊金设置完成", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                }
            }
        });
    }


    @Override
    public int getMidTitle() {
        return R.string.title_edit_fee;
    }
}