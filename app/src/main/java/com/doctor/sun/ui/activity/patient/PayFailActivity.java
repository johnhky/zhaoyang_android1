package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityPayFailBinding;
import com.doctor.sun.entity.AppointMent;
import com.doctor.sun.entity.EmergencyCall;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.AlipayCallback;
import com.doctor.sun.http.callback.WeChatPayCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.handler.AppointmentHandler;
import com.doctor.sun.ui.handler.EmergencyCallHandler;

import io.ganguo.library.AppManager;

/**
 * Created by lucas on 1/23/16.
 */
public class PayFailActivity extends BaseActivity2 implements View.OnClickListener {
    public static final int URGENT_CALL = 1;
    public static final int APPOINTMENT = 2;
    public static final int VOIP_PAY = 3;
    private AppointmentModule appointmentModule = Api.of(AppointmentModule.class);

    private PActivityPayFailBinding binding;

    public static Intent makeIntent(Context context, AppointMent data, boolean payWithWeChat) {
        Intent i = new Intent(context, PayFailActivity.class);
        i.putExtra(Constants.PAY_METHOD, payWithWeChat);
        i.putExtra(Constants.DATA, data);
        i.putExtra(Constants.TYPE, APPOINTMENT);
        return i;
    }

    public static Intent makeIntent(Context context, EmergencyCall data, boolean payWithWeChat) {
        Intent i = new Intent(context, PayFailActivity.class);
        i.putExtra(Constants.PAY_METHOD, payWithWeChat);
        i.putExtra(Constants.DATA, data);
        i.putExtra(Constants.TYPE, URGENT_CALL);
        return i;
    }

    public static Intent makeIntent(Context context, int money, String payType) {
        Intent i = new Intent(context, PayFailActivity.class);
        i.putExtra(Constants.TYPE, VOIP_PAY);
        i.putExtra(Constants.PAYTYPE, payType);
        i.putExtra(Constants.MONEY, money);
        return i;
    }

    private EmergencyCall getUrgentCall() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    private AppointMent getAppointment() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    private boolean shouldPayWithWeChat() {
        return getIntent().getBooleanExtra(Constants.PAY_METHOD, true);
    }

    private String getPayType() {
        return getIntent().getStringExtra(Constants.PAYTYPE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_pay_fail);
        binding.tvRetry.setOnClickListener(this);
        binding.tvMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main:
                Intent intent = MainActivity.makeIntent(PayFailActivity.this);
                startActivity(intent);
                AppManager.finishAllActivity();
                break;
            case R.id.tv_retry:
                switch (getIntent().getIntExtra(Constants.TYPE, -1)) {
                    case APPOINTMENT: {
                        AppointmentHandler handler = new AppointmentHandler(getAppointment());
                        if (shouldPayWithWeChat()) {
                            handler.payWithWeChat(this);
                        } else {
                            handler.payWithAlipay(this);
                        }
                        break;
                    }
                    case URGENT_CALL: {
                        EmergencyCallHandler handler = new EmergencyCallHandler(getUrgentCall());
                        if (shouldPayWithWeChat()) {
                            handler.payWithWeChat(this);
                        } else {
                            handler.payWithAlipay(this);
                        }
                        break;
                    }
                    case VOIP_PAY: {
                        switch (getPayType()) {
                            case "wechat":
                                appointmentModule.rechargeOrderWithWechat(getIntent().getIntExtra(Constants.MONEY, -1),  "im recharge", "wechat")
                                        .enqueue(new WeChatPayCallback(this, getIntent().getIntExtra(Constants.MONEY, -1)));
                                break;
                            case "alipay":
                                appointmentModule.rechargeOrderWithAlipay(getIntent().getIntExtra(Constants.MONEY, -1),  "im recharge", "alipay")
                                        .enqueue(new AlipayCallback(this, getIntent().getIntExtra(Constants.MONEY, -1)));
                                break;
                        }
                    }
                    default: {
                        break;
                    }
                }
        }
    }
}
