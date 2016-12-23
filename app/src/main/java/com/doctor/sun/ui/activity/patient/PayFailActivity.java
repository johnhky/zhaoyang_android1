package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityPayFailBinding;
import com.doctor.sun.entity.handler.AppointmentHandler2;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.AlipayCallback;
import com.doctor.sun.http.callback.WeChatPayCallback;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.PMainActivity2;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.fragment.DrugListFragment;
import com.doctor.sun.util.JacksonUtils;

import java.util.HashMap;

/**
 * Created by lucas on 1/23/16.
 */
public class PayFailActivity extends BaseFragmentActivity2 implements View.OnClickListener {
    public static final int APPOINTMENT = 2;
    public static final int OTHERS = 3;
    private AppointmentModule appointmentModule = Api.of(AppointmentModule.class);

    private PActivityPayFailBinding binding;

    public static Intent makeIntent(Context context, Appointment data, boolean payWithWeChat) {
        Intent i = new Intent(context, PayFailActivity.class);
        i.putExtra(Constants.PAY_METHOD, payWithWeChat);
        i.putExtra(Constants.DATA, JacksonUtils.toJson(data));
        i.putExtra(Constants.TYPE, APPOINTMENT);
        return i;
    }


    public static Intent makeIntent(Context context, double money, boolean payWithWeChat, HashMap<String, String> goods) {
        Intent i = new Intent(context, PayFailActivity.class);
        i.putExtra(Constants.TYPE, OTHERS);
        i.putExtra(Constants.PAY_METHOD, payWithWeChat);
        i.putExtra(Constants.MONEY, money);
        i.putExtra(Constants.EXTRA_FIELD, goods);
        return i;
    }

    private Appointment getAppointment() {
        return JacksonUtils.fromJson(getIntent().getStringExtra(Constants.DATA), Appointment.class);
    }

    private boolean shouldPayWithWeChat() {
        return getIntent().getBooleanExtra(Constants.PAY_METHOD, true);
    }

    private double getMoney() {
        return getIntent().getDoubleExtra(Constants.MONEY, 0D);
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
                Intent intent1 = PMainActivity2.makeIntent(this);
                startActivity(intent1);
                Intent intent2 = getAppointmentOrDrugIntent();
                startActivity(intent2);
                finish();
                break;
            case R.id.tv_retry:
                switch (getIntent().getIntExtra(Constants.TYPE, -1)) {
                    case APPOINTMENT: {
                        if (shouldPayWithWeChat()) {
                            AppointmentHandler2.payWithWeChat(this, "", getAppointment());
                        } else {
                            AppointmentHandler2.payWithAlipay(this, "", getAppointment());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = PMainActivity2.makeIntent(PayFailActivity.this);
        startActivity(intent1);
        Intent intent2 = getAppointmentOrDrugIntent();
        startActivity(intent2);
        finish();
    }

    private boolean isTypeAppointment() {
        return getIntent().getIntExtra(Constants.TYPE, -1) == APPOINTMENT;
    }

    private Intent getAppointmentOrDrugIntent() {
        Intent intent;
        if (isTypeAppointment()) {
            intent = MyOrderActivity.makeIntent(this);
        } else {
            Bundle bundle = DrugListFragment.getArgs();
            intent = SingleFragmentActivity.intentFor(this, "寄药订单", bundle);
        }

        return intent;
    }
}
