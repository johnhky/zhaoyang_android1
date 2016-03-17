package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityAddFeeBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.EmergencyCall;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.EmergencyModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.handler.EmergencyCallHandler;
import com.doctor.sun.ui.model.HeaderViewModel;

/**
 * Created by lucas on 1/23/16.
 */
public class UrgentAddFeeActivity extends BaseActivity2 {
    private PActivityAddFeeBinding binding;
    private EmergencyModule api = Api.of(EmergencyModule.class);

    public static Intent makeIntent(Context context, EmergencyCall data) {
        Intent i = new Intent(context, UrgentAddFeeActivity.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }


    private EmergencyCall getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_add_fee);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("增加诊金");
        binding.setData(getData());
        binding.setHeader(header);
        binding.tvTime.setText(getData().getCreatedAt().substring(0, getData().getCreatedAt().length() - 3));
        binding.tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.addMoney(getData().getId(), binding.etFee.getText().toString()).enqueue(new SimpleCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {
                        EmergencyCallHandler handler = new EmergencyCallHandler(getData());
                        if (binding.rbAlipay.isChecked()) {
                            handler.payWithAlipay(UrgentAddFeeActivity.this);
                        } else {
                            handler.payWithWeChat(UrgentAddFeeActivity.this);
                        }
                    }
                });
            }
        });
    }
}
