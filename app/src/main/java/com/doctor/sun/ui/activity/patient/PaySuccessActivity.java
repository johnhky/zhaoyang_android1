package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityPaySuccessBinding;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.PMainActivity2;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.fragment.DrugListFragment;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.util.ShowCaseUtil;

import java.text.SimpleDateFormat;

import io.ganguo.library.AppManager;

/**
 * Created by lucas on 1/23/16.
 */
public class PaySuccessActivity extends BaseFragmentActivity2 implements View.OnClickListener {
    public static final int APPOINTMENT = 1;
    public static final int PRESCRIPTION = 2;

    private PActivityPaySuccessBinding binding;

    public static Intent makeIntent(Context context, Appointment data) {
        if (data == null) {
            return makeIntent(context);
        }
        Intent i = new Intent(context, PaySuccessActivity.class);
        i.putExtra(Constants.DATA, JacksonUtils.toJson(data));
        i.putExtra(Constants.TYPE, APPOINTMENT);
        return i;
    }

//    public static Intent makeIntent(Context context, UrgentCall data) {
//        Intent i = new Intent(context, PaySuccessActivity.class);
//        i.putExtra(Constants.DATA, data);
//        i.putExtra(Constants.TYPE, URGENT_CALL);
//        return i;
//    }

    private static Intent makeIntent(Context context) {
        Intent i = new Intent(context, PaySuccessActivity.class);
        i.putExtra(Constants.TYPE, PRESCRIPTION);
        return i;
    }

    private Appointment getAppointment() {
        return JacksonUtils.fromJson(getIntent().getStringExtra(Constants.DATA), Appointment.class);
    }

//    private UrgentCall getUrgentCall() {
//        return getIntent().getParcelableExtra(Constants.DATA);
//    }

    private int getType() {
        return getIntent().getIntExtra(Constants.TYPE, -1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_pay_success);
        binding.tvMainButton.setOnClickListener(this);
        binding.tvSubButton.setOnClickListener(this);
        if (getType() == PRESCRIPTION) {
            binding.llDrugOrder.setVisibility(View.VISIBLE);
            binding.tvSubButton.setText("返回首页");
            binding.tvMainButton.setText("返回寄药订单列表");
        } else {
            binding.llAppointment.setVisibility(View.VISIBLE);
            binding.tvAppointmentTime.setText(getAppointment().getBook_time());
            if (getAppointment().getType() == 2) {
                binding.btnBack.setText("立即给医生留言");
                binding.tvEndTime.setText("咨询医生");
                binding.tvStartTime.setText("请立即开始填写问卷");
            } else {
                binding.btnBack.setText("返回咨询订单列表");
                binding.tvStartTime.setText(getAppointment().getVisit_time());
                binding.tvEndTime.setText(getAppointment().getEnd_time());
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main_button: {
                Intent intent1 = PMainActivity2.makeIntent(PaySuccessActivity.this);
                startActivity(intent1);
                if (getType() == PRESCRIPTION) {
                    Bundle bundle = DrugListFragment.getArgs();
                    Intent intent = SingleFragmentActivity.intentFor(this, "寄药订单", bundle);
                    startActivity(intent);
                }
                break;
            }
            case R.id.tv_sub_button: {
                //TODO

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
                if (getType() == PRESCRIPTION) {
                    finish();
                    Intent intent = PMainActivity2.makeIntent(this);
                    startActivity(intent);
                }
                break;
            }
            case R.id.ll_askService:
                Intent intent = MedicineStoreActivity.intentForCustomerService(this);
                startActivity(intent);
                break;
            case R.id.btn_back:
                Intent intent1 = PMainActivity2.makeIntent(PaySuccessActivity.this);
                startActivity(intent1);
                int position = 0;
                if (getType() == APPOINTMENT) {
                    if (getAppointment().getType() == 2) {
                        startActivity(PConsultingActivity.class);
                    } else {
                        Intent intent2 = MyOrderActivity.makeIntent(PaySuccessActivity.this, position);
                        startActivity(intent2);
                    }
                }
                finish();
                break;
            case R.id.btn_input:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
                if (getType() == APPOINTMENT) {
                    String id = getAppointment().getId();
                    Intent intents = PMainActivity2.makeIntent(this);
                    startActivity(intents);
                    Intent intent2 = MyOrderActivity.makeIntent(this);
                    startActivity(intent2);
                    Intent intent3 = EditQuestionActivity.intentFor(this, id, QuestionsPath.NORMAL);
                    startActivity(intent3);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = PMainActivity2.makeIntent(this);
        startActivity(intent);
        AppManager.finishAllActivity();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (getType() == APPOINTMENT) {
            ShowCaseUtil.showCase2(binding.tvSubButton, "点击开始填写问卷", -1, 12, 13, true);
        }
    }
}
