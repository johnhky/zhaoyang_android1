package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityPayFailBinding;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.UrgentCall;
import com.doctor.sun.entity.handler.AppointmentHandler;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.AlipayCallback;
import com.doctor.sun.http.callback.WeChatPayCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.handler.UrgentCallHandler;

import java.util.HashMap;

import io.ganguo.library.AppManager;

/**
 * Created by lucas on 1/23/16.
 */
public class PayFailActivity extends BaseFragmentActivity2 implements View.OnClickListener {
    public static final int URGENT_CALL = 1;
    public static final int APPOINTMENT = 2;
    public static final int OTHERS = 3;
    private AppointmentModule appointmentModule = Api.of(AppointmentModule.class);

    private PActivityPayFailBinding binding;

    public static Intent makeIntent(Context context, Appointment data, boolean payWithWeChat) {
        Intent i = new Intent(context, PayFailActivity.class);
        i.putExtra(Constants.PAY_METHOD, payWithWeChat);
        i.putExtra(Constants.DATA, data);
        i.putExtra(Constants.TYPE, APPOINTMENT);
        return i;
    }

    public static Intent makeIntent(Context context, UrgentCall data, boolean payWithWeChat) {
        Intent i = new Intent(context, PayFailActivity.class);
        i.putExtra(Constants.PAY_METHOD, payWithWeChat);
        i.putExtra(Constants.DATA, data);
        i.putExtra(Constants.TYPE, URGENT_CALL);
        return i;
    }

    public static Intent makeIntent(Context context, String money, boolean payWithWeChat, HashMap<String, String> goods) {
        Intent i = new Intent(context, PayFailActivity.class);
        i.putExtra(Constants.TYPE, OTHERS);
        i.putExtra(Constants.PAY_METHOD, payWithWeChat);
        i.putExtra(Constants.MONEY, money);
        i.putExtra(Constants.EXTRA_FIELD, goods);
        return i;
    }

    private UrgentCall getUrgentCall() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    private Appointment getAppointment() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    private boolean shouldPayWithWeChat() {
        return getIntent().getBooleanExtra(Constants.PAY_METHOD, true);
    }

    private String getMoney() {
        return getIntent().getStringExtra(Constants.MONEY);
    }

    private HashMap<String, String> getExtraField() {
        return (HashMap<String, String>) getIntent().getSerializableExtra(Constants.EXTRA_FIELD);
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
                Intent intent = PMainActivity.makeIntent(PayFailActivity.this);
                startActivity(intent);
                AppManager.finishAllActivity();
                break;
            case R.id.tv_retry:
                switch (getIntent().getIntExtra(Constants.TYPE, -1)) {
                    case APPOINTMENT: {
                        AppointmentHandler handler = new AppointmentHandler(getAppointment());
                        if (shouldPayWithWeChat()) {
                            handler.payWithWeChat(this, "");
                        } else {
                            handler.payWithAlipay(this, "");
                        }
                        break;
                    }
                    case URGENT_CALL: {
                        UrgentCallHandler handler = new UrgentCallHandler(getUrgentCall());
                        if (shouldPayWithWeChat()) {
                            handler.payWithWeChat(this, "");
                        } else {
                            handler.payWithAlipay(this, "");
                        }
                        break;
                    }
                    case OTHERS: {
                        if (shouldPayWithWeChat()) {
                            appointmentModule.buildWeChatGoodsOrder(getMoney(), "wechat", getExtraField())
                                    .enqueue(new WeChatPayCallback(this, getMoney(), getExtraField()));
                            break;
                        } else {
                            appointmentModule.buildAlipayGoodsOrder(getMoney(), "alipay", getExtraField())
                                    .enqueue(new AlipayCallback(this, getMoney(), getExtraField()));
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
