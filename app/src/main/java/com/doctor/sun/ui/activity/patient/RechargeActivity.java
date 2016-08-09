package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.PActivityRechargeBinding;
import com.doctor.sun.entity.PatientMoney;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.AlipayCallback;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.WeChatPayCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.module.ImModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;

import java.util.HashMap;

import io.ganguo.library.common.ToastHelper;

/**
 * Created by lucas on 1/6/16.
 */
public class RechargeActivity extends BaseFragmentActivity2 {
    private PActivityRechargeBinding binding;
    private ImModule api = Api.of(ImModule.class);
    private AppointmentModule appointmentModule = Api.of(AppointmentModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, RechargeActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_recharge);
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.setData(new PatientMoney());
        api.money().enqueue(new ApiCallback<PatientMoney>() {
            @Override
            protected void handleResponse(PatientMoney response) {
                binding.tvRest.setText(response.getMoney());
            }
        });
    }

    private void initListener() {
        binding.tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> extraField = new HashMap<String, String>();
                extraField.put("body", "im recharge");
                String money = binding.etMoney.getText().toString();
                if (money.equals("")) {
                    ToastHelper.showMessage(RechargeActivity.this, "请输入充值金额");
                    return;
                }
                String totalFee = money + "00";
                if (binding.rbWechat.isChecked()) {
                    appointmentModule.buildWeChatGoodsOrder(totalFee, "wechat", extraField)
                            .enqueue(new WeChatPayCallback(RechargeActivity.this, totalFee, extraField));
                } else {
                    appointmentModule.buildAlipayGoodsOrder(totalFee, "alipay", extraField)
                            .enqueue(new AlipayCallback(RechargeActivity.this, totalFee, extraField));
                }
            }
        });
    }
}
