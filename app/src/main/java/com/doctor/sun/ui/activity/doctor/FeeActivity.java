package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityFeeBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Fee;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;

/**
 * Created by lucas on 12/8/15.
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
        api.fee().enqueue(new ApiCallback<Fee>() {
            @Override
            protected void handleResponse(Fee response) {
                binding.etFirst.setText(String.valueOf(response.getMoney()));
                binding.etSecond.setText(String.valueOf(response.getSecond_money()));
//                binding.etElse.setText(String.valueOf(response.getCommission()));
            }
        });
    }

    private void initListener() {
        binding.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etFirst.getText().toString().isEmpty() || binding.etSecond.getText().toString().isEmpty()
//                        binding.etElse.getText().toString().isEmpty()
                        ) {
                    Toast.makeText(v.getContext(), "有必填的输入项为空", Toast.LENGTH_SHORT).show();
                } else {
                    api.setFee(binding.etFirst.getText().toString(), binding.etSecond.getText().toString()
                    ).enqueue(new ApiCallback<String>() {
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