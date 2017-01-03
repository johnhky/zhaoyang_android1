package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
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
            binding.tvSystemTip.setVisibility(View.GONE);
            binding.tvSubButton.setText("返回首页");
            binding.tvMainButton.setText("返回寄药订单列表");
            binding.tvTip.setVisibility(View.GONE);
        } else {
            setBookTime();
        }
    }

    private void setBookTime() {
        switch (getType()) {
            case APPOINTMENT: {
                String bookTime = getAppointment().getBook_time();
                binding.setData(bookTime);
                break;
            }
            default: {

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main_button: {
                Intent intent1 = PMainActivity2.makeIntent(PaySuccessActivity.this);
                startActivity(intent1);
                int position = 0;
                if (getType() == APPOINTMENT) {
                    Intent intent2 = MyOrderActivity.makeIntent(PaySuccessActivity.this, position);
                    startActivity(intent2);
                    finish();
                }else if (getType() == PRESCRIPTION) {
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
                if (getType() == APPOINTMENT) {
                    String id = getAppointment().getId();
                    Intent intent1 = PMainActivity2.makeIntent(this);
                    startActivity(intent1);
                    Intent intent2 = MyOrderActivity.makeIntent(this);
                    startActivity(intent2);
                    Intent intent3 = EditQuestionActivity.intentFor(this, id, QuestionsPath.NORMAL);
                    startActivity(intent3);
                }

                if (getType() == PRESCRIPTION) {
                    finish();
                    Intent intent = PMainActivity2.makeIntent(this);
                    startActivity(intent);
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = PMainActivity2.makeIntent(this);
        startActivity(intent);
        AppManager.finishAllActivity();

    }
}
