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
import com.doctor.sun.util.JacksonUtils;

import io.ganguo.library.AppManager;

/**
 * Created by lucas on 1/23/16.
 */
public class PaySuccessActivity extends BaseFragmentActivity2 implements View.OnClickListener {
    public static final int URGENT_CALL = 1;
    public static final int APPOINTMENT = 2;
    public static final int VOIP_PAY = 3;

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
        i.putExtra(Constants.TYPE, VOIP_PAY);
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
        binding.tvMain.setOnClickListener(this);
        binding.tvQuestion.setOnClickListener(this);
        if (getType() == VOIP_PAY) {
            binding.tvSystemTip.setVisibility(View.GONE);
            binding.tvQuestion.setText("返回订单列表");
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
            case R.id.tv_main: {
                Intent intent = PMainActivity.intentFor(this);
                startActivity(intent);
                AppManager.finishAllActivity();
                break;
            }
            case R.id.tv_question: {
                //TODO
                String id = getAppointment().getId();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
                if (getType() == APPOINTMENT) {
                    Intent intent1 = PMainActivity.intentFor(this);
                    startActivity(intent1);
                    Intent intent2 = EditQuestionActivity.intentFor(this, id, QuestionsPath.NORMAL);
                    startActivity(intent2);
                }

                if (getType() == VOIP_PAY) {
                    finish();
                    Intent intent = PAppointmentListActivity.makeIntent(this);
                    startActivity(intent);
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = PMainActivity.intentFor(this);
        startActivity(intent);
        AppManager.finishAllActivity();

    }
}
